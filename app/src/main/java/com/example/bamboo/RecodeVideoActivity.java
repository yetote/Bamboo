package com.example.bamboo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.bamboo.encode.CameraUtil;
import com.example.bamboo.encode.EncodeListener;
import com.example.bamboo.encode.MutexUtil;
import com.example.bamboo.encode.Record;
import com.example.bamboo.myview.RecodeButton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo
 * @class 录制视频页面
 * @time 2018/11/28 15:41
 * @change
 * @chang time
 * @class describe
 */
public class RecodeVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RecodeVideoActivity";
    private Button switchCameraBtn;
    private RecodeButton recodeButton;
    private ImageView iv, switchCamera, switchVideo;
    private TextureView textureView;
    private boolean isRecording;
    private Record record;
    private SurfaceTexture surfaceTexture;
    private String videoPath, audioPath, path;
    private String[] permissionArr = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int PERMISSION_CAMERA_AND_RECORD_CODE = 1;
    private static final int PERMISSION_OPEN_RECORD_CODE = 2;
    private Size size;
    private int dw, dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_recode_video);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metric);

        dw = metric.widthPixels;
        dh = metric.heightPixels;

        initView();
        onClick();
        record.mutexState(state -> Observable.create((ObservableOnSubscribe<Integer>) emitter -> emitter.onNext(state))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(mutexState -> {
                    switch (mutexState) {
                        case MutexUtil.MUTEX_STOP:
                            Toast.makeText(RecodeVideoActivity.this, "录制完成", Toast.LENGTH_SHORT).show();
                            break;
                        case MutexUtil.MUTEX_TIME_SHORT:
                            Toast.makeText(RecodeVideoActivity.this, "录制时间太短", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }

                }));
    }

    private void onClick() {
        switchCameraBtn.setOnClickListener(this);
        recodeButton.setOnClickListener(this);
        switchCamera.setOnClickListener(this);
        switchVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recode_video_switchCamera_btn:

                break;
            case R.id.recode_video_recodeBtn:
                if (!isRecording) {
                    Toast.makeText(this, "开始录制", Toast.LENGTH_SHORT).show();
                    isRecording = true;
                    boolean permissionCheckResult = true;
                    for (int i = 0; i < permissionArr.length; i++) {
                        if (ActivityCompat.checkSelfPermission(this, permissionArr[i]) != PackageManager.PERMISSION_GRANTED) {
                            permissionCheckResult = false;
                            break;
                        }
                    }

                    if (!permissionCheckResult) {
                        ActivityCompat.requestPermissions(this, permissionArr, PERMISSION_OPEN_RECORD_CODE);
                    }
                    changeTextureSize();
                    record.start(getWindowManager().getDefaultDisplay().getRotation(), new Surface(surfaceTexture));
                } else {
                    isRecording = false;
                    changeTextureSize();
                    record.stop(new Surface(surfaceTexture));
                }
                break;
            case R.id.recode_video_switch_camera:
//                isCamera = true;
//                changeCameraModeAnimation(isCamera);
                break;
            case R.id.recode_video_switch_video:
//                isCamera = false;
//                changeCameraModeAnimation(isCamera);
                break;
            default:
                break;
        }
    }


    private void initView() {
        videoPath = getExternalFilesDir(null).getPath() + "/test.h264";
        audioPath = getExternalFilesDir(null).getPath() + "/test.aac";
        path = getExternalFilesDir(null).getPath() + "/test.mp4";
        record = new Record(this, 48000, 2, dw, dh, path);
        textureView = findViewById(R.id.recode_video_texture);
        switchCameraBtn = findViewById(R.id.recode_video_switchCamera_btn);
        recodeButton = findViewById(R.id.recode_video_recodeBtn);
        iv = findViewById(R.id.recode_video_iv);
        switchCamera = findViewById(R.id.recode_video_switch_camera);
        switchVideo = findViewById(R.id.recode_video_switch_video);
        TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                Log.e(TAG, "onSurfaceTextureAvailable: 加载listener");
                surfaceTexture = surface;
                boolean permissionCheckResult = true;
                for (int i = 0; i < permissionArr.length; i++) {
                    if (ActivityCompat.checkSelfPermission(RecodeVideoActivity.this, permissionArr[i]) != PackageManager.PERMISSION_GRANTED) {
                        permissionCheckResult = false;
                        break;
                    }
                }

                if (!permissionCheckResult) {
                    ActivityCompat.requestPermissions(RecodeVideoActivity.this, permissionArr, PERMISSION_OPEN_RECORD_CODE);
                } else {
                    record.init();
                    changeTextureSize();
                    record.openCamera(new Surface(surfaceTexture));
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
        textureView.setSurfaceTextureListener(textureListener);

    }

    private void changeTextureSize() {
        if (size == null) {
            size = record.getBestSize();
        }
        surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA_AND_RECORD_CODE:

                break;
            case PERMISSION_OPEN_RECORD_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "您拒绝了权限可能导致相机无法使用", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                record.init();
                RecodeVideoActivity.this.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | SYSTEM_UI_FLAG_FULLSCREEN
                        | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                changeTextureSize();
                record.openCamera(new Surface(surfaceTexture));
                break;
            default:
                break;
        }
    }


    /**
     * 切换相机模式动画
     *
     * @param isCamera 是否为照相机模式
     */
    private void changeCameraModeAnimation(boolean isCamera) {
        ObjectAnimator oa1, oa2;
        int start, end;
        if (isCamera) {
            start = 0;
            end = 1;
        } else {
            start = 1;
            end = 0;
        }
        oa1 = ObjectAnimator.ofFloat(switchVideo, "alpha", start, end);
        oa2 = ObjectAnimator.ofFloat(switchCamera, "alpha", end, start);
        oa1.setDuration(1000);
        oa2.setDuration(1000);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(oa1, oa2);

        animatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isCamera) {
                    switchVideo.setVisibility(View.VISIBLE);
                } else {
                    switchCamera.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isCamera) {
                    switchCamera.setVisibility(View.GONE);
                } else {
                    switchVideo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet1.start();
    }
}
