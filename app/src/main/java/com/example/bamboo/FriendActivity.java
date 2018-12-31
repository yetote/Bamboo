package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.StatusBarUtils;

import java.util.ArrayList;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 好友
 * @time 2018/11/28 15:43
 * @change
 * @chang time
 * @class describe
 */
public class FriendActivity extends AppCompatActivity {
    private RecyclerView rv;
    private FriendAdapter adapter;
    private ArrayList<PersonalBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.changedStatusBar(this);
        setContentView(R.layout.activity_friend);
        initView();

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(this, list);
        rv.setAdapter(adapter);
        adapter.setClickListener(new RecyclerViewOnClickListener() {
            @Override
            public void onClick(Object obj, int position) {

            }
        });
    }

    private void initView() {
        rv = findViewById(R.id.friend_rv);
        list = new ArrayList<>();
        list.add(new PersonalBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544006393996&di=06f232e186c7ad846f977ec2b36c7484&imgtype=0&src=http%3A%2F%2Fbmp.skxox.com%2F201703%2F27%2Fxz123456.162643.jpg", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3484494144,556561024&fm=26&gp=0.jpg", "yetote", 13004265, 32, 40, "vip3", "一个非常无聊的人"));
    }
}
