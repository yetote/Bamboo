package com.example.bamboo.opengl.utils;

import java.util.Random;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.utils
 * @class describe
 * @time 2018/11/13 14:38
 * @change
 * @chang time
 * @class describe
 */
public class RandomTagPoint {
    /**
     * 生成随机点的X坐标
     *
     * @param xArr   x坐标数组
     * @param count  要生成的个数
     * @param around 取值范围
     */
    public static void pointX(int[] xArr, int count, int around) {
        xArr[0] = Math.abs((int) randomNum() * around);
        for (int i = 1; i < count; i++) {
            xArr[i] = (int) Math.abs(randomNum() * around);
            if (Math.abs(xArr[i] - xArr[i - 1]) < 50) {
                i--;
            }
        }
    }

    /**
     * 生成随机点的Y坐标
     *
     * @param yArr  y坐标数组
     * @param count 要生成的个数
     */
    public static void pointY(int[] yArr, int count) {
//        yArr[0] = Math.abs((int) randomNum() * around);
//        for (int i = 1; i < count; i++) {
//            yArr[i] = (int) Math.abs(randomNum() * around);
//            if (Math.abs(yArr[i] - yArr[i - 1]) < 50) {
//                i--;
//            }
//        }
    }

    public static float randomNum() {
        return new Random(System.currentTimeMillis()).nextFloat() * 2f - 1.0f;
    }

    public static float randomRadius(float[] radiusArr, int count) {
        radiusArr[0] = Math.abs(randomNum());
        for (int i = 1; i < count; i++) {
            if (Math.abs(radiusArr[i] - radiusArr[i - 1]) < 0.1) {
                i--;
            }
        }
        return new Random(System.currentTimeMillis()).nextFloat();

    }
}
