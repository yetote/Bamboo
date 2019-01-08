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
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 推荐视频详情评论
 * @time 2019/1/8 9:47
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoDetailedVideoDiscussFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_video_detailed_video_discuss, null);
        return v;
    }
}
