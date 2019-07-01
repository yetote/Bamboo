package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;

import com.zhihu.matisse.MimeType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import internal.org.apache.http.entity.mime.MIME;

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
    private static final String TAG = "EncodeVideo";
    private MutexUtil mutexUtil;
    private int recordHeight, recordWight;
    private MediaCodec videoCodec;
    private MediaFormat videoFormat;
    private BlockingQueue<byte[]> videoQueue;
    private boolean isStart;
    long pts = 0;
    private MediaCodec.Callback callback = new MediaCodec.Callback() {
        @Override
        public void onInputBufferAvailable(@NonNull MediaCodec codec, int index) {
            if (index >= 0) {
                ByteBuffer byteBuffer = codec.getInputBuffer(index);
                if (byteBuffer != null) {
                    try {
                        byte[] data = videoQueue.take();
                        int flag = 0;
                        if (videoQueue.isEmpty() && !isStart) {
                            Log.e(TAG, "onInputBufferAvailable: 视频最后一帧");
                            flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                        }
                        byteBuffer.clear();
                        byteBuffer.put(data);
                        codec.queueInputBuffer(index, 0, data.length, System.currentTimeMillis()*1000L-pts, flag);
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
                mutexUtil.writeData(buffer, info, false);
            }
            codec.releaseOutputBuffer(index, false);
            if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                mutexUtil.stop(false);
                codec.stop();
                codec.release();
            }
        }

        @Override
        public void onError(@NonNull MediaCodec codec, @NonNull MediaCodec.CodecException e) {

        }

        @Override
        public void onOutputFormatChanged(@NonNull MediaCodec codec, @NonNull MediaFormat format) {
            mutexUtil.addTrack(format, false);
        }
    };

    public EncodeVideo(int recordWidth, int recordHeight, MutexUtil mutexUtil) {
        this.recordHeight = recordHeight;
        this.recordWight = recordWidth;
        this.mutexUtil = mutexUtil;
        videoQueue = new LinkedBlockingQueue<>();
        HandlerThread handlerThread = new HandlerThread("videoEncode");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, recordWidth, recordHeight);
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, recordWidth * recordHeight * 30 * 3);
        videoFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, BITRATE_MODE_VBR);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        try {
            videoCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoCodec.setCallback(callback, handler);
        videoCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    public void pushData(byte[] data) {
        try {
            videoQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (videoCodec != null) {
            if (pts == 0) {
                pts = System.currentTimeMillis() * 1000L;
            }
            videoCodec.start();
            isStart = true;
        }
    }

    public void stop() {
        isStart = false;
    }
}
