package com.example.bamboo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class describe
 * @time 2018/10/25 13:33
 * @change
 * @chang time
 * @class describe
 */
public class CrashHandle implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CacheHandle";
    private Context context;
    private String path;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        saveCrash(e);
        uploadCrash();

    }

    private static final CrashHandle ourInstance = new CrashHandle();

    public static CrashHandle getInstance() {
        return ourInstance;
    }

    private CrashHandle() {
    }

    private void uploadCrash() {
    }

    private void saveCrash(Throwable throwable) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean mkdirsSuccessful = dir.mkdirs();
            if (!mkdirsSuccessful) {
                Log.e(TAG, "saveCrash: " + "创建文件夹失败");
            }
        }
        long currentTime = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(currentTime);

        File file = new File(path + time + ".track");
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);

            //打印应用信息
            pw.println("发生异常时间" + time);
            pw.println("应用版本" + pi.versionName);
            pw.println("应用版本号：" + pi.versionCode);
            pw.println("android版本号：" + Build.VERSION.RELEASE);
            pw.println("android版本号API：" + Build.VERSION.SDK_INT);
            pw.println("手机制造商:" + Build.MANUFACTURER);
            pw.println("手机型号：" + Build.MODEL);

            //打印异常信息
            throwable.printStackTrace(pw);
            Log.e(TAG, "saveThrowableToSDCard: " + throwable);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(this);
        this.context = context;
        path = context.getExternalCacheDir().getPath() + "/crash/";
    }
}