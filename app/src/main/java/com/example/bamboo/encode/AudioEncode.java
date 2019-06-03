package com.example.bamboo.encode;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import java.io.IOException;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/5/31 14:49
 * @change
 * @chang time
 * @class describe
 */
public class AudioEncode {
    private int sampleRate, channelLayout;
    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;

    public AudioEncode(int sampleRate, int channelLayout) {
        this.sampleRate = sampleRate;
        this.channelLayout = channelLayout;
        mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelLayout);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, sampleRate * channelLayout);
        mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectERLC);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MediaFormat getMediaFormat() {
        return mediaFormat;
    }
}
