package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.fragment.RecommendVideoDetailedVideoDiscussFragment;
import com.example.bamboo.fragment.RecommendVideoDetailedVideoImFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo
 * @class 推荐视频详情
 * @time 2018/11/28 15:41
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoDetailedActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainViewPagerAdapter adapter;
    private ArrayList<Fragment> list;
    private ArrayList<String> title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_video_detailed);
        initView();
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabIndicatorFullWidth(false);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabLayout = findViewById(R.id.recommend_video_detailed_tab);
        viewPager = findViewById(R.id.recommend_video_detailed_vp);
        list = new ArrayList<>();
        title = new ArrayList<>();
        list.add(new RecommendVideoDetailedVideoImFragment());
        list.add(new RecommendVideoDetailedVideoDiscussFragment());
        title.add("简介");
        title.add("评论");
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), list, title);
    }
}
