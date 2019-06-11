package com.example.bamboo.encode;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 相机工具
 * @time 2019/5/30 14:40
 * @change
 * @chang time
 * @class describe
 */
public class CameraUtil {
    private VideoEncode videoEncode;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCharacteristics frontCameraCharacteristics, backCameraCharacteristics;
    private String[] cameraIds;
    private int frontCameraId = -1, backCameraId = -1;
    private Size frontCameraSize, backCameraSize;
    private CaptureRequest.Builder previewCaptureBuilder, recordCaptureBuilder;
    private Context context;
    private int width, height;
    private int recordWidth = 1280, recordHeight = 640;
    private ImageReader imageReader;
    private static final String TAG = "CameraUtil";
    public static final int CAMERA_TYPE_FRONT = 1;
    public static final int CAMERA_TYPE_BACK = 2;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CameraCaptureSession captureSession;
    private ImageReader.OnImageAvailableListener imageAvailableListener = reader -> {
        Log.e(TAG, "onImageAvailable: 接受图片");
        Image image = reader.acquireNextImage();
        changeNv21(image);
        image.close();
    };

    private static final SparseIntArray ORIENTATION = new SparseIntArray();

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    public CameraUtil(Context context, int width, int height,String path) {
        this.context = context;
        this.width = width;
        this.height = height;
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new android.os.Handler(backgroundThread.getLooper());
        imageReader = ImageReader.newInstance(recordWidth, recordHeight, ImageFormat.YUV_420_888, 1);
        imageReader.setOnImageAvailableListener(imageAvailableListener, backgroundHandler);
        videoEncode = new VideoEncode(recordWidth, recordHeight,path);
    }

    public boolean initCamera() {
        try {
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (cameraManager == null) {
                return false;
            }
            cameraIds = cameraManager.getCameraIdList();
            if (cameraIds.length == 0) {
                return false;
            }

            for (int i = 0; i < cameraIds.length; i++) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraIds[i]);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = i;
                    frontCameraCharacteristics = cameraCharacteristics;
                    checkSupportLevel("前置", cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                    chooseBestSize(CAMERA_TYPE_FRONT, cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.YUV_420_888));
                } else if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    backCameraId = i;
                    backCameraCharacteristics = cameraCharacteristics;
                    checkSupportLevel("后置", cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                    chooseBestSize(CAMERA_TYPE_BACK, cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.YUV_420_888));

                }
            }
            if (frontCameraId == -1 && backCameraId == -1) {
                return false;
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void openCamera(Surface surface) {
        if (backCameraId == -1 && frontCameraId == -1) {
            return;
        }
        String cameraId;
        if (backCameraId != -1) {
            cameraId = cameraIds[backCameraId];
        } else {
            cameraId = cameraIds[frontCameraId];
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    openPreview(surface);
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {

                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {

                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void openPreview(Surface surface) {
        if (surface == null) {
            return;
        }
        try {
            previewCaptureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewCaptureBuilder.addTarget(surface);
            previewCaptureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        captureSession.setRepeatingRequest(previewCaptureBuilder.build(), null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void startRecord(Surface surface,int orientation) {
        videoEncode.setRecording(true);
        videoEncode.startEncode();
        if (captureSession != null) {
            captureSession.close();
        }
        try {
            recordCaptureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            recordCaptureBuilder.addTarget(imageReader.getSurface());
            recordCaptureBuilder.addTarget(surface);
            recordCaptureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATION.get(orientation));
            cameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface(), surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    captureSession = session;
                    try {
                        captureSession.setRepeatingRequest(recordCaptureBuilder.build(), null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public Size getPreviewSize(int cameraType) {
        switch (cameraType) {
            case CAMERA_TYPE_FRONT:
                return frontCameraSize;
            case CAMERA_TYPE_BACK:
                return backCameraSize;
            default:
                return null;
        }
    }


    private void chooseBestSize(int cameraType, Size[] supportResolvingSize) {
        float diff = Float.MAX_VALUE;
        int bestWidth = 0, bestHeight = 0;

        float bestRatio = (float) height / (float) width;
        int bestIndex = -1;
        for (int j = 0; j < supportResolvingSize.length - 1; j++) {

            float newDiff = Math.abs(supportResolvingSize[j].getWidth() / supportResolvingSize[j].getHeight() - bestRatio);
            if (newDiff == 0) {
                bestWidth = supportResolvingSize[j].getWidth();
                bestHeight = supportResolvingSize[j].getHeight();
                bestIndex = j;
                break;
            }

            if (newDiff < diff) {
                bestWidth = supportResolvingSize[j].getWidth();
                bestHeight = supportResolvingSize[j].getHeight();
                diff = newDiff;
                bestIndex = j;
            }
        }
        if (bestWidth == 0) {
            Log.e(TAG, "chooseBestSize: 最佳分辨率为0");
            return;
        }
        Log.e(TAG, "chooseBestSize: bestPreviewSize" + bestWidth + "\n" + bestHeight);
        if (cameraType == CAMERA_TYPE_FRONT) {
            frontCameraSize = supportResolvingSize[bestIndex];
        } else {
            backCameraSize = supportResolvingSize[bestIndex];
        }
    }

    private void checkSupportLevel(String camera, int supportLevel) {
        switch (supportLevel) {
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY:
                Log.e(TAG, "checkSupportLevel: " + camera + "支持级别为:不支持");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED:
                Log.e(TAG, "checkSupportLevel: " + camera + "支持级别为:简单支持");
                break;
            case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL:
                Log.e(TAG, "checkSupportLevel: " + camera + "支持级别为:部分支持");
                break;
            case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL:
                Log.e(TAG, "checkSupportLevel: " + camera + "支持级别为:设备还支持传感器，闪光灯，镜头和后处理设置的每帧手动控制，以及高速率的图像捕获");
                break;
            case CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_3:
                Log.e(TAG, "checkSupportLevel: " + camera + "支持级别为:设备还支持YUV重新处理和RAW图像捕获，以及其他输出流配置");
                break;
            default:
                Log.e(TAG, "checkSupportLevel: " + camera + "未检测到相机信息");
                break;
        }

    }

    private void changeNv21(Image image) {
        long now = System.currentTimeMillis();
        int w = image.getWidth();
        int h = image.getHeight();
        Log.e(TAG, "dataEnqueue: 图片宽高" + w + h);
        byte[] yBuffer = new byte[w * h];
        byte[] uvBuffer = new byte[w * h / 2];
        byte[] dataBuffer = new byte[w * h * 3 / 2];
        image.getPlanes()[0].getBuffer().get(yBuffer);
        image.getPlanes()[1].getBuffer().get(uvBuffer, 0, w * h / 2 - 1);
        uvBuffer[w * h / 2 - 1] = image.getPlanes()[2].getBuffer().get(w * h / 2 - 2);
        System.arraycopy(yBuffer, 0, dataBuffer, 0, yBuffer.length);
        System.arraycopy(uvBuffer, 0, dataBuffer, yBuffer.length, uvBuffer.length);
        videoEncode.pushData(dataBuffer);
        Log.e(TAG, "dataEnqueue: 耗时" + (System.currentTimeMillis() - now));
    }
}

