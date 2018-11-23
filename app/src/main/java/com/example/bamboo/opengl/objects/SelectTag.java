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
public class SelectTag {
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
    private float[] vertexData;
    private boolean isSelect = false;

    Object tag;
    float x, y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public SelectTag(float x, float y, float radius, int width, int height) {
        this.width = width;
        this.height = height;
        initVertex(x, y, radius);
    }

    private void initVertex(float x, float y, float radius) {
        vertexData = new float[3];
        vertexData[0] = x;
        vertexData[1] = y;
        vertexData[2] = radius * 1080;
        tagRound = new float[]{
                x - radius, y - radius,
                x + radius, y + radius
        };
        vertexArray = new VertexArray(vertexData);
    }

    public void bindData(UnSelectTagProgram program) {
        vertexArray.setVertexAttributePointer(0, program.getAttrPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, program.getAttrRadiusLocation(), RADIUS_COMPONENT_COUNT, STRIDE);
//        Log.e(TAG, "bindData: " + Arrays.toString(vertexData));
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 1);
    }

    public void touch(float x, float y, UnSelectTagProgram program) {
        if (isInCircle(x, y)) {
            vertexData[2] += width / 10;
            vertexArray.setVertexAttributePointer(0, program.getAttrPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
            vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, program.getAttrRadiusLocation(), RADIUS_COMPONENT_COUNT, STRIDE);
        }
    }

    public boolean isInCircle(float x, float y) {
        // TODO: 2018/11/15 判断仍有bug
        float xTemp = CoordinateTransformation.androidToOpenGLx(x, width);
        float yTemp = CoordinateTransformation.androidToOpenGLy(y, height);
        float distance = (float) Math.sqrt((xTemp - vertexData[0]) * (xTemp - vertexData[0]) + (yTemp - vertexData[1]) * (yTemp - vertexData[1]));
//        return (distance < vertexData[2] / width) && (distance < vertexData[2] / height);
        return distance < vertexData[2] / 1080;
    }

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public float getRadius() {
        return vertexData[2];
    }

    public float[] getVertexData() {
        return vertexData;
    }
}
