package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.example.bamboo.util.PlayerView;
import com.example.bamboo.util.TextRecourseReader;

public class TestActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btn;
    PlayerView playerView;
    int w, h;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder = holder;
                w = width;
                h = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        String vertexCode = TextRecourseReader.readTextFileFromResource(this, R.raw.yuv_vertex_shader);
        String fragCode = TextRecourseReader.readTextFileFromResource(this, R.raw.yuv_frag_shader);

        btn.setOnClickListener(v -> playerView.play(path, vertexCode, fragCode, surfaceHolder.getSurface(), w, h));
    }

    private void initView() {
        surfaceView = findViewById(R.id.test_surfaceView);
        btn = findViewById(R.id.test_btn);
        playerView = new PlayerView();
        path = this.getExternalCacheDir().getPath() + "/test.mp4";
    }
}
