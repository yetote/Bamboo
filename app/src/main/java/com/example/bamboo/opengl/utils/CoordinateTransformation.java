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
    private static float OpenGLToAndroid(float coordinate, int width) {
        return 0;
    }

    public static float AndroidToOpenGLx(float coordinate, int width) {

        return (coordinate - width / 2) / (width / 2);
    }
    public static float AndroidToOpenGLy(float coordinate, int height) {

        return ( height / 2-coordinate) / (height / 2);
    }
}
