package com.example.bamboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bamboo.adapter.FriendAdapter;
import com.example.bamboo.adapter.SearchUserApapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.OnAddFriendInterface;
import com.example.bamboo.myinterface.OnRecyclerViewItemViewClickListener;
import com.example.bamboo.myinterface.services.UserService;
import com.example.bamboo.util.HuanXinHelper;
import com.example.bamboo.util.NetworkUtil;

import java.util.ArrayList;

import static com.example.bamboo.util.NetworkUtil.NETWORK_CONTACT_ALREADY;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

public class AddContactActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ArrayList<PersonalBean> list;
    private SearchUserApapter adapter;
    public static final int HANDLER_ADD_CODE = 1;
    private static final String TAG = "AddContactActivity";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_ADD_CODE:
                    Bundle bundle = (Bundle) msg.obj;
                    if (bundle.getBoolean("state")) {
                        Toast.makeText(AddContactActivity.this, "添加成功,等待响应中", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddContactActivity.this, "error" + bundle.getInt("error"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);
        initView();

        callBack();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MyApplication.retrofit.create(UserService.class)
                        .search(query)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(personalBeanJsonBean -> {
                            Log.e(TAG, "onQueryTextSubmit: " + personalBeanJsonBean.getCode());
                            if (personalBeanJsonBean.getCode() == NETWORK_RESULT_OK) {
                                list.addAll(personalBeanJsonBean.getBody());
                                adapter.notifyDataSetChanged();
                            } else if (personalBeanJsonBean.getCode() == NetworkUtil.NETWORK_LOGIN_ERR_UN_USER) {
                                Toast.makeText(AddContactActivity.this, "未找到该用户", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddContactActivity.this, "未知错误" + personalBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        adapter.setClickListener(position -> {
            String contactName = list.get(position).getuName();
            MyApplication.retrofit.create(UserService.class)
                    .addContact(MyApplication.uName, 0, "waiting", contactName)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(integerJsonBean -> {
                        switch (integerJsonBean.getCode()) {
                            case NETWORK_CONTACT_ALREADY:
                                Toast.makeText(AddContactActivity.this, "好友已存在", Toast.LENGTH_SHORT).show();
                                break;
                            case NETWORK_RESULT_ERR:
                                Toast.makeText(AddContactActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                break;
                            case NETWORK_RESULT_OK:
                                Toast.makeText(AddContactActivity.this, "已添加，等待响应", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(AddContactActivity.this, "未知错误" + integerJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    });
//                HuanXinHelper.addFriend(contactName, "我是" + MyApplication.uName);
        });
    }

    private void callBack() {
        ((MyApplication) getApplication()).getCallBackUtils().setAddFriendInterface(new OnAddFriendInterface() {
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
        list = new ArrayList<PersonalBean>();
        adapter = new SearchUserApapter(list, this);
    }
}
