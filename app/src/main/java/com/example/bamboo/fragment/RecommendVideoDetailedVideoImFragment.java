package com.example.bamboo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bamboo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 推荐视频详情信息
 * @time 2019/1/8 9:47
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoDetailedVideoImFragment extends Fragment {
    private Button priseBtn, booingBtn, collectBtn, coinBtn, shareBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_video_detailed_video_im, null);
        initView(v);
        return v;
    }

    private void initView(View v) {
        priseBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_praiseBtn);
        booingBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_booingBtn);
        collectBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_collectBtn);
        coinBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_coinBtn);
        shareBtn = v.findViewById(R.id.fragment_recommend_video_detailed_im_shareBtn);

        changeBtnDrawable(priseBtn, R.drawable.prise);
        changeBtnDrawable(booingBtn, R.drawable.booing);
        changeBtnDrawable(collectBtn, R.drawable.collect);
        changeBtnDrawable(coinBtn, R.drawable.coin);
        changeBtnDrawable(shareBtn, R.drawable.share);
    }

    private void changeBtnDrawable(Button btn, int resourceId) {
        Drawable drawable = getActivity().getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, 50, 50);
        btn.setCompoundDrawables(null, drawable, null, null);
    }
}
