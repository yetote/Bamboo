package com.example.bamboo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.fragment.HomePageFragment;
import com.example.bamboo.fragment.MattersFragment;
import com.example.bamboo.fragment.MessageFragment;
import com.example.bamboo.fragment.NearFragment;
import com.example.bamboo.fragment.RecommendFragment;
import com.example.bamboo.myinterface.OnLoginInterface;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;

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
    private TextView nameTv, describeTv;
    private NavigationView headerView;
    private CircleImageView headIv;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    nameTv.setText(msg.obj.toString());
                    Log.e(TAG, "handleMessage: " + nameTv.getText().toString() + "1");
                    Glide.with(MainActivity.this).load(R.drawable.bc).into(headBcIv);
                    Glide.with(MainActivity.this).load(R.drawable.boss).into(headIv);

                    break;
                default:
                    break;
            }
        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.changedStatusBar(this);

        initView();

        if (MyApplication.isLogin) {
//            Intent i = getIntent();
//            String uName = i.getStringExtra("u_name");
//            nameTv.setText(uName);
//            Glide.with(MainActivity.this).load(R.drawable.bc).into(headBcIv);
//            Glide.with(MainActivity.this).load(R.drawable.boss).into(headIv);
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
        nameTv.setText("12121");
        onClick();

        callBack();
    }

    private void callBack() {
        CallBackUtils.setLoginInterface((isLogin, uName) -> {

            if (isLogin) {
//                nameTv.setText("12121");
                Message msg = new Message();
                MainActivity.this.nameTv.setText(uName);
                msg.what = 0;
                msg.obj = uName;
                handler.sendMessage(msg);
                Log.e(TAG, "callBack: " + Thread.currentThread().getName());
                Toast.makeText(this, uName, Toast.LENGTH_SHORT).show();
             }
        });
    }

    private void onClick() {
        headerView.getHeaderView(0).setOnClickListener(v -> {
            if (MyApplication.isLogin) {
                Intent i = new Intent();
                i.putExtra("id", 123);
                i.setClass(MainActivity.this, PersonalImActivity.class);
                startActivity(i);
            } else {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
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
    }

}

