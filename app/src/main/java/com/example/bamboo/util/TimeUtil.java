package com.example.bamboo.util;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/11/8 14:01
 * @change
 * @chang time
 * @class describe
 */
public class TimeUtil {
    private static final int MINUTE = 60;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    /**
     * 将秒数转化为时长
     *
     * @param seconds 秒数
     * @return 时长
     */
    public static String caseTime(int seconds) {
        if (seconds < HOUR) {
            return seconds / MINUTE + ":" + seconds % MINUTE;
        }
        return seconds / HOUR + ":" + (seconds % HOUR) / MINUTE + ":" + seconds % HOUR % MINUTE;
    }
}
