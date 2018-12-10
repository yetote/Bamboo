package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bamboo.R;
import com.example.bamboo.RecodeVideoActivity;
import com.example.bamboo.myinterface.ffmpeg.OnPreparedListener;
import com.example.bamboo.opengl.utils.TextRecourseReader;
import com.example.bamboo.myview.PlayerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    Button startBtn, decode;
    PlayerView playerView;
    private static final String TAG = "HomePageFragment";
    private boolean isPlaying = false;
    private String path;
    int w;
    int h;
    private String outPath;
    private FloatingActionButton recodeBtn;
    private String networkSource;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_homepage, null, false);

        initView(v);

        onClick();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e(TAG, "surfaceChanged: " + path);
                surfaceHolder = holder;
                w = width;
                h = height;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        ffmpegCallBack();

        return v;
    }

    private void ffmpegCallBack() {
        playerView.setPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.e(TAG, "onPrepared: ffmpeg准备好了");
            }
        });
    }

    private void onClick() {
        startBtn.setOnClickListener(vStart -> {
            if (isPlaying) {
                startBtn.setBackgroundResource(R.mipmap.play);
            } else {
                startBtn.setBackgroundResource(R.mipmap.pause);
                playerView.prepared(networkSource);
            }
            isPlaying = !isPlaying;
        });
        recodeBtn.setOnClickListener(vRecode -> {
            Intent i = new Intent();
            i.setClass(getActivity(), RecodeVideoActivity.class);
            startActivity(i);
        });
    }

    private void initView(View v) {
        surfaceView = v.findViewById(R.id.homePager_surfaceView);
        startBtn = v.findViewById(R.id.homePager_start_btn);
        playerView = new PlayerView();
        decode = v.findViewById(R.id.decode);
        path = getActivity().getExternalCacheDir().getPath() + "/res/sample.mp3";
        outPath = getActivity().getExternalCacheDir().getPath() + "/res/test.pcm";
        networkSource = "http://wsaudio.bssdlbig.kugou.com/1812101617/soWMwbaRaxJwrs0mJZXyVQ/1544516232/bss/extname/wsaudio/1b9622ef73c66de3e13c789619a677c4.mp3";
        recodeBtn = v.findViewById(R.id.homePager_recode_video);
    }
}
