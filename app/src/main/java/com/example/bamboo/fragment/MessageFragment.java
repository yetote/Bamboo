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
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 消息页面
 * @time 2018/12/24 17:24
 * @change
 * @chang time
 * @class describe
 */
public class MessageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, null);
        return v;
    }
}
