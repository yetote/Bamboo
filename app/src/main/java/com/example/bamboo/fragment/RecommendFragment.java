package com.example.bamboo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bamboo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public class RecommendFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend, null, false);
        return v;
    }
}
