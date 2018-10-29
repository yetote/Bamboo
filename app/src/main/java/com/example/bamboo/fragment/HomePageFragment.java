package com.example.bamboo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bamboo.R;
import com.example.bamboo.util.PlayerView;
import com.example.bamboo.util.TextRecourseReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author yetote QQ:503779938
 * @name HomePageFragment
 * @class name：com.example.bamboo.fragment
 * @class 首页
 * @time 2018/10/19 16:58
 * @change
 * @chang time
 * @class describe
 */
public class HomePageFragment extends Fragment {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button startBtn;
    PlayerView playerView;
    private static final String TAG = "HomePageFragment";
    private boolean isPlaying = false;
    private String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_homepage, null, false);

        initView(v);

        startBtn.setOnClickListener(v1 -> {
            if (isPlaying) {
                startBtn.setBackgroundResource(R.mipmap.play);
            } else {
                String vertexCode = TextRecourseReader.readTextFileFromResource(getActivity(), R.raw.yuv_vertex_shader);
                String fragCode = TextRecourseReader.readTextFileFromResource(getActivity(), R.raw.yuv_frag_shader);
                playerView.play(path, vertexCode, fragCode);
                startBtn.setBackgroundResource(R.mipmap.pause);
            }
            isPlaying = !isPlaying;
        });

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "surfaceChanged: " + path);


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        return v;
    }

    private void initView(View v) {
        surfaceView = v.findViewById(R.id.homePager_surfaceView);
        startBtn = v.findViewById(R.id.homePager_start_btn);
        playerView = new PlayerView();
        path = getActivity().getExternalCacheDir().getPath() + "/test.mp4";
    }
}
