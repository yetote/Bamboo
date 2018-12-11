package com.example.bamboo.myinterface.ffmpeg;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.ffmpeg
 * @class 缓冲回调接口
 * @time 2018/12/11 10:56
 * @change
 * @chang time
 * @class describe
 */
public interface OnLoadListener {
    /**
     * 是否在加载中
     *
     * @param isLoad 加载中
     */
    void onLoad(boolean isLoad);
}
