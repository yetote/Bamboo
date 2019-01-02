package com.example.bamboo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bamboo.application.MyApplication;
import com.example.bamboo.myinterface.OnLoginInterface;
import com.example.bamboo.myinterface.ffmpeg.OnLoadListener;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.CheckUtils;
import com.example.bamboo.util.HuanXinHelper;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 密码登录
 * @time 2018/11/28 15:43
 * @change
 * @chang time
 * @class describe
 */
public class PwdLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab;
    private CardView cardView;
    private EditText tel, pwd;
    private Button sure;
    private TextView toVerifyCodeLogin;
    private static final String TAG = "PwdLoginActivity";
    private OnLoginInterface loginInterface;
    public static final int HANDLER_LOGIN_CODE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LOGIN_CODE:
                    Bundle bundle = (Bundle) msg.obj;
                    boolean loginState = bundle.getBoolean("login_state");
                    if (loginState) {
                        String uName = bundle.getString("u_name");
                        Toast.makeText(PwdLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        MyApplication.isLogin = true;
//                        SharedPreferences sp = getSharedPreferences("sp", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putBoolean("is_login", true);
//                        editor.apply();
                        MyApplication.uName = uName;
                        Intent i = new Intent();
                        i.putExtra("u_name", uName);
                        PwdLoginActivity.this.setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Toast.makeText(PwdLoginActivity.this, "登录失败,错误码" + bundle.getInt("error_code"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void setLoginInterface(OnLoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setExitTransition(null);
        getWindow().setEnterTransition(null);

        setContentView(R.layout.activity_login_pwd);

        StatusBarUtils.changedStatusBar(this);

        initViews();

        // TODO: 2018/2/1 重复代码 待抽取
        startAnimation();

        onClick();
        callBack();
    }

    private void callBack() {
        ((MyApplication)getApplication()).getCallBackUtils().setLoginInterface((isLogin, uName, error) -> {
            Message msg = new Message();
            msg.what = HANDLER_LOGIN_CODE;
            Bundle bundle = new Bundle();
            bundle.putBoolean("login_state", isLogin);
            bundle.putString("u_name", uName);
            bundle.putInt("error_code", error);
            msg.obj = bundle;
            handler.sendMessage(msg);

        });
    }


    private void onClick() {
        fab.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    private void initViews() {
        fab = findViewById(R.id.login_pwd_fab);
        cardView = findViewById(R.id.login_pwd_cardview);
        tel = findViewById(R.id.login_pwd_tel_et);
        pwd = findViewById(R.id.login_pwd_pwd_et);
        sure = findViewById(R.id.login_pwd_sure);
        toVerifyCodeLogin = findViewById(R.id.login_pwd_to_verifyCodeLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_pwd_fab:
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                startActivity(new Intent(this, CodeLoginActivity.class), options.toBundle());
                finish();
                break;
            case R.id.login_pwd_sure:
                String telText = tel.getText().toString();
                String pwdText = pwd.getText().toString();
                if (CheckUtils.checkNull(telText, pwdText)) {
                    Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    HuanXinHelper.login(telText, pwdText);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开启Activity交互的动画，狭义可以理解为转场动画
     */
    private void startAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            /**
             * 动画开始
             * @param transition 动画
             */
            @Override
            public void onTransitionStart(Transition transition) {
                cardView.setVisibility(View.GONE);
                Log.e(TAG, "onTransitionStart: " + "asa");
            }

            /** 动画结束
             * @param transition 动画
             */
            @Override
            public void onTransitionEnd(Transition transition) {
                animateRevelShow();
                transition.removeListener(this);
            }

            /**
             * 中途取消动画
             * @param transition 动画
             */
            @Override
            public void onTransitionCancel(Transition transition) {

            }

            /**
             * 动画暂停  无
             * @param transition 动画
             */
            @Override
            public void onTransitionPause(Transition transition) {

            }

            /**
             * 动画重启 无
             * @param transition 动画
             */
            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    /**
     * 开启水波纹动画
     */
    private void animateRevelShow() {
        Log.e(TAG, "animateRevelShow: " + "111");
        int centerX = cardView.getWidth() / 2;
        int centerY = 0;
        float endRadius = (float) Math.sqrt(centerX * centerX + cardView.getHeight() * cardView.getHeight());
        Animator animation = ViewAnimationUtils.createCircularReveal(cardView, centerX, centerY, 0, endRadius);
        animation.setDuration(500);
        //AccelerateInterpolator插值器为线性加速
        animation.setInterpolator(new AccelerateInterpolator());
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cardView.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        animation.start();
    }

    /**
     * 关闭水波纹动画
     */
    private void animationRevelClose() {
        int centerX = cardView.getWidth() / 2;
        int centerY = 0;
        float startRadius = (float) Math.sqrt(centerX * centerX + cardView.getHeight() * cardView.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(cardView, centerX, centerY, startRadius, fab.getWidth() / 2);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cardView.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                PwdLoginActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    @Override
    public void onBackPressed() {
        //按下返回键调用关闭水波纹动画
        animationRevelClose();
    }
}
