package com.example.bamboo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bamboo.R;
import com.example.bamboo.SelectTagActivity;
import com.example.bamboo.adapter.RecommendAdapter;
import com.example.bamboo.model.RecommendBean;
import com.example.bamboo.myinterface.MattersInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
    ArrayList<RecommendBean> list;
    RecyclerView rv;
    RecommendAdapter adapter;
    FloatingActionButton addBtn;
    private static final String TAG = "RecommendFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend, null, false);
        init(v);
        onClick();
        return v;
    }

    private void onClick() {
        addBtn.setOnClickListener(this);
    }

    private void init(View v) {
        list = new ArrayList<>();
        adapter = new RecommendAdapter(getActivity(), list);
        rv = v.findViewById(R.id.recommend_rv);
        addBtn = v.findViewById(R.id.recommend_addBtn);

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
