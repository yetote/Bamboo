package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.bamboo.opengl.SelectTagRenderer;

/**
 * @author yetote
 * @decription 选择标签
 */
public class SelectTagActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private SelectTagRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        init();
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(renderer);
    }

    private void init() {
        glSurfaceView = findViewById(R.id.select_tag_glSurfaceView);
        renderer = new SelectTagRenderer(this, glSurfaceView.getWidth(), glSurfaceView.getHeight());
    }
}
