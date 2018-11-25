package com.example.bamboo.util;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.utils
 * @class 坐标系转换
 * @time 2018/11/15 11:41
 * @change
 * @chang time
 * @class describe
 */
public class CoordinateTransformation {
    /**
     * 屏幕的边的种类，宽度还是高度
     */
    public enum SIDE_TYPE {
        /**
         * 宽度
         */
        SIDE_WIDTH,
        /**
         * 高度
         */
        SIDE_HEIGHT
    }

    /**
     * 屏幕宽高 (dp)
     */
    public static int dpWidth, dpHeight;
    /**
     * 屏幕宽高 (px)
     */
    public static int pxWidth, pxHeight;

    /**
     * 设置屏幕尺寸
     *
     * @param pxw 宽 px
     * @param pxh 高 px
     * @param dpw 宽 dp
     * @param dph 高 dp
     */
    public static final void initDisplay(int pxw, int pxh, int dpw, int dph) {
        dpWidth = 1080;
        dpHeight = 1800;
        pxWidth = 1680;
        pxHeight = 2040;
    }

    private static float openGLToAndroid(float coordinate, int width) {
        return 0;
    }

    public static float androidToOpenGL(float coordinate, SIDE_TYPE sideType, int ruler) {
        if (sideType == SIDE_TYPE.SIDE_WIDTH) {
            return (coordinate - ruler / 2) / (ruler / 2);
        }
        return (ruler / 2 - coordinate) / (ruler / 2);
    }

    /**
     * android转opengl相对坐标
     *
     * @param distance 距离
     * @param ruler    标尺 x轴为宽度，y轴为高度
     * @return 相对位置
     */
    public static float relativeAndroidToOpenGL(float distance, float ruler) {
        return distance / ruler * 2;
    }

    /**
     * openGL坐标转Android坐标
     *
     * @param coordinate openGL坐标
     * @param sideType   边的种类
     * @param ruler      边的长度
     * @return android坐标
     */
    public static float openGLToAndroid(float coordinate, SIDE_TYPE sideType, int ruler) {
        if (sideType == SIDE_TYPE.SIDE_WIDTH) {
            return coordinate * ruler / 2 + ruler / 2;
        }
        return ruler / 2 - coordinate * ruler / 2;
    }

    /**
     * px转dp
     *
     * @param px   输入的px数据
     * @param type 边的种类
     * @return 返回的dp数据
     */
    public static float pxToDp(float px, SIDE_TYPE type) {
        if (type == SIDE_TYPE.SIDE_WIDTH) {
            return px * ((float) dpWidth / (float) pxWidth);
        }
        return px * ((float) dpHeight / (float) pxHeight);
    }

    /**
     * dp转px
     *
     * @param dp   输入的dp数据
     * @param type 边的种类
     * @return 返回的px数据
     */
    public static float dpToPX(float dp, SIDE_TYPE type) {
        if (type == SIDE_TYPE.SIDE_WIDTH) {
            return dp * ((float) pxWidth / (float) dpWidth);
        }
        return dp * ((float) pxHeight / (float) dpHeight);
    }
}
