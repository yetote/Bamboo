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
//        if (currentTrackIndex==audioTrackIndex){
//            Log.e(TAG, "pushData: 接受到的pts"+bufferInfo.presentationTimeUs );
//        }
        mediaMuxer.writeSampleData(currentTrackIndex, data, bufferInfo);
    }

    public synchronized void stop(boolean isAudio) {
        if (isAudio) {
            Log.e(TAG, "stop: 音频结束");
            audioStop = true;
        } else {
            Log.e(TAG, "stop: 视频结束");
            videoStop = true;
        }
        if (audioStop && videoStop) {
            Log.e(TAG, "stop: 封装完成");
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
