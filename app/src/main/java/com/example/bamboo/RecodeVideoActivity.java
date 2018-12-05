package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;

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
    int frontCameraId, backCameraId;
    private int frontCameraOrientation, backCameraOrientation;
    private CameraCharacteristics frontCameraCharacteristics, backCameraCharacteristics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recode_video);
        //打开相机
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String ids[] = cameraManager.getCameraIdList();
            int cameraNum = ids.length;
            for (int i = 0; i < ids.length; i++) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(ids[i]);
                int orientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (orientation == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = i;
                    frontCameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                    frontCameraCharacteristics = characteristics;
                } else {
                    backCameraId = i;
                    backCameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                    backCameraCharacteristics = characteristics;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
