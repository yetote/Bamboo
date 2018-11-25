package com.example.bamboo.opengl.objects;

import com.example.bamboo.opengl.data.VertexArray;
import com.example.bamboo.opengl.programs.UnSelectTagProgram;
import com.example.bamboo.util.CoordinateTransformation;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.glDrawArrays;
import static com.example.bamboo.util.CoordinateTransformation.SIDE_TYPE.SIDE_WIDTH;
import static com.example.bamboo.util.CoordinateTransformation.dpToPX;
import static com.example.bamboo.util.CoordinateTransformation.dpWidth;
import static com.example.bamboo.util.CoordinateTransformation.pxWidth;

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
    private VertexArray vertexArray;
    private static final String TAG = "UnSelectTag";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int RADIUS_COMPONENT_COUNT = 1;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + RADIUS_COMPONENT_COUNT) * 4;
    float[] tagRound;
    private float[] vertexData;
    private boolean isSelect = false;
    private float[] center;
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

    public SelectTag(float x, float y, float radius) {
        initVertex(x, y, radius);
    }

    private void initVertex(float x, float y, float radius) {
        vertexData = new float[3];
        vertexData[0] = x;
        vertexData[1] = y;
        vertexData[2] = radius * dpWidth;
        tagRound = new float[]{
                x - radius, y - radius,
                x + radius, y + radius
        };
        vertexArray = new VertexArray(vertexData);
        center = new float[]{vertexData[0], vertexData[1]};
    }

    public void bindData(UnSelectTagProgram program) {
        vertexArray.setVertexAttributePointer(0, program.getAttrPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT, program.getAttrRadiusLocation(), RADIUS_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 1);
    }


    public boolean isInCircle(float x, float y) {
        float distance = (float) Math.sqrt((x - center[0]) * (x - center[0]) + (y - center[1]) * (y - center[1]));
        return distance < dpToPX(vertexData[2], SIDE_WIDTH) / 2;
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

    public float[] getCenter() {
        return center;
    }

    public void setCenter(float x, float y) {
        center[0] = x;
        center[1] = y;
    }
}
