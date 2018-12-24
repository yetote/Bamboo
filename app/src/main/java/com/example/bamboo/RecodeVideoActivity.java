package com.example.bamboo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bamboo.myview.RecodeButton;
import com.example.bamboo.util.BuildModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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
public class RecodeVideoActivity extends AppCompatActivity {
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private int frontCameraId = -1, backCameraId = -1;
    private int frontCameraOrientation, backCameraOrientation;
    private CameraCharacteristics frontCameraCharacteristics, backCameraCharacteristics;
    public static final int PERMISSION_CAMERA_CODE = 1;
    private Handler backGroundHandler;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageReader imageReader;
    private static final String TAG = "RecodeVideoActivity";
    private Button switchCameraBtn;
    private RecodeButton recodeButton;
    private String[] ids;

    enum CAMERA_ORIENTATION {
        /**
         * 相机为前置摄像头
         */
        CAMERA_ORIENTATION_FRONT,
        /**
         * 相机为后置摄像头
         */
        CAMERA_ORIENTATION_BACK
    }

    private CAMERA_ORIENTATION cameraOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recode_video);
        switch (Build.MODEL) {
            case BuildModel.XIAOMI_MIX2S:
                new AlertDialog.Builder(RecodeVideoActivity.this).setMessage("是否旋转屏幕").setPositiveButton("是", (dialog, which) -> setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT)).setNegativeButton("否", (dialog, which) -> Toast.makeText(RecodeVideoActivity.this, "这可能导致您的拍摄角度出现问题", Toast.LENGTH_SHORT).show()).show();
                break;
            default:
                break;
        }

        initView();

        onClick();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                surfaceHolder = holder;
                if (ContextCompat.checkSelfPermission(RecodeVideoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RecodeVideoActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_CODE);
                } else {
                    getCameraInfo();
                    if (backCameraId != -1 && ids.length != 0) {
                        openCamera(backCameraId);
                        cameraOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_BACK;
                    }
                }
                imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {

                    }
                }, backGroundHandler);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });


    }

    private void onClick() {
        switchCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraDevice != null) {
                    cameraDevice.close();
                    if (cameraOrientation == CAMERA_ORIENTATION.CAMERA_ORIENTATION_BACK) {
                        if (frontCameraId != -1) {
                            openCamera(frontCameraId);
                            cameraOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_FRONT;
                        } else {
                            Toast.makeText(RecodeVideoActivity.this, "未找到前置摄像机", Toast.LENGTH_SHORT).show();
                        }
                    } else if (cameraOrientation == CAMERA_ORIENTATION.CAMERA_ORIENTATION_FRONT) {
                        if (backCameraId != -1) {
                            openCamera(backCameraId);
                            cameraOrientation = CAMERA_ORIENTATION.CAMERA_ORIENTATION_BACK;
                        } else {
                            Toast.makeText(RecodeVideoActivity.this, "未找到后置摄像机", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void initView() {
        backGroundHandler = new Handler(getMainLooper());
        surfaceView = findViewById(R.id.recode_video_surfaceView);
        switchCameraBtn = findViewById(R.id.recode_video_switchCamera_btn);
        recodeButton = findViewById(R.id.recode_video_recodeBtn);
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
                        backCameraCharacteristics = characteristics;
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
                CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(surfaceHolder.getSurface());
//                if (imageReader == null) {
                cameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {
                        Toast.makeText(RecodeVideoActivity.this, "摄像头完成配置，可以处理Capture请求了。", Toast.LENGTH_SHORT).show();
//                            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,);
                        CaptureRequest captureRequest = captureRequestBuilder.build();
                        try {
                            session.setRepeatingRequest(captureRequest, null, backGroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        Toast.makeText(RecodeVideoActivity.this, "摄像头配置失败", Toast.LENGTH_SHORT).show();
                    }
                }, null);
//                } else {
//                    Toast.makeText(this, "无法加载ImageRenderer", Toast.LENGTH_SHORT).show();
//                }
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
                }, backGroundHandler);
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
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    getCameraInfo();
                } else {
                    Toast.makeText(this, "您拒绝了相机权限可能导致相机无法使用", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
