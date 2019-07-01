package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/6/26 16:58
 * @change
 * @chang time
 * @class describe
 */
public class EncodeAudio {
    private static final String TAG = "EncodeAudio";
    private boolean isStart;
    private long pts, lastPts;
    private MediaCodec.Callback callback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
            if (index >= 0) {
                ByteBuffer buffer = codec.getInputBuffer(index);
                if (buffer != null) {
                    try {
                        byte[] data = audioQueue.poll(1, TimeUnit.SECONDS);
                        int flag = 0;
                        if (!isStart && audioQueue.isEmpty()) {
                            Log.e(TAG, "onInputBufferAvailable: 音频最后一帧");
                            flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                        }
                        buffer.clear();
                        if (data != null) {
                            buffer.put(data);
                            codec.queueInputBuffer(index, 0, data.length, System.currentTimeMillis() * 1000L - pts, flag);
                        }else {
                            codec.queueInputBuffer(index, 0, 0, System.currentTimeMillis() * 1000L - pts, flag);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {
            if (index >= 0) {
                ByteBuffer buffer = codec.getOutputBuffer(index);
                if (buffer != null) {
                    if (lastPts < info.presentationTimeUs) {
                        mutexUtil.writeData(buffer, info, true);
                        lastPts = info.presentationTimeUs;
                    } else {
                        Log.e(TAG, "onOutputBufferAvailable: 音频时间戳不正确");
                    }
                }
                codec.releaseOutputBuffer(index, false);
            }
            Log.e(TAG, "onOutputBufferAvailable: 音频flag=" + info.flags);
            Log.e(TAG, "onOutputBufferAvailable: 音频size=" + info.size);
            if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                mutexUtil.stop(true);
                codec.stop();
                codec.release();
            }
        }

        @Override
        public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {

        }

        @Override
        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
            mutexUtil.addTrack(format, true);
        }
    };
    private MediaFormat audioFormat;
    private MediaCodec audioCodec;
    private BlockingQueue<byte[]> audioQueue;
    private MutexUtil mutexUtil;

    public EncodeAudio(int sampleRate, int channelCount, MutexUtil mutexUtil) {
        this.mutexUtil = mutexUtil;

        HandlerThread handlerThread = new HandlerThread("AudioEncode");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        audioQueue = new LinkedBlockingQueue<>();

        audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount);
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, sampleRate * channelCount);
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 512 * 1024);
        try {
            audioCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioCodec.setCallback(callback, handler);
        audioCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    public void push(byte[] data) {
        try {
            audioQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (!isStart) {
            if (pts == 0) {
                pts = System.currentTimeMillis() * 1000L;
            }
            audioCodec.start();
            isStart = true;
        }
    }

    public void stop() {
//        if (isStart) {
//            if (audioCodec != null) {
//                audioCodec.stop();
//                audioCodec.release();
//                isStart = false;
//            }
//        }
        isStart = false;
    }
}
