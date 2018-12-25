package com.example.bamboo.util;

/**
 * @author yetote QQ:503779938
 * @name OpenGLPlay
 * @class name：com.example.openglplay.utils
 * @class describe
 * @time 2018/9/18 14:27
 * @change
 * @chang time
 * @class describe
 */
public class MathUtil {
    private static final boolean EVEN = true;
    private static final boolean ODD = false;

    static boolean powerOfTwo(int... arr) {
        first:
        for (int anArr : arr) {
            for (int j = 0; j <= 11; j++) {
                if (2 << j == anArr) {
                    continue first;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean even(int... arr) {
        for (int anArr : arr) {
            if (anArr % 2 != 0) {
                return ODD;
            }
        }
        return EVEN;
    }

    /**
     * 未读的消息数
     *
     * @param num 消息数量
     * @return 经过处理的消息数量
     */
    public static String msgNum(int num) {
        if (num > 99) {
            return "99+";
        }
        return num + "";
    }

}
