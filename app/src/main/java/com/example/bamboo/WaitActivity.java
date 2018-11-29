package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.myinterface.WaitingAnimationEndInterface;
import com.example.bamboo.myview.TimeButton;
import com.example.bamboo.opengl.objects.SelectTag;
import com.example.bamboo.util.StatusBarUtils;

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
        setContentView(R.layout.activity_wait);
        StatusBarUtils.changedStatusBar(this);
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
        }
    }
}
