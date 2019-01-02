package com.example.bamboo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.OnAddFriendInterface;
import com.example.bamboo.myview.RecodeButton;
import com.example.bamboo.util.CallBackUtils;
import com.example.bamboo.util.HuanXinHelper;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TestActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<PersonalBean> list;
    private FriendAdapter adapter;
    public static final int HANDLER_ADD_CODE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_ADD_CODE:
                    Bundle bundle = (Bundle) msg.obj;
                    if (bundle.getBoolean("state")) {
                        Toast.makeText(TestActivity.this, "添加成功,等待响应中", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TestActivity.this, "error" + bundle.getInt("error"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        initView();

        callBack();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HuanXinHelper.addFriend(query, "我是" + MyApplication.uName);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void callBack() {
        ((MyApplication)getApplication()).getCallBackUtils().setAddFriendInterface(new OnAddFriendInterface() {
            @Override
            public void add(boolean isSuccess, int code) {
                Bundle bundle = new Bundle();
                bundle.putInt("error", code);
                bundle.putBoolean("state", isSuccess);
                Message msg = new Message();
                msg.what = HANDLER_ADD_CODE;
                msg.obj = bundle;
                handler.sendMessage(msg);
            }
        });
    }

    private void initView() {
        searchView = findViewById(R.id.add_friend_search_view);
        recyclerView = findViewById(R.id.add_friend_rv);
        list = new ArrayList<>();
        adapter = new FriendAdapter(this, list);
    }
}
