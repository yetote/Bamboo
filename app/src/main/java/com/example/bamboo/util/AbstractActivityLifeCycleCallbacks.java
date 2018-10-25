package com.example.bamboo.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 监听Activity生命周期
 * @time 2018/10/25 11:09
 * @change
 * @chang time
 * @class describe
 */
public abstract class AbstractActivityLifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }
}
