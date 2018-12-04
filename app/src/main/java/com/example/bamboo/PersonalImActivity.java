package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.fragment.PersonalMainCollect;
import com.example.bamboo.fragment.PersonalMainContributes;
import com.example.bamboo.fragment.PersonalMainDynamics;
import com.example.bamboo.fragment.PersonalMainPager;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PersonalImActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ImageView bgIv, headIv;
    private TextView userName, userId, followNum, fansNum, synopsis;
    private ArrayList<Fragment> list;
    private ArrayList<String> title;
    private MainViewPagerAdapter adapter;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparentStatusBar(this);
        setContentView(R.layout.activity_personal_im);
        Intent i = getIntent();
        int id = i.getIntExtra("id", -1);
        initView();
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab(), true);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

        toolbar.setTitle("yetote");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

            }
        });
    }

    private void initView() {
        tabLayout = findViewById(R.id.personal_im_tabLayout);
        bgIv = findViewById(R.id.personal_im_bg);
        headIv = findViewById(R.id.personal_im_headImg);
        userName = findViewById(R.id.personal_im_username_tv);
        userId = findViewById(R.id.personal_im_userId_tv);
        followNum = findViewById(R.id.personal_im_followNum_tv);
        fansNum = findViewById(R.id.personal_im_fans_tv);
        synopsis = findViewById(R.id.personal_im_synopsis);
        viewPager = findViewById(R.id.personal_im_viewPager);
        toolbar = findViewById(R.id.personal_im_toolbar);
        appBarLayout = findViewById(R.id.personal_im_appBarLayout);
        list = new ArrayList<>();
        title = new ArrayList<>();
        list.add(new PersonalMainPager());
        list.add(new PersonalMainDynamics());
        list.add(new PersonalMainContributes());
        list.add(new PersonalMainCollect());
        title.add("首页");
        title.add("动态");
        title.add("投稿");
        title.add("收藏");
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), list, title);
    }
}
