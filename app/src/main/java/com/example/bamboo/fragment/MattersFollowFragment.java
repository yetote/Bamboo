package com.example.bamboo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.adapter.MattersFollowAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.MattersFollowBean;
import com.example.bamboo.myinterface.services.MattersService;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_ERR;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class describe
 * @time 2018/11/12 17:20
 * @change
 * @chang time
 * @class describe
 */
public class MattersFollowFragment extends Fragment {
    private MattersFollowAdapter adapter;
    private RecyclerView rv;
    private ArrayList<MattersFollowBean> list;
    private ArrayList<String> imgList, imgList1, imgList2, imgList3, imgList5, imgList7;
    private static final String TAG = "MattersFollowFragment";
    private boolean isScrolling = false;
    private boolean isLoadData = false;
    private SwipeRefreshLayout swl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_matters_follow, null);
        initView(v);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new MattersFollowAdapter(list, getActivity());
        rv.setAdapter(adapter);
        adapter.setListener((obj, position, tag) -> Toast.makeText(MattersFollowFragment.this.getActivity(), position + "", Toast.LENGTH_SHORT).show());
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isScrolling = true;
                    Glide.with(getContext()).pauseRequests();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isScrolling) {
                        Glide.with(getContext()).resumeRequests();
                    }
                    isScrolling = false;
                }
            }
        });
        isLoadData = true;
        swl.setProgressViewOffset(false, 50, 100);
        swl.setSize(SwipeRefreshLayout.LARGE);
        swl.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swl.setOnRefreshListener(() -> {
            if (MyApplication.isLogin) {
                MyApplication.retrofit.create(MattersService.class)
                        .follow(MyApplication.uId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<JsonBean<MattersFollowBean>>() {
                    @Override
                    public void accept(JsonBean<MattersFollowBean> mattersFollowBeanJsonBean) throws Exception {
                        switch (mattersFollowBeanJsonBean.getCode()) {
                            case NETWORK_RESULT_OK:
                                list.addAll(mattersFollowBeanJsonBean.getBody());
                                break;
                            case NETWORK_RESULT_ERR:
                                Toast.makeText(getActivity(), "请求错误", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "未知错误" + mattersFollowBeanJsonBean.getCode(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                swl.setRefreshing(false);
            } else {
                Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    private void initView(View v) {
        rv = v.findViewById(R.id.matters_follow_rv);
        swl = v.findViewById(R.id.matters_follow_swl);
        list = new ArrayList<>();
      }

}
