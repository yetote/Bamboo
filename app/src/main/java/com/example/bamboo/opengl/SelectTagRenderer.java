package com.example.bamboo.opengl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.widget.Toast;

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
    private static final String TAG = "SelectTagRenderer";
    private Context context;
    private UnSelectTag[] unSelectTag;
    private int width, height;
    private UnSelectTagProgram[] program;
    private int[] textureIds;
    private float[] radiusArr = new float[]{
            0.3f, 0.2f, 0.2f,
            0.4f, 0.4f,
            0.4f, 0.2f, 0.2f,
            0.2f, 0.3f
    };
    private float[] xArr = new float[]{
            -0.65f, 0.02f, 0.6f,
            -0.45f, 0.5f,
            -0.35f, 0.2f, 0.65f,
            -0.8f, 0.5f
    };
    private float[] yArr = new float[]{
            0.75f, 0.6f, 0.7f,
            0.2f, 0.15f,
            -0.5f, -0.2f, -0.3f,
            -0.8f, -0.7f
    };


    public SelectTagRenderer(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
        Log.e(TAG, "SelectTagRenderer: " + width);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        textureIds = new int[10];
        unSelectTag = new UnSelectTag[10];
        program = new UnSelectTagProgram[10];
        int i = 0;
        init(10, width, height, context);
    }

    private void init(int count, int width, int height, Context context) {
        int[] drawableArr = new int[]{
                R.drawable.texture,
                R.drawable.texture1,
                R.drawable.texture2,
                R.drawable.texture3,
                R.drawable.texture4,
                R.drawable.texture5,
                R.drawable.texture6,
                R.drawable.texture7,
                R.drawable.texture8,
                R.drawable.texture9
        };
        for (int i = 0; i < count; i++) {
            unSelectTag[i] = new UnSelectTag(xArr[i], yArr[i], radiusArr[i], width, height);
            program[i] = new UnSelectTagProgram(context);
            textureIds[i] = TextureHelper.loadTexture(context, drawableArr[i]);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        for (int i = 0; i < 10; i++) {
            program[i].useProgram();
            program[i].setUniform(textureIds[i]);
            unSelectTag[i].bindData(program[i]);
            unSelectTag[i].draw();
        }
    }

    public void tagClick(float x, float y) {
        for (int i = 0; i < 10; i++) {
            if (unSelectTag[i].touchCheck(x, y)) {
                Log.e(TAG, "tagClick: " + i);
            }
        }
    }

}
