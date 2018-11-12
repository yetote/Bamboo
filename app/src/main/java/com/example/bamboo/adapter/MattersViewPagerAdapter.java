package com.example.bamboo.adapter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2018/11/12 17:39
 * @change
 * @chang time
 * @class describe
 */
public class MattersViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> title;
    private ArrayList<Fragment> fragmentArrayList;

    public MattersViewPagerAdapter(FragmentManager fm, ArrayList<String> title, ArrayList<Fragment> fragmentArrayList) {
        super(fm);
        this.fragmentArrayList = fragmentArrayList;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
