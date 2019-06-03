package com.example.bamboo.encode;

import android.content.Context;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

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
    private BlockingQueue<byte[]> audioQueue, videoQueue;
    private AudioEncode audioEncode;
    private VideoEncode videoEncode;
    private CameraUtil cameraUtil;
    private Context context;
    private boolean isCamera;
    private AudioRecordUtil audioRecordUtil;
    private static final String TAG = "MutexUtil";

    public MutexUtil(Context context, int width, int height) {
        this.context = context;
        audioQueue = new LinkedBlockingQueue<>();
        videoQueue = new LinkedBlockingQueue<>();
        audioEncode = new AudioEncode(48000, 2);
        videoEncode = new VideoEncode(640, 1280);
        cameraUtil = new CameraUtil(context, width, height);
        isCamera = cameraUtil.initCamera();
        audioRecordUtil = new AudioRecordUtil(48000, 2);
    }

    public void open(Surface surface) {
        if (isCamera) {
            Log.e(TAG, "open: ");
            cameraUtil.openCamera(surface);
        }
    }

    public void record(Surface surface) {
        cameraUtil.startRecord(surface);

    }


    public Size getCameraBestSize(int cameraType) {
        if (isCamera) {
            return cameraUtil.getPreviewSize(cameraType);
        }
        return null;
    }

}
