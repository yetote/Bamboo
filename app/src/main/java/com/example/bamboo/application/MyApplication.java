package com.example.bamboo.application;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.bamboo.util.AbstractActivityLifeCycleCallbacks;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.CoordinateTransformation;
import com.example.bamboo.util.CrashHandle;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * com.demo.yetote.cubegame.application
 *
 * @author Swg
 * @date 2018/1/25 10:10
 */
public class MyApplication extends Application {
    private static MyApplication mContext;
    private List<Activity> activityList;
    private View statusBar;
    private static final String TAG = "MyApplication";
    public static boolean isFirst;
    public static boolean isLogin = false;
    public static String uName;
    private CallBackUtils callBackUtils;

    public CallBackUtils getCallBackUtils() {
        return callBackUtils;
    }

    public static MyApplication getContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        initHuanxin();
        callBackUtils = new CallBackUtils();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        int pxWidth = dm.widthPixels;
        // 屏幕高度（像素）
        int pxHeight = dm.heightPixels;
        float density = dm.density;
        int dpWidth = (int) (pxWidth / density);
        int dpHeight = (int) (pxHeight / density);

        CoordinateTransformation.initDisplay(pxWidth, pxHeight, dpWidth, dpHeight);

        startCrash();

        activityList = Collections.synchronizedList(new ArrayList<Activity>());
        registerActivityLifecycleCallbacks(new AbstractActivityLifeCycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                super.onActivityCreated(activity, bundle);
                activityList.add(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                activityList.remove(activity);
            }
        });

        initRetrofit();
        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
        isFirst = sp.getBoolean("is_first", true);
//        isLogin = sp.getBoolean("is_login", false);
    }

    /**
     * 初始化环信sdk
     */
    private void initHuanxin() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getApplicationContext().getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);

        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //是否自动登录
        options.setAutoLogin(false);
        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //初始化easyUi
        EaseUI.getInstance().init(this, options);
    }

    private void initRetrofit() {

    }

    private void startCrash() {
        CrashHandle crashHandler = CrashHandle.getInstance();
        crashHandler.init(mContext);
    }

    public void exitApp() {
        Iterator<Activity> iterator = activityList.listIterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            activity.finish();
            iterator.remove();
        }
        activityList.clear();
        System.exit(0);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
