package com.example.bamboo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

import java.math.BigDecimal;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author yetote
 * @decription 启动页
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private LottieAnimationView lottieView;
    private Button loginBtn, registerBtn;
    private static final String TAG = "StartActivity";
    private boolean isShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        oa1.setDuration(2000);
        oa2.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.playTogether(oa1, oa2);
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
        switch (v.getId()) {
            case R.id.start_login:
                hideAnimation(registerBtn);
                lottieView.resumeAnimation();
                break;
            case R.id.start_register:
                hideAnimation(loginBtn);
                lottieView.resumeAnimation();
                break;
            default:
                break;
        }
    }
}
