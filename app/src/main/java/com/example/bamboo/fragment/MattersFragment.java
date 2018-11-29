package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bamboo.R;
import com.example.bamboo.SelectTagActivity;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.adapter.MattersViewPagerAdapter;
import com.example.bamboo.myinterface.MattersInterface;
import com.example.bamboo.opengl.objects.SelectTag;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class describe
 * @time 2018/10/19 17:25
 * @change
 * @chang time
 * @class describe
 */
public class MattersFragment extends Fragment {
    private SearchView searchView;
    private TabLayout tabLayout;
    private MattersViewPagerAdapter adapter;
    private ViewPager viewPager;
    private ArrayList<Fragment> list;
    private ArrayList<String> title;
    private Button addFollowBtn;
    public static final int REQUEST_TAG = 1;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_matters, null, false);

        initView(v);

        toolbar.inflateMenu(R.menu.matters_toolbar_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.matters_toolbar_write:
                    Toast.makeText(getActivity(), "未完成，敬请期待", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        });


        tabLayout.addTab(tabLayout.newTab().setText("附近"));
        tabLayout.addTab(tabLayout.newTab().setText("关注"), true);
        tabLayout.addTab(tabLayout.newTab().setText("通知"));

        list.add(new MattersNearFragment());
        list.add(new MattersFollowFragment());
        list.add(new MattersDynamicFragment());

        title.add("附近");
        title.add("关注");
        title.add("通知");
        /*
         * 卡顿原因
         * 原因：在给ViewPager的设置adapter时
         * 传递的FragmentManager应该是getChildFragmentManager()，
         * 而不是getActivity().getSupportFragmentManager()。
         * 原文地址 https://blog.csdn.net/Pusherson/article/details/82837489
         * */
        adapter = new MattersViewPagerAdapter(getChildFragmentManager(), title, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);


        return v;
    }

    private void initView(View v) {
        searchView = v.findViewById(R.id.matters_searchView);
        tabLayout = v.findViewById(R.id.matters_tabLayout);
        viewPager = v.findViewById(R.id.matters_viewPager);
        list = new ArrayList<>();
        title = new ArrayList<>();
        toolbar = v.findViewById(R.id.matters_toolbar);
    }

}
