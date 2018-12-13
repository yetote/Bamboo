//
// Created by ether on 2018/12/7.
//





#include "AudioPlayer.h"


AudioPlayer::AudioPlayer(PlayerStatus *playStatus, int sampleRate, PlayerCallJava *playerCallJava) {
    this->sampleRate = sampleRate;
    this->playstatus = playStatus;
    this->blockQueue = new BlockQueue(playstatus);
    this->playerCallJava = playerCallJava;
    buffer = static_cast<uint8_t *>(malloc(sampleRate * 2 * 2));
    pthread_mutex_init(&codecMutex, null);
}

AudioPlayer::~AudioPlayer() {
    pthread_mutex_destroy(&codecMutex);
}

void playerCallBack(SLAndroidSimpleBufferQueueItf bf, void *context) {
    AudioPlayer *player = static_cast<AudioPlayer *>(context);
    if (player != null) {
        int bufferSize = player->resampleAudio();
        if (bufferSize > 0) {
            player->clock += bufferSize / ((double) player->sampleRate * 2 * 2);
            if (player->clock - player->last_time >= 0.1) {
                player->last_time = player->clock;
                player->playerCallJava->onCallTimeInfo(CHILD_THREAD, player->clock,
                                                       player->duration);
            }
            (*player->bufferQueueItf)->Enqueue(player->bufferQueueItf, player->buffer, bufferSize);
        }
    }
}

void *decodePlay(void *data) {
    AudioPlayer *audio = static_cast<AudioPlayer *>(data);
    audio->initOpenSLES();
    pthread_exit(&audio->playThread);
}

void AudioPlayer::play() {
    pthread_create(&playThread, null, decodePlay, this);
}

int AudioPlayer::resampleAudio() {
    int ret;
    int data_size = 0;
    while (playstatus != NULL && !playstatus->isExit) {

        if (blockQueue->getQueueSize() == 0) {
            if (!playstatus->isLoad) {
                playstatus->isLoad = true;
                playerCallJava->onCallLoad(CHILD_THREAD, true);
            }
            av_usleep(1000 * 100);
            continue;
        } else {
            if (playstatus->isLoad) {
                playstatus->isLoad = false;
                playerCallJava->onCallLoad(CHILD_THREAD, false);
            }
        }

        avPacket = av_packet_alloc();
        if (blockQueue->getAvpacket(avPacket) != 0) {
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            LOGE("获取packet失败");
            continue;
        }
        pthread_mutex_lock(&codecMutex);

        ret = avcodec_send_packet(pCodecCtx, avPacket);
        if (ret != 0) {
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            LOGE("发送packet失败%d", ret);
            pthread_mutex_unlock(&codecMutex);
            continue;
        }
        avFrame = av_frame_alloc();
        ret = avcodec_receive_frame(pCodecCtx, avFrame);
        if (ret == 0) {

            if (avFrame->channels > 0 && avFrame->channel_layout == 0) {
                avFrame->channel_layout = av_get_default_channel_layout(avFrame->channels);
            } else if (avFrame->channels == 0 && avFrame->channel_layout > 0) {
                avFrame->channels = av_get_channel_layout_nb_channels(avFrame->channel_layout);
            }

            SwrContext *swr_ctx;

            swr_ctx = swr_alloc_set_opts(
                    NULL,
                    AV_CH_LAYOUT_STEREO,
                    AV_SAMPLE_FMT_S16,
                    avFrame->sample_rate,
                    avFrame->channel_layout,
                    (AVSampleFormat) avFrame->format,
                    avFrame->sample_rate,
                    NULL, NULL
            );
            if (!swr_ctx || swr_init(swr_ctx) < 0) {
                av_packet_free(&avPacket);
                av_free(avPacket);
                avPacket = NULL;
                av_frame_free(&avFrame);
                av_free(avFrame);
                avFrame = NULL;
                swr_free(&swr_ctx);
                LOGE("swr初始化失败");
                pthread_mutex_unlock(&codecMutex);
                continue;
            }

            int nb = swr_convert(
                    swr_ctx,
                    &buffer,
                    avFrame->nb_samples,
                    (const uint8_t **) avFrame->data,
                    avFrame->nb_samples);

            int out_channels = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
            data_size = nb * out_channels * av_get_bytes_per_sample(AV_SAMPLE_FMT_S16);

            now_time = avFrame->pts * av_q2d(time_base);
            if (now_time < clock) {
                now_time = clock;
            }
            clock = now_time;

            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame = NULL;
            swr_free(&swr_ctx);
            pthread_mutex_unlock(&codecMutex);
            break;
        } else {
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;
            av_frame_free(&avFrame);
            av_free(avFrame);
            avFrame = NULL;
            LOGE("接受packet失败%d", ret);
            pthread_mutex_unlock(&codecMutex);
            continue;
        }
    }

    return data_size;
}

