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
public class PlayerView {
    static {
        System.loadLibrary("native-lib");

    }

    public native void play(String path,String vertexCode,String fragCode);
}
