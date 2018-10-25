package com.example.bamboo.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.example.bamboo.util.AbstractActivityLifeCycleCallbacks;
import com.example.bamboo.util.CrashHandle;

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

    public static MyApplication getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
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

}
