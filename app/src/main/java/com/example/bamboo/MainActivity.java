package com.example.bamboo;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.fragment.HomePageFragment;
import com.example.bamboo.fragment.MattersFragment;
import com.example.bamboo.fragment.MessageFragment;
import com.example.bamboo.fragment.RecommendFragment;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.services.UserService;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author yetote
 * @decription 起始Activity
 */
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private MainViewPagerAdapter adapter;
    private ArrayList<Fragment> fmList;
    private ArrayList<String> titleList;
    private ViewPager viewPager;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintStart;
    private ConstraintSet constraintReply;
    private boolean isFirst = true;
    private static final String TAG = "MainActivity";
    private MattersFragment mattersFragment;
    private ImageView headBcIv;
    private TextView nameTv, describeTv, mattersNum, followNum, fansNum;
    private NavigationView headerView;
    private CircleImageView headIv;
    public static final int LOGIN_CODE = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.changedStatusBar(this);

        initView();

        if (MyApplication.isFirst && MyApplication.isLogin) {
            Intent i = getIntent();
            String uName = i.getStringExtra("u_name");
            nameTv.setText(uName);
            Glide.with(MainActivity.this).load(R.drawable.bc).into(headBcIv);
            Glide.with(MainActivity.this).load(R.drawable.boss).into(headIv);
        } else {
            Glide.with(MainActivity.this).load(R.drawable.question_mark).into(headIv);
            Glide.with(MainActivity.this).load(R.drawable.default_bc).into(headBcIv);
            nameTv.setText("阁下何人，报上名来");
        }

        tabLayout.addTab(tabLayout.newTab().setText("推荐"), false);
        tabLayout.addTab(tabLayout.newTab().setText("首页"), true);
        tabLayout.addTab(tabLayout.newTab().setText("消息"), false);
        tabLayout.addTab(tabLayout.newTab().setText("动态"), false);

        fmList = new ArrayList<>();
        fmList.add(new RecommendFragment());
        fmList.add(new HomePageFragment());
        fmList.add(new MessageFragment());
        fmList.add(mattersFragment);

        titleList = new ArrayList<>();
        titleList.add("推荐");
        titleList.add("首页");
        titleList.add("消息");
        titleList.add("动态");

        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fmList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

        if (isFirst) {
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintStart.applyTo(constraintLayout);
            isFirst = false;
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e(TAG, "onTabSelected: " + tab.getPosition());
                if (tab.getPosition() == 1) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintStart.applyTo(constraintLayout);
                } else {
                    if (tab.getPosition() == 3) {
//                        mattersFragment.
                    }
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintReply.applyTo(constraintLayout);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        nameTv.setText("12121");
        onClick();

        callBack();
    }

    private void callBack() {
    }

    private void onClick() {
        headerView.getHeaderView(0).setOnClickListener(v -> {
            if (MyApplication.isLogin) {
                Intent i = new Intent();
                i.putExtra("u_name", MyApplication.uName);
                i.setClass(MainActivity.this, PersonalImActivity.class);
                startActivity(i);
            } else {
//                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                startActivityForResult(new Intent(MainActivity.this, PwdLoginActivity.class), LOGIN_CODE);
            }
        });
    }

    private void initView() {
        tabLayout = findViewById(R.id.main_tabLayout);
        viewPager = findViewById(R.id.main_viewPager);
        mattersFragment = new MattersFragment();
        constraintLayout = findViewById(R.id.homePager_constraintLayout);
        headerView = findViewById(R.id.main_navigationView);

        constraintStart = new ConstraintSet();
        constraintStart.clone(this, R.layout.activity_main_finish);

        constraintReply = new ConstraintSet();
        constraintReply.clone(constraintLayout);

        headIv = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_headIv);
        headBcIv = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_bc);
        nameTv = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_name);
        describeTv = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_describe);
        mattersNum = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_matter_num);
        fansNum = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_fans_num);
        followNum = headerView.getHeaderView(0).findViewById(R.id.drawerlayout_head_follow_num);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_CODE:
                if (data != null) {
                    String username = data.getStringExtra("u_name");
                    MyApplication.retrofit.create(UserService.class)
                            .userIm(username)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(personalBeanJsonBean -> {
                                nameTv.setText(personalBeanJsonBean.getBody().get(0).getuName());
                                Glide.with(MainActivity.this).load(personalBeanJsonBean.getBody().get(0).getuBg()).into(headBcIv);
                                Glide.with(MainActivity.this).load(personalBeanJsonBean.getBody().get(0).getuHeader()).into(headIv);
                                describeTv.setText(personalBeanJsonBean.getBody().get(0).getuSynopsis());
                                fansNum.setText(personalBeanJsonBean.getBody().get(0).getuFans()+"");
                                followNum.setText(personalBeanJsonBean.getBody().get(0).getuFollow()+"");
                                mattersNum.setText(0 + "");
                            });
                }
                break;
        }
    }
}

