package com.example.bamboo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.fragment.PersonalMainCollect;
import com.example.bamboo.fragment.PersonalMainContributes;
import com.example.bamboo.fragment.PersonalMainDynamics;
import com.example.bamboo.fragment.PersonalMainPager;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.services.UserService;
import com.example.bamboo.util.IdentityUtils;
import com.example.bamboo.util.StatusBarUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

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
    public static final int CHANGE_IM_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparentStatusBar(this);
        setContentView(R.layout.activity_personal_im);
//        Log.e(TAG, "onCreate: " + uId);
        initView();

        toolbar.inflateMenu(R.menu.person_im_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.person_im_menu_change:
                        Intent i = new Intent();
                        i.setClass(PersonalImActivity.this, ChangeImActivity.class);
                        startActivityForResult(i, CHANGE_IM_CODE);
//                        startActivity(new Intent());
                        break;
                }
                return false;
            }
        });

        MyApplication.retrofit
                .create(UserService.class)
                .userIm(MyApplication.uId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonBean<PersonalBean>>() {
                    @Override
                    public void accept(JsonBean<PersonalBean> personalBeanJsonBean) throws Exception {
                        if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                            dataList.addAll(personalBeanJsonBean.getBody());
                            setData(dataList);
                        }
                    }
                });

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab(), true);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i1) -> {
            if (-i1 >= appBarLayout.getTotalScrollRange() * 0.75) {
                toolbar.setTitle(dataList.get(0).getuName());
            } else {
                toolbar.setTitle("");
            }
        });

    }

    private void setData(List<PersonalBean> dataList) {
//        if (dataList.get(0).getuBg().equals("null"))
        Glide.with(this).load(dataList.get(0).getuBg()).into(bgIv);
        Glide.with(this).load(dataList.get(0).getuHeader()).into(headIv);
        userName.setText(dataList.get(0).getuName());
        int drawableId = -1;

        Drawable drawable = getResources().getDrawable(IdentityUtils.getIdentityDrawable(dataList.get(0).getuIdentity()));
        drawable.setBounds(0, 0, 50, 50);
        userId.setCompoundDrawables(drawable, null, null, null);
        userId.setText(dataList.get(0).getuId() + "");
        followNum.setText(dataList.get(0).getuFollow() + "关注");
        fansNum.setText(dataList.get(0).getuFans() + "粉丝");
        synopsis.setText(dataList.get(0).getuSynopsis());
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
//        dataList.add(new PersonalBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544006393996&di=06f232e186c7ad846f977ec2b36c7484&imgtype=0&src=http%3A%2F%2Fbmp.skxox.com%2F201703%2F27%2Fxz123456.162643.jpg", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3484494144,556561024&fm=26&gp=0.jpg", "yetote", 13004265, 32, 40, "vip3", "一个非常无聊的人"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANGE_IM_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    MyApplication.uName = data.getStringExtra("name");
                    userName.setText(MyApplication.uName);
                }
                break;
        }
    }
}
