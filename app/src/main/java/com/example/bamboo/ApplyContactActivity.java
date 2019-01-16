package com.example.bamboo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bamboo.adapter.ContactAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.AddImBean;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.myinterface.OnRecyclerViewItemViewClickListener;
import com.example.bamboo.myinterface.services.UserService;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_CONTACT_UN_APPLIED;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

public class ApplyContactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<AddImBean> list;
    private ContactAdapter adapter;
    private static final String TAG = "ApplyContactActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_contact);
        initView();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        MyApplication.retrofit.create(UserService.class)
                .selectAddContactIm(MyApplication.uName, "all")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addImBeanJsonBean -> {
                    switch (addImBeanJsonBean.getCode()) {
                        case NETWORK_RESULT_OK:
                            list.addAll(addImBeanJsonBean.getBody());
                            adapter.notifyDataSetChanged();
                            break;
                        case NETWORK_CONTACT_UN_APPLIED:
                            Toast.makeText(ApplyContactActivity.this, "没有好友申请信息", Toast.LENGTH_SHORT).show();
                            break;
                        case NETWORK_RESULT_ERR:
                            Toast.makeText(ApplyContactActivity.this, "请求错误", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(ApplyContactActivity.this, "请求错误" + addImBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        adapter.setItemViewClickListener(position -> {
            int id = list.get(position).getId();
            String user = list.get(position).getUsername();
            String contact = list.get(position).getContactname();
            Log.e(TAG, "onClick: " + id + user + contact);
            MyApplication.retrofit.create(UserService.class)
                    .changeAddState(id, "apply", user, contact)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(integerJsonBean -> {
                        if (integerJsonBean.getCode() == NETWORK_RESULT_OK) {
                            Toast.makeText(ApplyContactActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ApplyContactActivity.this, "添加失败" + integerJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.apply_contact_rv);
        list = new ArrayList<>();
        adapter = new ContactAdapter(this, list);
    }
}
