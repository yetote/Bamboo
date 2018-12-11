package com.example.bamboo.myinterface.ffmpeg;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.ffmpeg
 * @class 播放暂停接口
 * @time 2018/12/11 10:30
 * @change
 * @chang time
 * @class describe
 */
public interface OnPauseListener {
    /**
     * 播放暂停接口
     *
     * @param isPause 是否暂停
     */
    void onPause(boolean isPause);
}
