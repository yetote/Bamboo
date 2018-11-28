package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo
 * @class 注册页面
 * @time 2018/11/28 15:41
 * @change
 * @chang time
 * @class describe
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    private RelativeLayout include;
    private Button qqLogin, weChatLogin, sinaLogin;
    private static final String TAG = "RegisterActivity";
    private Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置进出Activity动画为null
        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        setContentView(R.layout.activity_register);

        StatusBarUtils.changedStatusBar(this);

        initData();
        initViews();
        onClick();
    }

    private void initData() {
//        mTencent = Tencent.createInstance(Config.QQLOGIN_APPID, this);
    }

    private void onClick() {
        fab.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    private void initViews() {
        fab = findViewById(R.id.register_fab);
        include = findViewById(R.id.register_other_login);
        qqLogin = include.findViewById(R.id.QQLogin);
        weChatLogin = include.findViewById(R.id.WeChatLogin);
        sinaLogin = include.findViewById(R.id.SinaLogin);
        sure = findViewById(R.id.register_sure);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_fab:
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                startActivity(new Intent(this, PwdLoginActivity.class), options.toBundle());
                break;
            case R.id.QQLogin:
                // TODO: 2018/2/2 需要进行封装
//                qqlogin();
                break;
            case R.id.register_sure:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}
