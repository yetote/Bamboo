package com.example.bamboo.opengl.objects;

import com.example.bamboo.opengl.data.VertexArray;
import com.example.bamboo.opengl.utils.RandomTagPoint;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.objects
 * @class describe
 * @time 2018/11/13 18:32
 * @change
 * @chang time
 * @class describe
 */
public class UnSelectTag {
    private int[] xArr, yArr;
    private VertexArray vertexArray;
    private int tagCount;
    private float[] vertexData;

    public UnSelectTag(int tagCount, int width, int height) {
        this.tagCount = tagCount;
        initVertex(width, height);
    }

    void initVertex(int width, int height) {
        xArr = new int[tagCount];
        yArr = new int[tagCount];
        vertexData = new float[5 * tagCount];
        RandomTagPoint.pointX(xArr, tagCount, width);
        RandomTagPoint.pointX(yArr, tagCount, height);
        for (int i = 0; i < tagCount; i++) {
            vertexData[i]=xArr[i];
            vertexData[i+1]=yArr[i];
            vertexData[i+2]=yArr[i];
            vertexData[i+3]=yArr[i];
            vertexData[i+4]=yArr[i];

        }
    }
}
