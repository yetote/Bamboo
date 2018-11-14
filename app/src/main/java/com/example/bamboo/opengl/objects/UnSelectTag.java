package com.example.bamboo.opengl.objects;

import android.opengl.GLES30;

import com.example.bamboo.opengl.data.VertexArray;
import com.example.bamboo.opengl.programs.UnSelectTagProgram;
import com.example.bamboo.opengl.utils.RandomTagPoint;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;

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
    private float[] xArr, yArr;
    private VertexArray vertexArray;
    private int tagCount;
    private float[] vertexData;
    private float[] radiusArr = new float[]{
            0.1f, 0.2f, 0.3f, 0.4f, 0.5f,
            0.1f, 0.2f, 0.3f, 0.4f, 0.5f
    };
    public static final int POSITION_COMPONENT_COUNT = 2;
    public static final int RADIUS_COMPONENT_COUNT = 1;
    public static final int STRIDE = (POSITION_COMPONENT_COUNT + RADIUS_COMPONENT_COUNT) * 4;

    public UnSelectTag(int tagCount, int width, int height) {
        this.tagCount = tagCount;
        initVertex(width, height);
    }

    private void initVertex(int width, int height) {
        xArr = new float[tagCount];
        yArr = new float[tagCount];
        vertexData = new float[3 * tagCount];
        RandomTagPoint.pointX(xArr, tagCount);
        RandomTagPoint.pointY(yArr, tagCount);
        int j = 0;
        for (int i = 0; i < tagCount; i++) {
            vertexData[j++] = xArr[i];
            vertexData[j++] = yArr[i];
            vertexData[j++] = radiusArr[i]*1000;
        }
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(UnSelectTagProgram program) {
        vertexArray.setVertexAttributePointer(0, program.getAttrPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, program.getAttrRadiusLocation(), RADIUS_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 1);
        glDrawArrays(GL_POINTS, 1, 1);
        glDrawArrays(GL_POINTS, 2, 1);
        glDrawArrays(GL_POINTS, 3, 1);
        glDrawArrays(GL_POINTS, 4, 1);
        glDrawArrays(GL_POINTS, 5, 1);
        glDrawArrays(GL_POINTS, 6, 1);
        glDrawArrays(GL_POINTS, 7, 1);
        glDrawArrays(GL_POINTS, 8, 1);
    }
}
