package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bamboo.R;
import com.example.bamboo.RecodeVideoActivity;
import com.example.bamboo.model.TimeInfoBean;
import com.example.bamboo.myinterface.ffmpeg.OnCompleteListener;
import com.example.bamboo.myinterface.ffmpeg.OnPauseListener;
import com.example.bamboo.myinterface.ffmpeg.OnPreparedListener;
import com.example.bamboo.myinterface.ffmpeg.OnStartListener;
import com.example.bamboo.myinterface.ffmpeg.OnStopListener;
import com.example.bamboo.myinterface.ffmpeg.OnTimeInfoListener;
import com.example.bamboo.myview.PlayerView;
import com.example.bamboo.opengl.utils.TextRecourseReader;
import com.example.bamboo.util.TimeUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
    private int playPosition;
    private ArrayList<String> musicList;
    private boolean isCut = false;
    private String vertexCode, fragCode;
    private boolean isShow = true;
    private ConstraintSet constraintStart, constraintReply;
    private ConstraintLayout constraintLayout;
    private boolean isPrepared = false;

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
                if (!isPrepared) {
                    surfaceHolder = holder;
                    w = width;
                    h = height;
                    playerView = new PlayerView(vertexCode, fragCode, surfaceHolder.getSurface(), w, h);
                    playerView.prepared(path);
                    playPosition = 0;
                    onCall();
                    isPrepared = true;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY, endX, endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        Log.e(TAG, "onTouch: down" + startY);
                        isCut = true;
                        isShow = constraintAnimation(isShow);
                    case MotionEvent.ACTION_MOVE:
                        endY = (int) event.getY();
                        if (isCut && (endY - startY) > 500) {
                            Log.e(TAG, endY + "move" + startY);
                            if (playPosition > 0) {
                                playerView.playNext(musicList.get(playPosition - 1));
                                playPosition -= 1;
                            } else {
                                Toast.makeText(getActivity(), "这已经是第一首音乐了", Toast.LENGTH_SHORT).show();
                            }
                            isCut = false;
                            break;
                        } else if (isCut && (endY - startY) < -500) {
                            Log.e(TAG, endY + "move" + startY);
                            if (playPosition < musicList.size() - 1) {
                                playerView.playNext(musicList.get(playPosition + 1));
                                playPosition += 1;
                            } else {
                                Toast.makeText(getActivity(), "这已经是最后一首音乐了", Toast.LENGTH_SHORT).show();
                            }
                            isCut = false;
                            break;
                        }
                }
                return true;
            }
        });


        return v;
    }

    private void onCall() {
       playerView.setPreparedListener(new OnPreparedListener() {
           @Override
           public void onPrepared() {
               playerView.start();
               isPlaying = true;
           }
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

           Observable.create((ObservableOnSubscribe<TimeInfoBean>) emitter -> emitter.onNext(timeInfoBean))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean -> {
                            if (!isSetTime) {
                                totalTime.setText(TimeUtil.caseTime(bean.getTotalTime()));
                                seekBar.setMax(bean.getTotalTime());
                            }
                            currentTime.setText(TimeUtil.caseTime(bean.getCurrentTime()));
                            seekBar.setProgress(bean.getCurrentTime());
                        });
            }
        });
        playerView.setCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: 播放完成回调" + playPosition);
//                if (playPosition < musicList.size() - 1) {
//                    Toast.makeText(getActivity(), "准备播放下一首", Toast.LENGTH_SHORT).show();
//                    playerView.playNext(musicList.get(playPosition++));
//                } else {
//                    Toast.makeText(getActivity(), "已经是最后一首音乐了，准备从头开始", Toast.LENGTH_SHORT).show();
//                    playPosition = 0;
//                    playerView.playNext(musicList.get(playPosition));
//                }
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
            isShow = constraintAnimation(true);
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
        path = getActivity().getExternalCacheDir().getPath() + "/res/891.mp4";
        outPath = getActivity().getExternalCacheDir().getPath() + "/res/test.pcm";
        networkSource = "http://dl.stream.qqmusic.qq.com/C400003Chs7Y0jd49n.m4a?guid=1122016361&vkey=CB2B81BC8AFAA8E062DBA6147CD8CA439F1823527B8B7F3B070329495568FDABB6D7E2A0A2451220181E9366148ADBD3560C312E52EFFC29&uin=0&fromtag=66";
        recodeBtn = v.findViewById(R.id.homePager_recode_video);
        currentTime = v.findViewById(R.id.homePager_currentTime_tv);
        totalTime = v.findViewById(R.id.homePager_totalTime_tv);
        seekBar = v.findViewById(R.id.homePager_seekBar);
        musicList = new ArrayList<>();
        musicList.add("http://asklxf.coding.me/liaoxuefeng/v/python/install-py.mp4");
        musicList.add("http://asklxf.coding.me/liaoxuefeng/v/python/start-py.mp4");
