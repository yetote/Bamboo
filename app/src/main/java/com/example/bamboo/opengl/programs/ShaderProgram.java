package com.example.bamboo.opengl.programs;

import android.content.Context;

import com.example.bamboo.opengl.utils.ShaderHelper;
import com.example.bamboo.opengl.utils.TextRecourseReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    public static final String A_POSITION="a_Position";
    public static final String A_RADIUS="a_Radius";
    public static final String U_TEXTURE="u_Texture";
    public static final String U_SCALE="u_Scale";


    public final int program;

    public ShaderProgram(Context context, int vertexShaderRecourseId, int fragmentShaderRecourseId) {
        program = ShaderHelper.buildProgram(TextRecourseReader.readTextFileFromResource(context, vertexShaderRecourseId),
                TextRecourseReader.readTextFileFromResource(context, fragmentShaderRecourseId));

    }

    public void useProgram() {
        glUseProgram(program);
    }
}
