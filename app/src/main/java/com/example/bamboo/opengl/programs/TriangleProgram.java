package com.example.bamboo.opengl.programs;

import android.content.Context;

import com.example.bamboo.R;

import static android.opengl.GLES20.glGetAttribLocation;

public class TriangleProgram extends ShaderProgram {
    private int aColor, aPosition;

    public TriangleProgram(Context context) {
        super(context, R.raw.select_tag_vertex_shade_choose, R.raw.select_tag_frag_shade_choose);
//        aColor = glGetAttribLocation(program, A_COLOR);
        aPosition = glGetAttribLocation(program, A_POSITION);
    }

    public int getAttributeColor() {
        return aColor;
    }

    public int getAttributePosition() {
        return aPosition;
    }

}
