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
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private int frontCameraId = -1, backCameraId = -1;
    private int frontCameraOrientation, backCameraOrientation;
    private CameraCharacteristics frontCameraCharacteristics, backCameraCharacteristics;
    public static final int PERMISSION_CAMERA_CODE = 1;
    private Handler backgroundHandler;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageReader imageReader;
    private static final String TAG = "RecodeVideoActivity";
    private Button switchCameraBtn;
    private RecodeButton recodeButton;
    private String[] ids;
    private CaptureRequest.Builder previewRequestBuilder;
    private int previewState;
    private CameraCaptureSession captureSession;
    private ImageView iv, switchCamera, switchVideo;
    private TextureView textureView;
    ///为了使照片竖直显示
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * 相机模式
     */
    private boolean isCamera = true;

    private Bitmap bitmap;
    private Bitmap resource;
    private CameraUtil cameraUtil;
    private boolean isOpenCamera;
    private SurfaceTexture surfaceTexture;
    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            surfaceTexture = surface;
            Size size = mutexUtil.getCameraBestSize(CameraUtil.CAMERA_TYPE_BACK);
            if (size != null) {
                surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
            }
            if (ActivityCompat.checkSelfPermission(RecodeVideoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RecodeVideoActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_CODE);
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
//        onClick();
//        cameraUtil = new CameraUtil(this, point.x, point.y);
//        isOpenCamera = cameraUtil.initCamera();
        mutexUtil = new MutexUtil(this, point.x, point.y);
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
//                takePhoto();
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

    /**
     * 准备拍照
     */
    private void takePhoto() {
        //对焦
        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
        //修改状态
//        previewState = STATE_WAITING_LOCK;

        CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
            @Override
            public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
                super.onCaptureProgressed(session, request, partialResult);
            }

            @Override
            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                super.onCaptureCompleted(session, request, result);
//等待对焦
                final Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                if (afState == null) {
                    Toast.makeText(RecodeVideoActivity.this, "对焦失败", Toast.LENGTH_SHORT).show();
                    captureStillPicture();
                } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_INACTIVE == afState || CaptureResult.CONTROL_AF_STATE_PASSIVE_SCAN == afState) {
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                        //对焦完成，开始拍照
                        captureStillPicture();
                    }
                } else {
//                    runPreCaptureSequence();
                }
            }
        };

        //发送对焦请求
        try {
            captureSession.capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     */
    private void captureStillPicture() {
        Log.e(TAG, "captureStillPicture: " + "拍照");
        if (cameraDevice == null) {
            Log.e(TAG, "captureStillPicture: " + "device 为空");
            return;
        }
        //构建用来拍照的CaptureRequest
        try {
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            //使用相同的AR和AF模式作为预览
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 获取手机方向
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            //设置方向
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            //创建会话
            CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    Log.d(TAG, "onCaptureCompleted: ");
                }
            };
            //停止连续取景
            captureSession.stopRepeating();
            //捕获照片
            captureSession.capture(captureBuilder.build(), captureCallback, null);
            //重置自动对焦
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            captureSession.capture(previewRequestBuilder.build(), captureCallback, backgroundHandler);
            //相机恢复正常的预览状态
//            previewState = STATE_PREVIEW;
            //打开连续取景模式
            captureSession.setRepeatingRequest(previewRequestBuilder.build(), captureCallback, backgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 录制视频
     */
    private void recodeVideo() {

    }

    private void initView() {
        backgroundHandler = new Handler(getMainLooper());
        textureView = findViewById(R.id.recode_video_texture);
        switchCameraBtn = findViewById(R.id.recode_video_switchCamera_btn);
        recodeButton = findViewById(R.id.recode_video_recodeBtn);
        iv = findViewById(R.id.recode_video_iv);
        switchCamera = findViewById(R.id.recode_video_switch_camera);
        switchVideo = findViewById(R.id.recode_video_switch_video);
        textureView.setSurfaceTextureListener(textureListener);
    }

    private void getCameraInfo() {

        //获取相机服务
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        ids = new String[]{};
        try {
            //获取相机列表
            if (cameraManager != null) {
                ids = cameraManager.getCameraIdList();
                int cameraNum = ids.length;
                for (int i = 0; i < ids.length; i++) {
                    //获取相机参数
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(ids[i]);
                    int orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (orientation == CameraCharacteristics.LENS_FACING_FRONT) {
                        //前置摄像头id
                        frontCameraId = i;
                        //拍照方向
                        frontCameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                        frontCameraCharacteristics = characteristics;
                    } else {
                        backCameraId = i;
                        backCameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//                        backCameraCharacteristics = characteristics;
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        Set s = characteristics.getPhysicalCameraIds();
                        Log.e(TAG, "openCamera: " + s.toString());
                    }
                    final int supportLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    switch (supportLevel) {
                        case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY:
                            Toast.makeText(this, "支持级别为:不支持", Toast.LENGTH_SHORT).show();
                            break;
                        case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED:
                            Toast.makeText(this, "支持级别为:简单支持", Toast.LENGTH_SHORT).show();
                            break;
                        case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL:
                            Toast.makeText(this, "支持级别为:部分支持", Toast.LENGTH_SHORT).show();
                            break;
                        case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL:
                            Toast.makeText(this, "设备还支持传感器，闪光灯，镜头和后处理设置的每帧手动控制，以及高速率的图像捕获", Toast.LENGTH_SHORT).show();
                            break;
                        case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_3:
                            Toast.makeText(this, "设备还支持YUV重新处理和RAW图像捕获，以及其他输出流配置", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(this, "未检测到相机信息", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                }
            } else {
                Toast.makeText(this, "未检查到相机列表，请检查是否开启相机权限", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开相机预览
     */
    private void openPreview() {

        if (cameraDevice != null) {
            try {
                previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                previewRequestBuilder.addTarget(surfaceHolder.getSurface());
//                if (imageReader == null) {
                cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        captureSession = session;
                        Toast.makeText(RecodeVideoActivity.this, "摄像头完成配置，可以处理Capture请求了。", Toast.LENGTH_SHORT).show();
//                            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,);
                        CaptureRequest captureRequest = previewRequestBuilder.build();
                        try {
                            captureSession.setRepeatingRequest(captureRequest, null, backgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        Toast.makeText(RecodeVideoActivity.this, "摄像头配置失败", Toast.LENGTH_SHORT).show();
                    }
                }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "相机打开失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 开启相机
     */
    public void openCamera(int cameraId) {
        if (cameraId != -1 && ids.length != 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                cameraManager.openCamera(ids[cameraId], new CameraDevice.StateCallback() {
                    @Override
                    public void onOpened(@NonNull CameraDevice camera) {
                        //开启完成回调
                        cameraDevice = camera;
                        Toast.makeText(RecodeVideoActivity.this, "相机打开成功", Toast.LENGTH_SHORT).show();
                        openPreview();
                    }

                    @Override
                    public void onDisconnected(@NonNull CameraDevice camera) {
                        //与设备断开连接回调(不在使用)
                        cameraDevice.close();
                        Toast.makeText(RecodeVideoActivity.this, "相机不在使用", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull CameraDevice camera, int error) {
                        //打开失败回调
                        cameraDevice.close();
                        Toast.makeText(RecodeVideoActivity.this, "相机打开失败", Toast.LENGTH_SHORT).show();
                    }
                }, backgroundHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mutexUtil.open(new Surface(surfaceTexture));
                } else {
                    Toast.makeText(this, "您拒绝了相机权限可能导致相机无法使用", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (captureSession != null) {
            captureSession.close();
        }
        if (cameraDevice != null) {
            cameraDevice.close();
        }
        if (resource != null) {
            resource.recycle();
        }

        if (bitmap != null) {
            bitmap.recycle();
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

    private void adjustScreen(View sv) {
        int height = sv.getHeight();
        int width = sv.getWidth();
        if (height > width) {
            float justH = width * 4.f / 3;
            surfaceView.setScaleX(height / justH);
        } else {
            float justW = height * 4.f / 3;
            surfaceView.setScaleY(width / justW);
        }
    }

}
