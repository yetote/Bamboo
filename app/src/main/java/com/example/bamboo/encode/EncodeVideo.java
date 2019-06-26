package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import androidx.annotation.NonNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR;

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
public class EncodeVideo {
    private MutexUtil mutexUtil;
    private int recordHeight, recordWight;
    private MediaCodec videoCodec;
    private MediaFormat videoFormat;
    private BlockingQueue<byte[]> videoQueue;
    private MediaCodec.Callback callback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {

        }

        @Override
        public void onOutputBufferAvailable(@NonNull MediaCodec codec, int index, @NonNull MediaCodec.BufferInfo info) {

        }

        @Override
        public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {

        }

        @Override
        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {

        }
    };

    public EncodeVideo(int recordWidth, int recordHeight) {
        this.recordHeight = recordHeight;
        this.recordWight = recordWidth;
        videoQueue = new LinkedBlockingQueue<>();
        videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, recordWidth, recordHeight);
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, recordWidth * recordHeight * 30 * 3);
        videoFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, BITRATE_MODE_VBR);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
    }
}
