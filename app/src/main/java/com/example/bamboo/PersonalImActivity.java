package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.fragment.PersonalMainCollect;
import com.example.bamboo.fragment.PersonalMainContributes;
import com.example.bamboo.fragment.PersonalMainDynamics;
import com.example.bamboo.fragment.PersonalMainPager;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

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
    private List<PersonalBean> dataList;
    private static final String TAG = "PersonalImActivity";

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

        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i1) -> {
            if (-i1 >= appBarLayout.getTotalScrollRange()*0.75) {
                toolbar.setTitle(dataList.get(0).getName());
            }else {
                toolbar.setTitle("");
            }
        });
        setData(dataList);
    }

    private void setData(List<PersonalBean> dataList) {
        Glide.with(this).load(dataList.get(0).getBg()).into(bgIv);
        Glide.with(this).load(dataList.get(0).getHeadImg()).into(headIv);
        userName.setText(dataList.get(0).getName());
        int drawableId = -1;
        switch (dataList.get(0).getLevel()) {
            case 0:
                break;
            case 1:
                drawableId = R.mipmap.lv_one;
                break;
            case 2:
                drawableId = R.mipmap.lv_two;
                break;
            case 3:
                drawableId = R.mipmap.lv_three;
                break;
            default:
                break;
        }
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, 70, 70);
        userId.setCompoundDrawables(drawable, null, null, null);
        userId.setText(dataList.get(0).getUid() + "");
        followNum.setText(dataList.get(0).getFollowNum() + "关注");
        fansNum.setText(dataList.get(0).getFansNum() + "粉丝");
        synopsis.setText(dataList.get(0).getSynopsis());
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
        dataList = new ArrayList<>();
        dataList.add(new PersonalBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544006393996&di=06f232e186c7ad846f977ec2b36c7484&imgtype=0&src=http%3A%2F%2Fbmp.skxox.com%2F201703%2F27%2Fxz123456.162643.jpg", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3484494144,556561024&fm=26&gp=0.jpg", "yetote", 13004265, 32, 40, 3, "一个非常无聊的人"));
    }
}
