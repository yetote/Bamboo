package com.example.bamboo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.bamboo.R;
import com.example.bamboo.RecommendVideoArticleActivity;
import com.example.bamboo.RecommendVideoDetailedActivity;
import com.example.bamboo.adapter.RecommendVideoAdapter;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.model.VideoBean;
import com.example.bamboo.myinterface.OnRecyclerViewItemViewClickListener;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.myinterface.services.RecommendVideoService;

import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bamboo.util.NetworkUtil.NETWORK_BASE_URL;
import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;
import static com.example.bamboo.util.NetworkUtil.NETWORK_VIDEO_ERR_RECOMMEND_UN_UPDATE;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 推荐视频
 * @time 2018/11/29 17:17
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoFragment extends Fragment {
    private RecyclerView rv;
    private RecommendVideoAdapter adapter;
    private LinkedList<VideoBean> list;
    private static final String TAG = "RecommendVideoFragment";
    private SwipeRefreshLayout srl;
    private int position = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_video, null, false);
        initView(v);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv.setAdapter(adapter);

        adapter.setRecyclerViewOnClickListener(new RecyclerViewOnClickListener() {
            @Override
            public void onClick(Object obj, int position, Object tag) {
                Intent i = new Intent();
                switch ((String) tag) {
                    case "article":
                        i.setClass(getActivity(), RecommendVideoArticleActivity.class);
                        break;
                    case "video":
                        i.putExtra("video_id", list.get(position).getVideoId());
                        i.setClass(getActivity(), RecommendVideoDetailedActivity.class);
                        break;
                    case "ad":
                        break;
                    default:
                        break;
                }
                startActivity(i);
            }
        });

        adapter.setItemViewClickListener(position -> {
            Log.e(TAG, "onClick: menu");
            WindowManager.LayoutParams lp = getActivity().getWindow()
                    .getAttributes();
            lp.alpha = 0.4f;
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp);

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_video_more, null, false);
            PopupWindow popupWindow = new PopupWindow(view);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            popupWindow.setFocusable(true);
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            popupWindow.setOnDismissListener(() -> {
                WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
                lp1.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp1);
            });
        });


        srl.setOnRefreshListener(() -> MyApplication.retrofit.create(RecommendVideoService.class)
                .getRecommendVideo(position)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(videoBeanJsonBean -> {
                    int code = videoBeanJsonBean.getCode();
                    switch (code) {
                        case NETWORK_RESULT_OK:
                            for (int i = 0; i < videoBeanJsonBean.getBody().size(); i++) {
                                list.addFirst(videoBeanJsonBean.getBody().get(i));
                                adapter.notifyItemChanged(i);
                            }
                            Toast.makeText(getActivity(), "刷新成功", Toast .LENGTH_SHORT).show();
                            break;
                        case NETWORK_VIDEO_ERR_RECOMMEND_UN_UPDATE:
                            Toast.makeText(getActivity(), "没有更新的推荐", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    srl.setRefreshing(false);
                    position += 2;
                }));
        return v;

    }

    private void initView(View v) {
        rv = v.findViewById(R.id.recommend_video_rv);
        list = new LinkedList<>();
        adapter = new RecommendVideoAdapter(getActivity(), list);
        srl = v.findViewById(R.id.recommend_video_srl);
    }

}
