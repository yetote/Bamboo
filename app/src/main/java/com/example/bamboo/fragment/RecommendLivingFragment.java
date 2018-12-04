package com.example.bamboo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.adapter.RecommendLivingAdapter;
import com.example.bamboo.model.RecommendLivingBean;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.fragment
 * @class 直播
 * @time 2018/11/29 15:09
 * @change
 * @chang time
 * @class describe
 */
public class RecommendLivingFragment extends Fragment {
    private RecommendLivingAdapter adapter;
    private LinkedList<RecommendLivingBean> list;
    private RecyclerView rv;
    private boolean isScrolling = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommend_living, null, false);

        initView(v);

        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(adapter);

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

        return v;
    }

    private void initView(View v) {
        rv = v.findViewById(R.id.recommend_living_rv);
        list = new LinkedList<>();

        list.add(new RecommendLivingBean(0, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(1, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(2, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(3, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(4, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(5, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(6, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(7, "右边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566487113&di=651cf8c3b417dad38b5739ec3d3d98a3&imgtype=0&src=http%3A%2F%2Fimg.besoo.com%2Ffile%2F201705%2F16%2F0942077945908.jpg", "test01", "", 45));
        list.add(new RecommendLivingBean(8, "寅子的游戏教室", "top", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543573413460&di=be2134ceb0eb1c63eededbe7d8e0d466&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F120%2F24.jpg", "test01", "总榜NO.1", 45547));
        list.add(new RecommendLivingBean(9, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(10, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(11, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(12, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(13, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(14, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(15, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(16, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(17, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(18, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(19, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(20, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(21, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(22, "右边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566487113&di=651cf8c3b417dad38b5739ec3d3d98a3&imgtype=0&src=http%3A%2F%2Fimg.besoo.com%2Ffile%2F201705%2F16%2F0942077945908.jpg", "test01", "", 45));
        list.add(new RecommendLivingBean(23, "寅子的游戏教室", "top", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544513446&di=c1442bb633d0001182dfa5665482c2f5&imgtype=jpg&er=1&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fd0c8a786c9177f3ea9182a277bcf3bc79f3d56e8.jpg", "test01", "总榜NO.1", 45547));
        list.add(new RecommendLivingBean(24, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(25, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(26, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(27, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(28, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(29, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(30, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(31, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(32, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(33, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(34, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(35, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(36, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(37, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(38, "右边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566487113&di=651cf8c3b417dad38b5739ec3d3d98a3&imgtype=0&src=http%3A%2F%2Fimg.besoo.com%2Ffile%2F201705%2F16%2F0942077945908.jpg", "test01", "", 45));
        list.add(new RecommendLivingBean(39, "寅子的游戏教室", "top", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543573413460&di=be2134ceb0eb1c63eededbe7d8e0d466&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F120%2F24.jpg", "test01", "总榜NO.1", 45547));
        list.add(new RecommendLivingBean(40, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(41, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(42, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(43, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(44, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(45, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(46, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(47, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(48, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(49, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(50, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(51, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(52, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(53, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(54, "右边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566487113&di=651cf8c3b417dad38b5739ec3d3d98a3&imgtype=0&src=http%3A%2F%2Fimg.besoo.com%2Ffile%2F201705%2F16%2F0942077945908.jpg", "test01", "", 45));
        list.add(new RecommendLivingBean(55, "寅子的游戏教室", "top", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543573413460&di=be2134ceb0eb1c63eededbe7d8e0d466&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa1%2F120%2F24.jpg", "test01", "总榜NO.1", 45547));
        list.add(new RecommendLivingBean(56, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(57, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));
        list.add(new RecommendLivingBean(58, "古剑三速通", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566526924&di=47fdd6b809bd9c9a688e142f0fd52a4e&imgtype=0&src=http%3A%2F%2Fimg.duoziwang.com%2F2016%2F12%2F14%2F14023851160.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(59, "左边拉满", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275807&di=0476357e1763a860c9f6c551830c7167&imgtype=0&src=http%3A%2F%2Fimage.biaobaiju.com%2Fuploads%2F20180122%2F21%2F1516627042-UAEHgjCzXR.jpg", "test01", "", 867));
        list.add(new RecommendLivingBean(60, "今天天气不错", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "热搜榜TOP1", 4268));
        list.add(new RecommendLivingBean(61, "古剑三速通", "hot", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544168070&di=6056d2f5ed58c2b6f9a94c3d286a96f1&imgtype=jpg&er=1&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F25%2F56%2F29%2F58396c9c1a3a4_1024.jpg", "test01", "新人", 976543));
        list.add(new RecommendLivingBean(62, "今天天气真的很棒", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=ec4e87f3b7fae6d8e8c418fedc8badab&imgtype=0&src=http%3A%2F%2Fimg1.2345.com%2Fduoteimg%2FqqTxImg%2F2013%2F12%2Fwm%2F29-085312_75.jpg", "test01", "游戏榜TOP1", 2345));
        list.add(new RecommendLivingBean(63, "宋老师教你写代码，算了，你退群吧", "living", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543566275808&di=fe6155cca314e80198a9acae8d0bb95c&imgtype=0&src=http%3A%2F%2Fv1.qzone.cc%2Favatar%2F201408%2F15%2F11%2F43%2F53ed81e4b1959964.jpg%2521200x200.jpg", "test01", "", 34567));

        adapter = new RecommendLivingAdapter(getContext(), list);
    }
}
