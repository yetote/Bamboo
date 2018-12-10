package com.example.bamboo.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

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

    /**
     * 多久之前的时间
     *
     * @param timeStamp 时间戳
     * @return 过去多长时间
     */
    public static String agoTime(long timeStamp) {
        long time = System.currentTimeMillis() - timeStamp;
        time /= 1000;
        if (time < 3 * DAY) {
            if (time < HOUR) {
                return time / MINUTE + "分钟之前";
            } else if (time < DAY) {
                return time / HOUR + "小时之前";
            } else if (time < 2 * DAY) {
                return "昨天";
            }
            return time / DAY + "天前";
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(timeStamp);
    }
}
