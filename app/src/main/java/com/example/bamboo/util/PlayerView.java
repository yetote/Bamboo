package com.example.bamboo.util;

import android.os.HandlerThread;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/10/20 14:02
 * @change
 * @chang time
 * @class describe
 */
public class PlayerView extends HandlerThread {
    public PlayerView(String name) {
        super("recommendThread");
    }

    @Override
    public synchronized void start() {
        super.start();
        createEGLContext();
    }

    public void replace() {
        destroyEGLContext();
    }

    public native void createEGLContext();

    public native void destroyEGLContext();
}
