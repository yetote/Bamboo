package com.example.bamboo.opengl.programs;

import android.content.Context;
import android.icu.lang.UScript;
import android.opengl.GLES30;

import com.example.bamboo.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl.programs
 * @class describe
 * @time 2018/11/14 16:29
 * @change
 * @chang time
 * @class describe
 */
public class UnSelectTagProgram extends ShaderProgram {
    private int aPosition, aRadius;
    private int uTexture;
    private int uScale;
    private int uMatrix;

    public UnSelectTagProgram(Context context) {
        super(context, R.raw.select_tag_vertex_shade_unchoose, R.raw.select_tag_frag_shade_unchoose);
        aPosition = glGetAttribLocation(program, A_POSITION);
        aRadius = glGetAttribLocation(program, A_RADIUS);
        uTexture = glGetUniformLocation(program, U_TEXTURE);
        uScale = glGetUniformLocation(program, U_SCALE);
        uMatrix = glGetUniformLocation(program, U_MATRIX);
    }

    public int getAttrPositionLocation() {
        return aPosition;
    }

    public int getAttrRadiusLocation() {
        return aRadius;
    }

    public void setUniform(int textureId, float scale, float[] matrix) {
        glUniform1f(uScale, scale);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(uTexture, 0);
        glUniformMatrix4fv(uMatrix, 1, false, matrix, 0);
    }
}
