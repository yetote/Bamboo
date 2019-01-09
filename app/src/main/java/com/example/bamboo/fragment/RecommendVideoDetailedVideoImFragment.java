package com.example.bamboo.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bamboo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 推荐视频详情信息
 * @time 2019/1/8 9:47
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoDetailedVideoImFragment extends Fragment {
    private Button priseBtn, booingBtn, collectBtn, coinBtn, shareBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_video_detailed_video_im, null);
        initView(v);

        click();
        return v;
    }

    private void click() {
        priseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", 0, -10, 0);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.prised));
                    }
                });
            }
        });


        booingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", 0, 10, 0);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.booed));
                    }
                });
            }
        });
        coinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "rotationY", 0, 180, 0);
                oa1.setDuration(1000);
                oa1.start();
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });
            }
        });

        collectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "scaleX", 1, 0.8f, 1.2f, 1);
                ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "scaleY", 1, 0.8f, 1.2f, 1);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(1000);
                animatorSet.playTogether(oa1, oa2);
                animatorSet.start();
                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setBackground(getResources().getDrawable(R.drawable.collected));
                    }
                });
            }
        });
    }

    private void initView(View v) {
        priseBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_praiseBtn);
        booingBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_booingBtn);
        collectBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_collectBtn);
        coinBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_coinBtn);
        shareBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_shareBtn);

//        changeBtnDrawable(priseBtn, R.drawable.prise);
//        changeBtnDrawable(booingBtn, R.drawable.booing);
//        changeBtnDrawable(collectBtn, R.drawable.collect);
//        changeBtnDrawable(coinBtn, R.drawable.coin);
//        changeBtnDrawable(shareBtn, R.drawable.share);


    }

    private void changeBtnDrawable(Button btn, int resourceId) {
        Drawable drawable = getActivity().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, 50, 50);
        btn.setCompoundDrawables(null, drawable, null, null);
    }
}
