package com.example.bamboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.model.RecommendBean;
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
public class RecommendAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<RecommendBean> list;
    private static final String RECOMMEND_ARTICLE_TAG = "article";
    private static final String RECOMMEND_VIDEO_TAG = "video";
    private static final String RECOMMEND_AD_TAG = "ad";
    private static final int TYPE_ARTICLE = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_AD = 2;

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
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private VideoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        private AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RecommendAdapter(Context context, ArrayList<RecommendBean> list) {
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
                            return 6;
                        case TYPE_VIDEO:
                            return 3;
                        case TYPE_AD:
                            return 6;
                        default:
                            return 3;
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
                break;
            case TYPE_ARTICLE:
                v = LayoutInflater.from(context).inflate(R.layout.item_recommend_video_article, parent, false);
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
            Glide.with(context).load(list.get(position).getUrl()).into(((ArticleViewHolder) holder).img);
            ((ArticleViewHolder) holder).getContent().setText(list.get(position).getContent());
            ((ArticleViewHolder) holder).getCategory().setText(list.get(position).getCategory());
            ((ArticleViewHolder) holder).getTitle().setText(list.get(position).getTitle());
            ((ArticleViewHolder) holder).getDiscussNum().setText(context.getResources().getString(R.string.recommend_discuss_num, list.get(position).getDiscussNum()));
            ((ArticleViewHolder) holder).getTime().setText(TimeUtil.caseTime(list.get(position).getVideoTime()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getTag().equals(RECOMMEND_VIDEO_TAG)) {
            return TYPE_VIDEO;
        }
        if (list.get(position).getTag().equals(RECOMMEND_ARTICLE_TAG)) {
            return TYPE_ARTICLE;
        }
        if (list.get(position).getTag().equals(RECOMMEND_AD_TAG)) {
            return TYPE_AD;
        }
        return -1;
    }
}
