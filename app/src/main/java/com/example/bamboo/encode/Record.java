package com.example.bamboo.encode;

import android.content.Context;
import android.util.Size;
import android.view.Surface;

/**
 * @author yetote QQ:503779938
 * @name MyPlayer
 * @class name：com.example.myplayer.newencode
 * @class describe
 * @time 2019/6/17 10:44
 * @change
 * @chang time
 * @class describe
 */
public class Record {
    private Context context;
    private RecordAudio recordAudio;
    private RecordVideo recordVideo;
    private MutexUtil mutexUtil;
    int sampleRate, channelCount, displayWidth, displayHeight;

    public Record(Context context, int sampleRate, int channelCount, int displayWidth, int displayHeight, String path) {
        this.context = context;
        mutexUtil = new MutexUtil(path);
        this.sampleRate = sampleRate;
        this.channelCount = channelCount;
        this.displayWidth = displayWidth;
        this.displayHeight = displayHeight;
    }

    public void init() {
        recordAudio = new RecordAudio(sampleRate, channelCount, mutexUtil);
        recordVideo = new RecordVideo(context, displayWidth, displayHeight, mutexUtil);
    }

    public void openCamera(Surface surface) {
        recordVideo.openCamera(surface);
    }

    public void start(int orientation, Surface surface) {
        recordAudio.start();
        recordVideo.start(orientation, surface);

    }

    public void stop(Surface surface) {
        recordAudio.stop();
        recordVideo.stop(surface);
    }

    public Size getBestSize() {
        if (recordVideo != null) {
            return recordVideo.getBestSize();
        }
        return null;
    }
}
