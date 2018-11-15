package com.example.bamboo.opengl.objects;

import android.opengl.GLES30;
import android.util.Log;

import com.example.bamboo.opengl.data.VertexArray;
import com.example.bamboo.opengl.programs.UnSelectTagProgram;
import com.example.bamboo.opengl.utils.CoordinateTransformation;
import com.example.bamboo.opengl.utils.RandomTagPoint;

import java.util.Arrays;

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
    private float[] radiusArr = new float[]{
            0.1f, 0.2f, 0.3f, 0.4f, 0.5f,
            0.1f, 0.2f, 0.3f, 0.4f, 0.5f
    };
    private static final String TAG = "UnSelectTag";
    private int width;
    private int height;
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int RADIUS_COMPONENT_COUNT = 1;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + RADIUS_COMPONENT_COUNT) * 4;
    float[] tagRound;

    public UnSelectTag(float x, float y, float radius, int width, int height) {
//        this.tagCount = tagCount;
        this.width = width;
        this.height = height;
        initVertex(x, y, radius);
    }

    private void initVertex(float x, float y, float radius) {
        float[] vertexData = new float[3];
        vertexData[0] = x;
        vertexData[1] = y;
        vertexData[2] = radius * width;
        tagRound = new float[]{
                x - radius, y - radius,
                x + radius, y + radius
        };
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(UnSelectTagProgram program) {
        vertexArray.setVertexAttributePointer(0, program.getAttrPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, program.getAttrRadiusLocation(), RADIUS_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 1);
    }

    public boolean touchCheck(float x, float y) {
        float xTemp = CoordinateTransformation.AndroidToOpenGLx(x, width);
        float yTemp = CoordinateTransformation.AndroidToOpenGLy(y, height);
        Log.e(TAG, "xTemp" + xTemp + "\n" + "tTemp:" + yTemp);
        Log.e(TAG, "touchCheck: " + Arrays.toString(tagRound));
        return (xTemp >= tagRound[0] && xTemp <= tagRound[2]) && ((yTemp >= tagRound[1] && yTemp <= tagRound[3]));
    }

}
