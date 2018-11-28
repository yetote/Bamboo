package com.example.bamboo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.math.BigDecimal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @author yetote
 * @decription 启动页
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private LottieAnimationView lottieView;
    private Button loginBtn, registerBtn;
    private static final String TAG = "StartActivity";
    private boolean isShowing = false;
    private boolean isLogin = false, isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_start);

        initView();

        lottieView.addAnimatorUpdateListener(animation -> {
            BigDecimal b = new BigDecimal(lottieView.getProgress());
            float f = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (!isShowing && f == 0.5f) {
                Log.e(TAG, "onAnimationUpdate: " + f);
                lottieView.pauseAnimation();
                showAnimation();
                isShowing = true;
            }
        });

        setClick();

    }

    private void setClick() {
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    private void initView() {
        lottieView = findViewById(R.id.start_lottie_view);
        loginBtn = findViewById(R.id.start_login);
        registerBtn = findViewById(R.id.start_register);
    }

    void showAnimation() {
        loginBtn.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.VISIBLE);
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(loginBtn, "alpha", 0f, 1f);
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(registerBtn, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        Log.e(TAG, "showAnimation: " + animatorSet.getDuration());
        animatorSet.playTogether(oa1, oa2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                loginBtn.setVisibility(View.VISIBLE);
                registerBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    void hideAnimation(final View v) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        oa.setDuration(2000);
        oa.start();
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                v.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.start_login:
                if (!isLogin && !isRegister) {
                    hideAnimation(registerBtn);
                    i.setClass(this, MainActivity.class);
                    isLogin = true;
                } else {
                    Toast.makeText(this, "点这么多次干啥", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.start_register:
                if (!isRegister && !isLogin) {
                    hideAnimation(loginBtn);
                    i.setClass(this, RegisterActivity.class);
                    isRegister = true;
                } else {
                    Toast.makeText(this, "丫的找事是吧", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        lottieView.resumeAnimation();
        lottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(i);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


}
