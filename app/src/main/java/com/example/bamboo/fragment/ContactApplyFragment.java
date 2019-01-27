package com.example.bamboo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bamboo.R;
import com.example.bamboo.adapter.ContactApplyAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.AddImBean;
import com.example.bamboo.myinterface.services.UserService;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_CONTACT_UN_APPLIED;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class describe
 * @time 2019/1/25 18:39
 * @change
 * @chang time
 * @class describe
 */
public class ContactApplyFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<AddImBean> list;
    private ContactApplyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_apply, null);
        initView(view);

        MyApplication.retrofit.create(UserService.class)
                .selectAddContactIm(MyApplication.uId, "apply")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addImBeanJsonBean -> {
                    switch (addImBeanJsonBean.getCode()) {
                        case NETWORK_RESULT_OK:
                            list.addAll(addImBeanJsonBean.getBody());
                            adapter.notifyDataSetChanged();
                            break;
                        case NETWORK_CONTACT_UN_APPLIED:
                            Toast.makeText(getActivity(), "没有好友申请信息", Toast.LENGTH_SHORT).show();
                            break;
                        case NETWORK_RESULT_ERR:
                            Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "请求错误" + addImBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.contact_apply_rv);
        list = new ArrayList<>();
        adapter = new ContactApplyAdapter(getActivity(), list);
    }
}
