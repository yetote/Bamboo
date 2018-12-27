package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.CheckUtils;
import com.example.bamboo.util.HuanXinHelper;
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
    private EditText telEdit, codeEdit, pwdEdit;

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
        telEdit = findViewById(R.id.register_tel_et);
        codeEdit = findViewById(R.id.register_verifyCode_et);
        pwdEdit = findViewById(R.id.register_pwd_et);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_fab:
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                startActivity(new Intent(this, PwdLoginActivity.class), options.toBundle());
                finish();
                break;
            case R.id.QQLogin:
                // TODO: 2018/2/2 需要进行封装
//                qqlogin();
                break;
            case R.id.register_sure:
                String uNameText = telEdit.getText().toString();
                String pwdText = pwdEdit.getText().toString();

                if (CheckUtils.checkNull(uNameText, pwdText)) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (HuanXinHelper.register(uNameText, pwdText) == 0) {
                        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                        MyApplication.isLogin = true;
                        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("is_login", true);
                        editor.apply();

                        if (MyApplication.isFirst) {
                            Intent i = new Intent();
                            i.putExtra("u_name", uNameText);
                            i.setClass(this, MainActivity.class);
                            startActivity(i);
                        } else {
                            CallBackUtils.setLogin(true, uNameText);
                        }
                        finish();

                    } else {
                        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
