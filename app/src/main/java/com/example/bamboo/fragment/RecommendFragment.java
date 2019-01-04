package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bamboo.R;
import com.example.bamboo.SelectTagActivity;
import com.example.bamboo.adapter.MainViewPagerAdapter;
import com.example.bamboo.myinterface.MattersInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * @author yetote QQ:503779938
 * @name RecommendFragment
 * @class name：com.example.bamboo.fragment
 * @class 推荐页
 * @time 2018/10/19 16:58
 * @change
 * @chang time
 * @class describe
 */
public class RecommendFragment extends Fragment implements View.OnClickListener, MattersInterface {
    ViewPager viewPager;
    FloatingActionButton addBtn;
    private static final String TAG = "RecommendFragment";
    private ArrayList<Fragment> fragList;
    private ArrayList<String> title;
    private MainViewPagerAdapter adapter;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend, null, false);

        init(v);

        tabLayout.addTab(tabLayout.newTab().setText("直播"));
        tabLayout.addTab(tabLayout.newTab().setText("视频"));
        adapter = new MainViewPagerAdapter(getChildFragmentManager(), fragList, title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        onClick();

        return v;
    }

    private void onClick() {
        addBtn.setOnClickListener(this);
    }

    private void init(View v) {
        viewPager = v.findViewById(R.id.recommend_viewPager);
        addBtn = v.findViewById(R.id.recommend_addBtn);
        tabLayout = v.findViewById(R.id.recommend_tabLayout);

        fragList = new ArrayList<>();
        fragList.add(new RecommendLivingFragment());
        fragList.add(new RecommendVideoFragment());

        title = new ArrayList<>();
        title.add("直播");
        title.add("视频");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recommend_addBtn:
                Intent i = new Intent();
                i.setClass(getActivity(), SelectTagActivity.class);
                i.putExtra("user_id", "1");
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void selectedTag(Bundle bundle) {
        Log.e(TAG, "selectedTag: " + bundle.getInt("count"));
    }
}
