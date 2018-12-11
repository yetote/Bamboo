package com.example.bamboo.myinterface.ffmpeg;

import com.example.bamboo.model.TimeInfoBean;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.myinterface.ffmpeg
 * @class 播放时间接口
 * @time 2018/12/11 10:32
 * @change
 * @chang time
 * @class describe
 */
public interface OnTimeInfoListener {
    /**
     * 音频时间信息接口
     * @param timeInfoBean 音频时间信息
     */
   void onTimeInfo(TimeInfoBean timeInfoBean);
}
