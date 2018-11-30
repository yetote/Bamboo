package com.example.bamboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamboo.R;
import com.example.bamboo.model.RecommendLivingBean;

import java.util.ArrayList;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2018/11/29 15:19
 * @change
 * @chang time
 * @class describe
 */
public class RecommendLivingAdapter extends RecyclerView.Adapter {
    private Context context;
    private LinkedList<RecommendLivingBean> list;

    public RecommendLivingAdapter(Context context, LinkedList<RecommendLivingBean> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 热点
     */
    private static final int TYPE_HOT = 0;

    /**
     * 标签
     */
    private static final int TYPE_TAG = 1;
    /**
     * 直播item
     */
    private static final int TYPE_LIVING = 2;
    /**
     * 最热(总榜与分区)
     */
    private static final int TYPE_TOP = 3;
    private static final int TYPE_ERR = -1;

    public static final String RECOMMEND_LIVING_HOT = "hot";
    public static final String RECOMMEND_LIVING_TAG = "tag";
    public static final String RECOMMEND_LIVING_LIVING = "living";
    public static final String RECOMMEND_LIVING_TOP = "top";

    private class HotViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotIv;

        public ImageView getHotIv() {
            return hotIv;
        }

        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
            hotIv = itemView.findViewById(R.id.recommend_living_hot_iv);
        }
    }

    private class TagViewHolder extends RecyclerView.ViewHolder {

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private class LivingViewHolder extends RecyclerView.ViewHolder {
        private TextView livingSpectatorNum, livingLiver, livingTag, livingTitle;
        private ImageView livIv;

        public TextView getLivingTitle() {
            return livingTitle;
        }

        public TextView getLivingSpectatorNum() {
            return livingSpectatorNum;
        }

        public TextView getLivingLiver() {
            return livingLiver;
        }

        public TextView getLivingTag() {
            return livingTag;
        }

        public ImageView getLivIv() {
            return livIv;
        }

        public LivingViewHolder(@NonNull View itemView) {
            super(itemView);
            livingSpectatorNum = itemView.findViewById(R.id.recommend_living_live_spectatorNum);
            livingLiver = itemView.findViewById(R.id.recommend_living_live_liver);
            livingTag = itemView.findViewById(R.id.recommend_living_live_tag);
            livIv = itemView.findViewById(R.id.recommend_living_live_iv);
            livingTitle = itemView.findViewById(R.id.recommend_living_live_title);
        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {
        private ImageView topBlur, topIv;
        private TextView topTitle, topLabel, topLiver, topSpectatorNum;

        public ImageView getTopBlur() {
            return topBlur;
        }

        public ImageView getTopIv() {
            return topIv;
        }

        public TextView getTopTitle() {
            return topTitle;
        }

        public TextView getTopLabel() {
            return topLabel;
        }

        public TextView getTopLiver() {
            return topLiver;
        }

        public TextView getTopSpectatorNum() {
            return topSpectatorNum;
        }

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            topBlur = itemView.findViewById(R.id.recommend_living_top_blur);
            topIv = itemView.findViewById(R.id.recommend_living_top_iv);
            topTitle = itemView.findViewById(R.id.recommend_living_top_title);
            topLabel = itemView.findViewById(R.id.recommend_living_top_tag);
            topLiver = itemView.findViewById(R.id.recommend_living_top_liver);
            topSpectatorNum = itemView.findViewById(R.id.recommend_living_top_spectatorNum);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case TYPE_HOT:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_living_hot, parent, false);
                HotViewHolder hotViewHolder = new HotViewHolder(v);
                return hotViewHolder;
            case TYPE_TAG:
                break;
            case TYPE_LIVING:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_living_live, parent, false);
                LivingViewHolder livingViewHolder = new LivingViewHolder(v);
                return livingViewHolder;
            case TYPE_TOP:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_living_top, parent, false);
                TopViewHolder topViewHolder = new TopViewHolder(v);
                return topViewHolder;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LivingViewHolder) {
            ((LivingViewHolder) holder).getLivingLiver().setText(list.get(position).getUser());
            ((LivingViewHolder) holder).getLivingSpectatorNum().setText(list.get(position).getSpectator() + "");
            if ("".equals(list.get(position).getLabel())) {
                ((LivingViewHolder) holder).getLivingTag().setVisibility(View.GONE);
            } else {
                ((LivingViewHolder) holder).getLivingTag().setText(list.get(position).getLabel());
            }
            Glide.with(context).load(list.get(position).getImg()).into(((LivingViewHolder) holder).getLivIv());
            ((LivingViewHolder) holder).getLivingTitle().setText(list.get(position).getTitle());
        }
        if (holder instanceof HotViewHolder) {
            Glide.with(context).load(list.get(position).getImg()).into(((HotViewHolder) holder).getHotIv());
        }
        if (holder instanceof TopViewHolder) {
            Glide.with(context).load(list.get(position).getImg()).apply(bitmapTransform(new BlurTransformation(20, 3))).into(((TopViewHolder) holder).getTopBlur());
            Glide.with(context).load(list.get(position).getImg()).into(((TopViewHolder) holder).getTopIv());
            ((TopViewHolder) holder).getTopLabel().setText(list.get(position).getLabel());
            ((TopViewHolder) holder).getTopTitle().setText(list.get(position).getTitle());
            ((TopViewHolder) holder).getTopLiver().setText(list.get(position).getUser());
            ((TopViewHolder) holder).getTopSpectatorNum().setText(list.get(position).getSpectator() + "");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tagToType(list.get(position).getTag());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = tagToType(list.get(position).getTag());
                    switch (type) {
                        case TYPE_HOT:
                            return 2;
                        case TYPE_TAG:
                            return 2;
                        case TYPE_TOP:
                            return 2;
                        case TYPE_LIVING:
                            return 1;
                        default:
                            break;
                    }
                    return 0;
                }
            });
        }
    }

    private int tagToType(String tag) {
        switch (tag) {
            case RECOMMEND_LIVING_HOT:
                return TYPE_HOT;
            case RECOMMEND_LIVING_TAG:
                return TYPE_TAG;
            case RECOMMEND_LIVING_LIVING:
                return TYPE_LIVING;
            case RECOMMEND_LIVING_TOP:
                return TYPE_TOP;
            default:
                break;
        }
        return TYPE_ERR;
    }
}