//        musicList.add("http://michaelliao.gitcafe.io/video/git-apt-install.mp4");
        musicList.add("http://dl.stream.qqmusic.qq.com/C400002E3MtF0IAMMY.m4a?guid=1122016361&vkey=7EDECCE7ED528AFABCA9F530C2FA0E8CBB0ADABF90DF607A70C302F2C043A34B6F9F894F4E47BFEA065189ADE50D87E95281B9A5D06BFBAA&uin=0&fromtag=66");
        musicList.add("http://dl.stream.qqmusic.qq.com/C400003w4Tn23jENMJ.m4a?guid=1122016361&vkey=4E8440C7383FA8BB97CA238E829E74352645A7BA5E377C7E80B60B3E1E927EE7225EDD838E82DC5145FE2B2127269707AB78DFAE8D737F3E&uin=0&fromtag=66");
        musicList.add("http://124.193.230.147/amobile.music.tc.qq.com/C400004XePmv4CsaEq.m4a?guid=1122016361&vkey=14D52DBA0025A5A38C4D31AE2B900DD683C74CA7EA3D5FD367C7C20917E920865EB81D371D95D76FAE7C040CD96D960D8852FE8B40448F04&uin=0&fromtag=66");
        musicList.add("http://124.193.230.24/amobile.music.tc.qq.com/C400004dbfuf1jEjpI.m4a?guid=1122016361&vkey=F369C71AF877138C06D583C09D2582434049B768957492B78F75E2BD2257C7EC9C926606D002E32DB28C8823E1F37CDC9DA50F479A0BB1F1&uin=0&fromtag=66");
        musicList.add("http://dl.stream.qqmusic.qq.com/C400002f6kEI40uYmk.m4a?guid=1122016361&vkey=A41BF207292365BFA43D57172EC94D160AB334877DBCF41659F77BFE07D616C078C78C06040002D6B1138FAF43B5870F202FA34206372065&uin=0&fromtag=66");
        musicList.add("http://dl.stream.qqmusic.qq.com/C400003MsMD70D1xC9.m4a?guid=1122016361&vkey=015F17094EC0C89464D9565596295C2154AAE6F6022069CC844A9982F807365CF838E804A01B6FEA35AD3AEB794D82FAAD2F813281DA86DE&uin=0&fromtag=66");
        musicList.add("http://124.193.230.149/amobile.music.tc.qq.com/C4000044xahl3svaeK.m4a?guid=1122016361&vkey=B1EE299336328FDE4D8C31C77050795DB5C155246133B525B942EA8843BC17641EBCB3C731E7C528CD83839028BBABCCD4704C9512DF858D&uin=0&fromtag=66");
        musicList.add("http://dl.stream.qqmusic.qq.com/C400000jO3Qe1EEvuq.m4a?guid=1122016361&vkey=BFE6B61A74B0075092BE6F365F21E3881C36F9FE6BA4DF9D259C0893F95A1A03123F6F54D3B90DB0AB95CC674AE3C4B43DAB1A560E297E37&uin=0&fromtag=66");
        musicList.add("http://124.193.230.144/amobile.music.tc.qq.com/C400000jxuAK3aY3eU.m4a?guid=1122016361&vkey=F5ACA69426E3F307B20C5CC30315A96CA05543C71A56D4153E381EA695B6CD1D4CC7700B734DF232A34B2F32C5AF11E347322FB378B6505B&uin=0&fromtag=66");
        vertexCode = TextRecourseReader.readTextFileFromResource(getActivity(), R.raw.yuv_vertex_shader);
        fragCode = TextRecourseReader.readTextFileFromResource(getActivity(), R.raw.yuv_frag_shader);
        constraintLayout = v.findViewById(R.id.homePager_constraintLayout);
        constraintStart = new ConstraintSet();
        constraintStart.clone(getActivity(), R.layout.fragment_homepage_finish);

        constraintReply = new ConstraintSet();
        constraintReply.clone(getActivity(), R.layout.fragment_homepage);
    }


    @Override
    public void onPause() {
        super.onPause();
        playerView.pause();
        Log.e(TAG, "onPause: ");
    }


    @Override
    public void onStop() {
        playerView.stop();
        isPrepared = false;
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

    private boolean constraintAnimation(boolean isShow) {
        if (isShow) {
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintStart.applyTo(constraintLayout);
        } else {
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintReply.applyTo(constraintLayout);
        }
        return !isShow;
    }
}

