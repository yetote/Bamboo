package com.example.bamboo;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;

import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.fragment.HomePageFragment;
import com.example.bamboo.fragment.MattersFragment;
import com.example.bamboo.fragment.NearFragment;
import com.example.bamboo.fragment.RecommendFragment;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtils.changedStatusBar(this);

        initView();

        tabLayout.addTab(tabLayout.newTab().setText("推荐"), false);
        tabLayout.addTab(tabLayout.newTab().setText("首页"), true);
        tabLayout.addTab(tabLayout.newTab().setText("附近"), false);
        tabLayout.addTab(tabLayout.newTab().setText("动态"), false);

        fmList = new ArrayList<>();
        fmList.add(new RecommendFragment());
        fmList.add(new HomePageFragment());
        fmList.add(new NearFragment());
        fmList.add(mattersFragment);

        titleList = new ArrayList<>();
        titleList.add("推荐");
        titleList.add("首页");
        titleList.add("附近");
        titleList.add("动态");

        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fmList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(2);
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
    }

    private void initView() {
        tabLayout = findViewById(R.id.main_tabLayout);
        viewPager = findViewById(R.id.main_viewPager);
        mattersFragment = new MattersFragment();
        constraintLayout = findViewById(R.id.homePager_constraintLayout);

        constraintStart = new ConstraintSet();
        constraintStart.clone(this, R.layout.activity_main_finish);

        constraintReply = new ConstraintSet();
        constraintReply.clone(constraintLayout);
    }

}

