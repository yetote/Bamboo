package com.example.bamboo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.HuanXinHelper;
import com.example.bamboo.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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
    private Toolbar toolbar;
    private Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.changedStatusBar(this);
        setContentView(R.layout.activity_friend);
        initView();

        toolbar.inflateMenu(R.menu.friend_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.friend_menu_addFriend:
                        Intent i = new Intent();
                        i.setClass(FriendActivity.this, AddContactActivity.class);
                        startActivity(i);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        String uName = getIntent().getStringExtra("u_name");

        HuanXinHelper.selectFriendList();

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(this, list);
        rv.setAdapter(adapter);
        adapter.setClickListener(new RecyclerViewOnClickListener() {
            @Override
            public void onClick(Object obj, int position, Object tag) {
                Intent i = new Intent();
                i.putExtra("u_name", (String) (obj + ""));
                i.putExtra("u_header", "");
                i.setClass(FriendActivity.this, ChatActivity.class);
                startActivity(i);
            }
        });

        onCallBack();
    }

    private void onCallBack() {
        ((MyApplication) getApplication()).getCallBackUtils().setFriendInterface((friendList, error) -> {
            if (error == 0) {
                Observable.create((ObservableOnSubscribe<List<String>>) emitter -> emitter.onNext(friendList)).subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<String>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<String> templist) {
                                if (templist.size() == 0) {
                                    Toast.makeText(FriendActivity.this, "您好友列表为空，快去添加好友吧", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < templist.size(); i++) {
                                        list.add(new PersonalBean("", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3484494144,556561024&fm=26&gp=0.jpg", templist.get(i), 0, 0, 0, "vip1", ""));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
//                Toast.makeText(FriendActivity.this, "查询失败" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        rv = findViewById(R.id.friend_rv);
        toolbar = findViewById(R.id.friend_toolbar);
        list = new ArrayList<>();
//        list.add(new PersonalBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544006393996&di=06f232e186c7ad846f977ec2b36c7484&imgtype=0&src=http%3A%2F%2Fbmp.skxox.com%2F201703%2F27%2Fxz123456.162643.jpg", "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3484494144,556561024&fm=26&gp=0.jpg", "yetote", 13004265, 32, 40, "vip3", "一个非常无聊的人"));
    }
}
