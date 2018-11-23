package com.example.bamboo.opengl.utils;

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
    private static float openGLToAndroid(float coordinate, int width) {
        return 0;
    }

    public static float androidToOpenGLx(float coordinate, int width) {
        return (coordinate - width / 2) / (width / 2);
    }

    public static float androidToOpenGLy(float coordinate, int height) {
        return (height / 2 - coordinate) / (height / 2);
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

    public static float openGLToAndroidX(float x, int width)  {
        return x * width / 2 + width / 2;
    }

    public static float openGLToAndroidY(float y, int height) {
        return height / 2 - y * height / 2;
    }
}
