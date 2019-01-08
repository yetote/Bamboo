package com.example.bamboo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.model.RecommendVideoBean;
import com.example.bamboo.myinterface.OnRecyclerViewItemViewClickListener;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.TimeUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2018/11/6 15:01
 * @change
 * @chang time
 * @class describe
 */
public class RecommendVideoAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<RecommendVideoBean> list;
    private static final String RECOMMEND_ARTICLE_TAG = "article";
    private static final String RECOMMEND_VIDEO_TAG = "video";
    private static final String RECOMMEND_AD_TAG = "ad";
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_AD = 2;
    private RecyclerViewOnClickListener recyclerViewOnClickListener;
    private OnRecyclerViewItemViewClickListener itemViewClickListener;

    public void setItemViewClickListener(OnRecyclerViewItemViewClickListener itemViewClickListener) {
        this.itemViewClickListener = itemViewClickListener;
    }

    public void setRecyclerViewOnClickListener(RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView title, content, time, discussNum, category;
        private ImageView img;

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getDiscussNum() {
            return discussNum;
        }

        public TextView getCategory() {
            return category;
        }

        public ImageView getImg() {
            return img;
        }

        private ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_recommend_article_title);
            content = itemView.findViewById(R.id.item_recommend_article_content);
            time = itemView.findViewById(R.id.item_recommend_article_time);
            discussNum = itemView.findViewById(R.id.item_recommend_article_discuss);
            category = itemView.findViewById(R.id.item_recommend_article_tag);
            img = itemView.findViewById(R.id.item_recommend_article_iv);
            Drawable discussImage = context.getResources().getDrawable(R.mipmap.discuss);
            discussImage.setBounds(0, 0, 60, 60);
            discussNum.setCompoundDrawables(discussImage, null, null, null);
            Drawable timeImage = context.getResources().getDrawable(R.mipmap.time);
            timeImage.setBounds(0, 0, 60, 60);
            time.setCompoundDrawables(timeImage, null, null, null);
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;
        private ImageView menu;
        private TextView title;
        private TextView category;
        private TextView topic;

        private VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_recommend_video_iv);
            menu = itemView.findViewById(R.id.item_recommend_video_menu);
            title = itemView.findViewById(R.id.item_recommend_video_title);
            category = itemView.findViewById(R.id.item_recommend_video_category);
            topic = itemView.findViewById(R.id.item_recommend_video_topic);

            menu.setOnClickListener(v -> itemViewClickListener.onClick((Integer) itemView.getTag(R.id.list_position)));
        }

        public ImageView getIv() {
            return iv;
        }

        public ImageView getMenu() {
            return menu;
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getCategory() {
            return category;
        }

        public TextView getTopic() {
            return topic;
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        private AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RecommendVideoAdapter(Context context, ArrayList<RecommendVideoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    switch (type) {
                        case TYPE_ARTICLE:
                            return 2;
                        case TYPE_VIDEO:
                            return 1;
                        case TYPE_AD:
                            return 2;
                        default:
                            return 1;
                    }

                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case TYPE_VIDEO:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_video_video, parent, false);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewOnClickListener.onClick(v.getTag(R.id.list_item_content), (Integer) v.getTag(R.id.list_position), v.getTag(R.id.list_item_tag));
                    }
                });
                return new VideoViewHolder(v);
            case TYPE_ARTICLE:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_video_article, parent, false);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewOnClickListener.onClick(v.getTag(R.id.list_item_content), (Integer) v.getTag(R.id.list_position), v.getTag(R.id.list_item_tag));
                    }
                });
                return new ArticleViewHolder(v);
            case TYPE_AD:
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            Glide.with(context).load(list.get(position).getVideoBcImage()).into(((ArticleViewHolder) holder).img);
            ((ArticleViewHolder) holder).getContent().setText(list.get(position).getVideoContent());
            ((ArticleViewHolder) holder).getCategory().setText(list.get(position).getVideoCategory());
            ((ArticleViewHolder) holder).getTitle().setText(list.get(position).getVideoTitle());
            ((ArticleViewHolder) holder).getDiscussNum().setText(context.getResources().getString(R.string.recommend_discuss_num, list.get(position).getVideoDiscussNum()));
            ((ArticleViewHolder) holder).getTime().setText(TimeUtil.caseTime(list.get(position).getVideoTime()));
            holder.itemView.setTag(R.id.list_item_tag, RECOMMEND_ARTICLE_TAG);

        } else if (holder instanceof VideoViewHolder) {
            Glide.with(context).load(list.get(position).getVideoBcImage()).into(((VideoViewHolder) holder).getIv());
            ((VideoViewHolder) holder).getCategory().setText(list.get(position).getVideoCategory());
            ((VideoViewHolder) holder).getTitle().setText(list.get(position).getVideoTitle());
            ((VideoViewHolder) holder).getTopic().setText(list.get(position).getVideoTopic());
            holder.itemView.setTag(R.id.list_item_tag, RECOMMEND_VIDEO_TAG);
        }
        holder.itemView.setTag(R.id.list_item_content, list.get(position).getVideoContent());
        holder.itemView.setTag(R.id.list_position, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getVideoTag().equals(RECOMMEND_VIDEO_TAG)) {
            return TYPE_VIDEO;
        }
        if (list.get(position).getVideoTag().equals(RECOMMEND_ARTICLE_TAG)) {
            return TYPE_ARTICLE;
        }
        if (list.get(position).getVideoTag().equals(RECOMMEND_AD_TAG)) {
            return TYPE_AD;
        }
        return -1;
    }
}