void AudioPlayer::initOpenSLES() {
    SLresult result;
    result = slCreateEngine(&engineObj, 0, 0, 0, 0, 0);
    if (result != success) {
        LOGE("创建引擎失败");
        return;
    }
    result = (*engineObj)->Realize(engineObj, false);
    if (result != success) {
        LOGE("引擎对象实现失败");
        return;
    }
    result = (*engineObj)->GetInterface(engineObj, SL_IID_ENGINE, &engineItf);
    if (result != success) {
        LOGE("获取引擎接口失败");
        return;
    }
    const SLInterfaceID ids[1] = {SL_IID_ENVIRONMENTALREVERB};
    const SLboolean req[1] = {sl_true};

    result = (*engineItf)->CreateOutputMix(engineItf, &mixObj, 1, ids, req);
    if (result != success) {
        LOGE("创建混音器失败");
        return;
    }
    result = (*mixObj)->Realize(mixObj, false);
    if (result != success) {
        LOGE("混音器实现失败");
        return;
    }
    result = (*mixObj)->GetInterface(mixObj, SL_IID_ENVIRONMENTALREVERB, &environmentalReverbItf);
    if (result != success) {
        LOGE("获取环境混音器接口失败,请检查MODIFY_AUDIO_SETTINGS权限是否开启");
        return;
    }
    reverbSettings = SL_I3DL2_ENVIRONMENT_PRESET_STONECORRIDOR;
    result = (*environmentalReverbItf)->SetEnvironmentalReverbProperties(environmentalReverbItf,
                                                                         &reverbSettings);
    if (result != success) {
        LOGE("设置混音效果失败");
    }
    SLDataLocator_AndroidSimpleBufferQueue bufferQueue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE,
                                                          2};
    SLDataFormat_PCM fmt = {
            SL_DATAFORMAT_PCM,
            2,
            getCurrentSamplRate(sampleRate),
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_PCMSAMPLEFORMAT_FIXED_16,
            SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
            SL_BYTEORDER_LITTLEENDIAN
    };
    SLDataSource audioSrc = {&bufferQueue, &fmt};
    SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, mixObj};
    SLDataSink audioSink = {&outputMix, null};

    const SLInterfaceID mids[2] = {SL_IID_BUFFERQUEUE, SL_IID_PLAYBACKRATE};
    const SLboolean mreq[2] = {sl_true, sl_true};

    result = (*engineItf)->CreateAudioPlayer(engineItf, &playerObj, &audioSrc, &audioSink, 2, mids,
                                             mreq);
    if (result != success) {
        LOGE("创建播放器失败");
        return;
    }
    result = (*playerObj)->Realize(playerObj, false);
    if (result != success) {
        LOGE("播放器实现失败");
        return;
    }
    result = (*playerObj)->GetInterface(playerObj, SL_IID_PLAY, &playItf);
    if (result != success) {
        LOGE("播放器接口获取失败");
        return;
    }
    result = (*playerObj)->GetInterface(playerObj, SL_IID_BUFFERQUEUE, &bufferQueueItf);
    if (result != success) {
        LOGE("队列接口获取失败");
        return;
    }
    result = (*bufferQueueItf)->RegisterCallback(bufferQueueItf, playerCallBack, this);
    if (result != success) {
        LOGE("回调接口失败");
        return;
    }
    result = (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_PLAYING);
    if (result != success) {
        LOGE("设置播放状态失败");
        return;
    }
    result = (*playerObj)->GetInterface(playerObj, SL_IID_VOLUME, &volumeItf);
    if (result != success) {
        LOGE("获取音量接口失败");
    }
    playerCallBack(bufferQueueItf, this);
}

SLuint32 AudioPlayer::getCurrentSamplRate(int sampleRate) {
    SLuint32 rate = 0;
    switch (sampleRate) {
        case 8000:
            rate = SL_SAMPLINGRATE_8;
            break;
        case 11025:
            rate = SL_SAMPLINGRATE_11_025;
            break;
        case 12000:
            rate = SL_SAMPLINGRATE_12;
            break;
        case 16000:
            rate = SL_SAMPLINGRATE_16;
            break;
        case 22050:
            rate = SL_SAMPLINGRATE_22_05;
            break;
        case 24000:
            rate = SL_SAMPLINGRATE_24;
            break;
        case 32000:
            rate = SL_SAMPLINGRATE_32;
            break;
        case 44100:
            rate = SL_SAMPLINGRATE_44_1;
            break;
        case 48000:
            rate = SL_SAMPLINGRATE_48;
            break;
        case 64000:
            rate = SL_SAMPLINGRATE_64;
            break;
        case 88200:
            rate = SL_SAMPLINGRATE_88_2;
            break;
        case 96000:
            rate = SL_SAMPLINGRATE_96;
            break;
        case 192000:
            rate = SL_SAMPLINGRATE_192;
            break;
        default:
            rate = SL_SAMPLINGRATE_44_1;
            break;
    }
    return rate;
}

void AudioPlayer::resume() {
    if (playItf != null) {
        (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_PLAYING);
    }
}

void AudioPlayer::pause() {
    if (playItf != null) {
        (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_PAUSED);
    }
}

void AudioPlayer::stop() {
    if (playItf != null) {
        (*playItf)->SetPlayState(playItf, SL_PLAYSTATE_STOPPED);
    }
}

void AudioPlayer::release() {
    stop();
    if (blockQueue != null) {
        delete blockQueue;
        blockQueue = null;
    }
    if (playerObj != null) {
        (*playerObj)->Destroy(playerObj);
        playerObj = null;
        playItf = null;
        bufferQueueItf = null;
    }
    if (mixObj != null) {
        (*mixObj)->Destroy(mixObj);
        mixObj = null;
        environmentalReverbItf = null;
    }
    if (engineObj != null) {
        (*engineObj)->Destroy(engineObj);
        engineObj = null;
        engineItf = null;
    }
    if (buffer != null) {
        free(buffer);
        buffer = null;
    }
    if (pCodecCtx != null) {
        avcodec_close(pCodecCtx);
        avcodec_free_context(&pCodecCtx);
        av_free(pCodecCtx);
        pCodecCtx = null;
    }
    if (playstatus != null) {
        playstatus = null;
    }
    if (playerCallJava != null) {
        playerCallJava = null;
    }
}

void AudioPlayer::setVolume(int percent) {
    if (volumeItf != null) {
        (*volumeItf)->SetVolumeLevel(volumeItf, (100 - percent) * -50);
    }
}
