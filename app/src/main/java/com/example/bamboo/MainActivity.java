package com.example.bamboo;

import android.os.Bundle;
import android.view.View;

import com.example.bamboo.adapter.MainTabLayoutAdapter;
import com.example.bamboo.fragment.HomePageFragment;
import com.example.bamboo.fragment.MattersFragment;
import com.example.bamboo.fragment.NearFragment;
import com.example.bamboo.fragment.RecommendFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @author yetote
 * @decription 起始Activity
 */
public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private MainTabLayoutAdapter adapter;
    private ArrayList<Fragment> fmList;
    private ArrayList<String> titleList;
    private ViewPager viewPager;
    View statusBar;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (statusBar == null) {
                    int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
                    statusBar = getWindow().findViewById(identifier);
                }
                statusBar.setBackgroundResource(R.drawable.toolbar_gradient_background);
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });

        initView();

        tabLayout.addTab(tabLayout.newTab().setText("推荐"), false);
        tabLayout.addTab(tabLayout.newTab().setText("首页"), true);
        tabLayout.addTab(tabLayout.newTab().setText("附近"), false);
        tabLayout.addTab(tabLayout.newTab().setText("动态"), false);

        fmList = new ArrayList<>();
        fmList.add(new RecommendFragment());
        fmList.add(new HomePageFragment());
        fmList.add(new NearFragment());
        fmList.add(new MattersFragment());

        titleList = new ArrayList<>();
        titleList.add("推荐");
        titleList.add("首页");
        titleList.add("附近");
        titleList.add("动态");

        adapter = new MainTabLayoutAdapter(getSupportFragmentManager(), fmList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabLayout = findViewById(R.id.main_tabLayout);
        viewPager = findViewById(R.id.main_viewPager);
    }

}

