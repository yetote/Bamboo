package com.example.bamboo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bamboo.encode.MutexUtil;
import com.example.bamboo.myview.RecodeButton;
import com.example.bamboo.encode.CameraUtil;

import java.util.Arrays;
import java.util.Set;

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

    private SurfaceTexture surfaceTexture;
    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            surfaceTexture = surface;
            changeTextureSize();
            boolean permissionCheckResult = true;
            for (int i = 0; i < permissionArr.length; i++) {
                if (ActivityCompat.checkSelfPermission(RecodeVideoActivity.this, permissionArr[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionCheckResult = false;
                    break;
                }
            }

            if (!permissionCheckResult) {
                ActivityCompat.requestPermissions(RecodeVideoActivity.this, permissionArr, PERMISSION_CAMERA_AND_RECORD_CODE);
            } else {

                mutexUtil.open(new Surface(surfaceTexture));
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
    private MutexUtil mutexUtil;
    private String videoPath, audioPath;
    private String[] permissionArr = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private static final int PERMISSION_CAMERA_AND_RECORD_CODE = 1;
    private Size size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | SYSTEM_UI_FLAG_FULLSCREEN
                | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_recode_video);
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
//        switch (Build.MODEL) {
//            case BuildModel.XIAOMI_MIX2S:
//                new AlertDialog.Builder(RecodeVideoActivity.this).setMessage("是否旋转屏幕").setPositiveButton("是", (dialog, which) -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)).setNegativeButton("否", (dialog, which) -> Toast.makeText(RecodeVideoActivity.this, "这可能导致您的拍摄角度出现问题", Toast.LENGTH_SHORT).show()).show();
//                break;
//            default:
//                break;
//        }

        initView();
//        switchCamera.setVisibility(View.GONE);
        onClick();
//        cameraUtil = new CameraUtil(this, point.x, point.y);
//        isOpenCamera = cameraUtil.initCamera();
        mutexUtil = new MutexUtil(this, point.x, point.y, videoPath, audioPath);
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
//                if (cameraDevice != null) {
//                    cameraDevice.close();
//                    if (cameraOrientation == CAMERA_ORIENTATION.CAMERA_ORIENTATION_BACK) {
//                        if (frontCameraId != -1) {
//                            openCamera(frontCameraId);
//                            cameraOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_FRONT;
//                        } else {
//                            Toast.makeText(RecodeVideoActivity.this, "未找到前置摄像机", Toast.LENGTH_SHORT).show();
//                        }
//                    } else if (cameraOrientation == CAMERA_ORIENTATION.CAMERA_ORIENTATION_FRONT) {
//                        if (backCameraId != -1) {
//                            openCamera(backCameraId);
//                            cameraOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_BACK;
//                        } else {
//                            Toast.makeText(RecodeVideoActivity.this, "未找到后置摄像机", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
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
                        ActivityCompat.requestPermissions(this, permissionArr, PERMISSION_CAMERA_AND_RECORD_CODE);
                    }
                    changeTextureSize();
                    mutexUtil.record(getWindowManager().getDefaultDisplay().getRotation());
                } else {
                    Toast.makeText(this, "录制结束", Toast.LENGTH_SHORT).show();
                    isRecording = false;
                    changeTextureSize();
                    mutexUtil.stop();
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
        textureView = findViewById(R.id.recode_video_texture);
        switchCameraBtn = findViewById(R.id.recode_video_switchCamera_btn);
        recodeButton = findViewById(R.id.recode_video_recodeBtn);
        iv = findViewById(R.id.recode_video_iv);
        switchCamera = findViewById(R.id.recode_video_switch_camera);
        switchVideo = findViewById(R.id.recode_video_switch_video);
        textureView.setSurfaceTextureListener(textureListener);
        videoPath = getExternalFilesDir(null).getPath() + "/res/test.h264";
        audioPath = getExternalFilesDir(null).getPath() + "/res/test.aac";
    }

    public void changeTextureSize() {
        if (size == null) {
            size = mutexUtil.getCameraBestSize(CameraUtil.CAMERA_TYPE_BACK);
        }
        surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA_AND_RECORD_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "您拒绝了权限可能导致相机无法使用", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                RecodeVideoActivity.this.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | SYSTEM_UI_FLAG_FULLSCREEN
                        | SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                changeTextureSize();
                mutexUtil.open(new Surface(surfaceTexture));
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
