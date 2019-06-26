package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class 封包util
 * @time 2019/6/26 16:46
 * @change
 * @chang time
 * @class describe
 */
public class MutexUtil {
    private MediaMuxer mediaMuxer;
    private int audioTrack, videoTrack;
    private boolean isStart;
    private boolean videoStop, audioStop;
    private static final String TAG = "MutexUtil";

    public MutexUtil(String path) {
        try {
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioTrack = videoTrack = -1;
    }

    public synchronized void addTrack(MediaFormat mediaFormat, boolean isAudio) {
        if (isAudio) {
            audioTrack = mediaMuxer.addTrack(mediaFormat);
        } else {
            videoTrack = mediaMuxer.addTrack(mediaFormat);
        }
        if (audioTrack != -1 && videoTrack != -1) {
            mediaMuxer.start();
            isStart = true;
        }
    }

    public synchronized void writeData(ByteBuffer buffer, MediaCodec.BufferInfo info, boolean isAudio) {
        if (!isStart) {
            Log.e(TAG, "writeData: 混音器未启动");
            return;
        }

        if (isAudio) {
            mediaMuxer.writeSampleData(audioTrack, buffer, info);
        } else {
            mediaMuxer.writeSampleData(videoTrack, buffer, info);
        }
    }

    public synchronized void stop() {
        if (isStart && mediaMuxer != null) {
            mediaMuxer.stop();
            mediaMuxer.release();
            isStart = false;
        }
    }
}
