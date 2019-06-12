package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/6/12 14:15
 * @change
 * @chang time
 * @class describe
 */
public class MutexUtil {
    private MediaMuxer mediaMuxer;
    int audioTrackIndex = -1, videoTrackIndex = -1;
    private boolean audioStop, videoStop;
    private boolean isStart, isAddVideoTrack;
    private static final String TAG = "MutexUtil";

    public MutexUtil(String path, MediaFormat videoFormat, MediaFormat audioFormat) {

        try {
            mediaMuxer = new MediaMuxer(path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        videoTrackIndex = mediaMuxer.addTrack(videoFormat);
//        audioTrackIndex = mediaMuxer.addTrack(audioFormat);
//        mediaMuxer.start();
    }

    public synchronized void pushData(ByteBuffer data, boolean isAudio, MediaCodec.BufferInfo bufferInfo) {
        int currentTrackIndex = isAudio ? audioTrackIndex : videoTrackIndex;
        mediaMuxer.writeSampleData(currentTrackIndex, data, bufferInfo);
    }

    public synchronized void stop(boolean isAudio) {
        if (isAudio) {
            audioStop = true;
        } else {
            videoStop = true;
        }
        if (audioStop && videoStop) {
            mediaMuxer.stop();
        }
    }

    public synchronized void addFormat(MediaFormat mediaFormat, boolean isAudio) {
        Log.e(TAG, "addFormat: isAudio" + isAudio);
        if (isAudio) {
            audioTrackIndex = mediaMuxer.addTrack(mediaFormat);
        } else {
            videoTrackIndex = mediaMuxer.addTrack(mediaFormat);
        }
        if (audioTrackIndex != -1 && videoTrackIndex != -1) {
            mediaMuxer.start();
            isStart = true;
        }
    }

    public boolean getState() {
        return isStart;
    }
}
