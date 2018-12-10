package com.example.bamboo.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name��com.example.bamboo.util
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
     * ������ת��Ϊʱ��
     *
     * @param seconds ����
     * @return ʱ��
     */
    public static String caseTime(int seconds) {
        if (seconds < HOUR) {
            return seconds / MINUTE + ":" + seconds % MINUTE;
        }
        return seconds / HOUR + ":" + (seconds % HOUR) / MINUTE + ":" + seconds % HOUR % MINUTE;
    }

    /**
     * ���֮ǰ��ʱ��
     *
     * @param timeStamp ʱ���
     * @return ��ȥ�೤ʱ��
     */
    public static String agoTime(long timeStamp) {
        long time = System.currentTimeMillis() - timeStamp;
        time /= 1000;
        if (time < 3 * DAY) {
            if (time < HOUR) {
                return time / MINUTE + "����֮ǰ";
            } else if (time < DAY) {
                return time / HOUR + "Сʱ֮ǰ";
            } else if (time < 2 * DAY) {
                return "����";
            }
            return time / DAY + "��ǰ";
        }
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(timeStamp);
    }
}
