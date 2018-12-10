package com.example.bamboo.myview;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import com.example.bamboo.myinterface.ffmpeg.OnPreparedListener;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 播放器
 * @time 2018/10/20 14:02
 * @change
 * @chang time
 * @class describe
 */
public class PlayerView {
    static {
        System.loadLibrary("native-lib");
    }

    private OnPreparedListener preparedListener;

    public void setPreparedListener(OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void prepared(String source) {
        new Thread(() -> ffmpegPrepared(source)).start();
    }

    void onPreparedCall() {
        if (preparedListener != null) {
            preparedListener.onPrepared();
        }
    }

    private native void ffmpegPrepared(String source);
}
