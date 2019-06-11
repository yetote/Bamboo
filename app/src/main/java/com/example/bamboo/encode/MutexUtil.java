package com.example.bamboo.encode;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.core.app.ActivityCompat;

import com.example.bamboo.RecodeVideoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2019/5/31 14:46
 * @change
 * @chang time
 * @class describe
 */
public class MutexUtil {
    private CameraUtil cameraUtil;
    private Context context;
    private boolean isCamera;
    private AudioRecordUtil audioRecordUtil;
    private static final String TAG = "MutexUtil";


    public MutexUtil(Context context, int width, int height, String videoPath, String audioPath) {
        this.context = context;
        cameraUtil = new CameraUtil(context, width, height, videoPath);
        isCamera = cameraUtil.initCamera();
        audioRecordUtil = new AudioRecordUtil(48000, 2, audioPath);
    }

    public void open(Surface surface) {
        if (isCamera) {
            Log.e(TAG, "open: ");
            cameraUtil.openCamera(surface);
        }
    }

    public void record(Surface surface, int orientation) {
        cameraUtil.startRecord(surface, orientation);
        audioRecordUtil.startRecord();
    }


    public Size getCameraBestSize(int cameraType) {
        if (isCamera) {
            return cameraUtil.getPreviewSize(cameraType);
        }
        return null;
    }

}
