package com.example.bamboo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.myinterface.WaitingAnimationEndInterface;
import com.example.bamboo.myview.TimeButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo
 * @class 广告页
 * @time 2018/11/29 10:11
 * @change
 * @chang time
 * @class describe
 */
public class WaitActivity extends AppCompatActivity implements WaitingAnimationEndInterface {
    TimeButton timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_wait);
        initView();
        timeButton.startAnimation();
    }

    private void initView() {
        timeButton = findViewById(R.id.waiting_timeBtn);
    }

    @Override
    public void waitingEnd(boolean isEnd) {
        if (isEnd) {
            if (MyApplication.isFirst) {
                SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("is_first", false);
                editor.apply();
                startActivity(new Intent(this, StartActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish();
        }
    }
}
