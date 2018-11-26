package com.example.bamboo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.bamboo.R;
import com.example.bamboo.opengl.objects.SelectTag;
import com.example.bamboo.opengl.objects.TagImpl;
import com.example.bamboo.opengl.programs.UnSelectTagProgram;
import com.example.bamboo.opengl.utils.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static com.example.bamboo.util.CoordinateTransformation.SIDE_TYPE.SIDE_HEIGHT;
import static com.example.bamboo.util.CoordinateTransformation.dpHeight;
import static com.example.bamboo.util.CoordinateTransformation.dpWidth;
import static com.example.bamboo.util.CoordinateTransformation.pxToDp;
import static com.example.bamboo.util.CoordinateTransformation.relativeAndroidToOpenGL;
import static com.example.bamboo.util.CoordinateTransformation.touchInGlSurfaceView;

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
    private SelectTag[] selectTag;
    private UnSelectTagProgram[] program;
    private int[] textureIds;
    private int[] selectTextureIds;
    private TagImpl tagImpl;
    private float[][] modelMatrixArr = new float[15][16];
    private float scale = 1.2f;
    private float[] radiusArr = new float[]{
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f,
            0.25f, 0.25f, 0.25f
    };
    private float[] xArr = new float[]{
            //    @formatter:off
            -0.0f, 0.0f,  0.6f,
            -0.45f, 0.5f,  0.2f,
            -0.35f, 0.2f,  0.65f,
            -0.7f,  0.5f,  0.6f,
            -0.45f, 0.5f, -0.35f
            //    @formatter:on

    };
    private float[] yArr = new float[]{
            //    @formatter:off
             0.0f,  0.6f,  0.7f,
             0.2f,  0.2f, -0.7f,
            -0.4f, -0.2f, -0.3f,
            -0.7f, -0.7f,  0.6f,
            -0.2f, -0.3f, -0.4f
            //    @formatter:on
    };


    public SelectTagRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        textureIds = new int[15];
        selectTextureIds = new int[15];
        selectTag = new SelectTag[15];
        program = new UnSelectTagProgram[15];
        tagImpl = new TagImpl(selectTag);
        init(15, context);
        tagImpl.onLayout();
    }

    private void init(int count, Context context) {
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
                R.drawable.texture9,
                R.drawable.texture6,
                R.drawable.texture5,
                R.drawable.texture9,
                R.drawable.texture2,
                R.drawable.texture
        };
        int[] selectDrawableArr = new int[]{
                R.drawable.dianying,
                R.drawable.dongman,
                R.drawable.fengjing,
                R.drawable.gaoxiao,
                R.drawable.huwai,
                R.drawable.keji,
                R.drawable.mingxing,
                R.drawable.xinwen,
                R.drawable.yingyue,
                R.drawable.youxi,
                R.drawable.gaoxiao,
                R.drawable.huwai,
                R.drawable.keji,
                R.drawable.mingxing,
                R.drawable.xinwen
        };
        for (int i = 0; i < count; i++) {
            selectTag[i] = new SelectTag(xArr[i], yArr[i], radiusArr[i]);
            program[i] = new UnSelectTagProgram(context);
            textureIds[i] = TextureHelper.loadTexture(context, drawableArr[i]);
            selectTextureIds[i] = TextureHelper.loadTexture(context, selectDrawableArr[i]);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        for (float[] aModelMatrixArr : modelMatrixArr) {
            setIdentityM(aModelMatrixArr, 0);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        tagImpl.onDraw();
        for (int i = 0; i < selectTag.length; i++) {
            program[i].useProgram();
            translateM(modelMatrixArr[i],
                    0,
                    relativeAndroidToOpenGL(selectTag[i].getX(), dpWidth),
                    relativeAndroidToOpenGL(-selectTag[i].getY(), dpHeight),
                    0);
            if (selectTag[i].getIsSelect()) {
                program[i].setUniform(selectTextureIds[i], scale, modelMatrixArr[i]);
            } else {
                program[i].setUniform(textureIds[i], 1.0f, modelMatrixArr[i]);
            }
            selectTag[i].bindData(program[i]);
            selectTag[i].draw();
        }
    }

    public void tagClick(float x, float y) {
        Log.e(TAG, "tagClick: x     " + x + "\n" + "y         " + y);
        for (int i = 0; i < selectTag.length; i++) {
            if (selectTag[i].isInCircle(x, touchInGlSurfaceView(y))) {
                selectTag[i].setSelect(!(selectTag[i].getIsSelect()));
                tagImpl.onChanged(i, scale);
                break;
            }
        }
    }
}
