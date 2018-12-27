
package com.example.bamboo.util;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;

import com.example.bamboo.R;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 状态栏工具
 * @time 2018/11/28 16:26
 * @change
 * @chang time
 * @class describe
 */
public class StatusBarUtils {

    /**
     * 将状态栏改变为自定义的状态栏
     *
     * @param activity activity
     */
    public static void changedStatusBar(Activity activity) {
        activity.getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                View statusBar = null;
                if (statusBar == null) {
                    int identifier = activity.getResources().getIdentifier("statusBarBackground", "id", "android");
                    statusBar = activity.getWindow().findViewById(identifier);
                }
                statusBar.setBackgroundResource(R.drawable.toolbar_gradient_background);
                activity.getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });
    }

    /**
     * 透明状态栏
     *
     * @param activity activity
     */
    public static void transparentStatusBar(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
