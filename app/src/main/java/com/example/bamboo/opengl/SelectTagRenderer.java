package com.example.bamboo.opengl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.example.bamboo.R;
import com.example.bamboo.opengl.objects.UnSelectTag;
import com.example.bamboo.opengl.programs.UnSelectTagProgram;
import com.example.bamboo.opengl.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.opengl
 * @class 选择偏好-Renderer
 * @time 2018/11/13 14:33
 * @change
 * @chang time
 * @class describe
 */
public class SelectTagRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private UnSelectTag unSelectTag;
    private int width, height;
    private UnSelectTagProgram program;
    private int textureId;

    public SelectTagRenderer(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        unSelectTag = new UnSelectTag(10, width, height);
        program = new UnSelectTagProgram(context);
        textureId = TextureHelper.loadTexture(context, R.drawable.texture);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        program.useProgram();
        program.setUniform(textureId);
        unSelectTag.bindData(program);
        unSelectTag.draw();
    }
}
