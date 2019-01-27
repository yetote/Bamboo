package com.example.bamboo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.fragment.ContactApplyFragment;
import com.example.bamboo.fragment.ContactReceiveFragment;
import com.example.bamboo.model.AddImBean;
import com.example.bamboo.myinterface.services.UserService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_CONTACT_UN_APPLIED;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

public class ApplyContactActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> title;
    private MainViewPagerAdapter adapter;
    private static final String TAG = "ApplyContactActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_contact);
        initView();

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
//        MyApplication.retrofit.create(UserService.class)
//                .selectAddContactIm(MyApplication.uName, "all")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(addImBeanJsonBean -> {
//                    switch (addImBeanJsonBean.getCode()) {
//                        case NETWORK_RESULT_OK:
//                            list.addAll(addImBeanJsonBean.getBody());
//                            adapter.notifyDataSetChanged();
//                            break;
//                        case NETWORK_CONTACT_UN_APPLIED:
//                            Toast.makeText(ApplyContactActivity.this, "没有好友申请信息", Toast.LENGTH_SHORT).show();
//                            break;
//                        case NETWORK_RESULT_ERR:
//                            Toast.makeText(ApplyContactActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
//                            break;
//                        default:
//                            Toast.makeText(ApplyContactActivity.this, "请求错误" + addImBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                });

    }

    private void initView() {
        tabLayout = findViewById(R.id.apply_contact_tab);
        viewPager = findViewById(R.id.apply_contact_vp);
        fragments = new ArrayList<>();
        title = new ArrayList<>();
        fragments.add(new ContactApplyFragment());
        fragments.add(new ContactReceiveFragment());
        title.add("我的申请");
        title.add("我的邀请");
        adapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments, title);
    }
}
