package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bamboo.R;
import com.example.bamboo.RecodeVideoActivity;
import com.example.bamboo.model.TimeInfoBean;
import com.example.bamboo.myinterface.ffmpeg.OnLoadListener;
import com.example.bamboo.myinterface.ffmpeg.OnPauseListener;
import com.example.bamboo.myinterface.ffmpeg.OnPreparedListener;
import com.example.bamboo.myinterface.ffmpeg.OnStartListener;
import com.example.bamboo.myinterface.ffmpeg.OnStopListener;
import com.example.bamboo.myinterface.ffmpeg.OnTimeInfoListener;
import com.example.bamboo.opengl.utils.TextRecourseReader;
import com.example.bamboo.myview.PlayerView;
import com.example.bamboo.util.TimeUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;

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
    int w;
    int h;
    private String outPath;
    private FloatingActionButton recodeBtn;
    private String networkSource;
    public static final int MSG_AV_TIME_INFO_WHAT = 1;
    private boolean isSetTime = false;
    private TextView currentTime, totalTime;
    private SeekBar seekBar;

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

        playerView.prepared(networkSource);

        onCall();


        return v;
    }

    private void onCall() {
        playerView.setPreparedListener(() -> {
            Log.e(TAG, "onPrepared: 准备好了，开始播放");
            playerView.start();
            isPlaying = true;
        });

        playerView.setLoadListener(isLoad -> Log.e(TAG, "onLoad: 加载中,请稍等"));
        playerView.setPauseListener(new OnPauseListener() {
            @Override
            public void onPause(boolean isPause) {
                if (isPause) {
                    Log.e(TAG, "onPause: 暂停中");
                } else {
                    Log.e(TAG, "onPause: 继续播放");
                }
            }
        });
        playerView.setStartListener(new OnStartListener() {
            @Override
            public void onStart() {
                Log.e(TAG, "onStart: 开始播放");
            }
        });
        playerView.setStopListener(new OnStopListener() {
            @Override
            public void onStop() {
                Log.e(TAG, "onStop: 停止播放");
            }
        });
        playerView.setTimeInfoListener(new OnTimeInfoListener() {
            @Override
            public void onTimeInfo(TimeInfoBean timeInfoBean) {
                Message msg = new Message();
                msg.what = MSG_AV_TIME_INFO_WHAT;
                msg.obj = timeInfoBean;
                handler.sendMessage(msg);
            }
        });
    }


    private void onClick() {

        startBtn.setOnClickListener(vStart -> {
            if (isPlaying) {
                startBtn.setBackgroundResource(R.mipmap.play);
                playerView.pause();
            } else {
                startBtn.setBackgroundResource(R.mipmap.pause);
                playerView.onResume();
            }
            isPlaying = !isPlaying;
        });

        recodeBtn.setOnClickListener(vRecode -> {
            Intent i = new Intent();
            i.setClass(getActivity(), RecodeVideoActivity.class);
            startActivity(i);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
                playerView.seek(seekBar.getProgress());
            }
        });
    }

    private void initView(View v) {
        surfaceView = v.findViewById(R.id.homePager_surfaceView);
        startBtn = v.findViewById(R.id.homePager_start_btn);
        playerView = new PlayerView();
        path = getActivity().getExternalCacheDir().getPath() + "/res/sample.mp3";
        outPath = getActivity().getExternalCacheDir().getPath() + "/res/test.pcm";
        networkSource = "http://wsaudio.bssdlbig.kugou.com/1812101617/soWMwbaRaxJwrs0mJZXyVQ/1544516232/bss/extname/wsaudio/1b9622ef73c66de3e13c789619a677c4.mp3";
        recodeBtn = v.findViewById(R.id.homePager_recode_video);
        currentTime = v.findViewById(R.id.homePager_currentTime_tv);
        totalTime = v.findViewById(R.id.homePager_totalTime_tv);
        seekBar = v.findViewById(R.id.homePager_seekBar);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_AV_TIME_INFO_WHAT:
                    TimeInfoBean bean = (TimeInfoBean) msg.obj;
                    if (!isSetTime) {
                        totalTime.setText(TimeUtil.caseTime(bean.getTotalTime()));
                        seekBar.setMax(bean.getTotalTime());
                    }
                    currentTime.setText(TimeUtil.caseTime(bean.getCurrentTime()));
                    seekBar.setProgress(bean.getCurrentTime());
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        playerView.pause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        playerView.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        playerView.stop();
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isPlaying) {
            playerView.pause();
            startBtn.setBackgroundResource(R.mipmap.play);
            isPlaying = false;
            Log.e(TAG, "setUserVisibleHint: ");
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}

