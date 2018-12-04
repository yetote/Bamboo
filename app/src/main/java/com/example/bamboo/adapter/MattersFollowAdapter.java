package com.example.bamboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.PersonalImActivity;
import com.example.bamboo.R;
import com.example.bamboo.model.MattersFollowBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.myview.NineImageView;
import com.example.bamboo.util.TimeUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class 动态 Adapter
 * @time 2018/11/12 14:42
 * @change
 * @chang time
 * @class describe
 */
public class MattersFollowAdapter extends RecyclerView.Adapter {
    private ArrayList<MattersFollowBean> list;
    private Context context;
    private static final String TAG = "MattersFollowAdapter";
    private RecyclerViewOnClickListener listener;

    public MattersFollowAdapter(@NonNull ArrayList<MattersFollowBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setListener(RecyclerViewOnClickListener listener) {
        this.listener = listener;
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        private TextView name, context, releaseTime;
        private ImageView headIv, identityIv;
        private NineImageView nineImageView;

        private TextView getName() {
            return name;
        }

        private TextView getContext() {
            return context;
        }

        private TextView getReleaseTime() {
            return releaseTime;
        }

        private ImageView getHeadIv() {
            return headIv;
        }

        private ImageView getIdentityIv() {
            return identityIv;
        }

        private NineImageView getNineImageView() {
            return nineImageView;
        }

        private ImageHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_matters_username);
            context = itemView.findViewById(R.id.item_matters_content);
            releaseTime = itemView.findViewById(R.id.item_matters_release_time);
            headIv = itemView.findViewById(R.id.item_matters_heads);
            identityIv = itemView.findViewById(R.id.item_matters_identity);
            nineImageView = itemView.findViewById(R.id.item_matters_nineIv);
            Log.e(TAG, "ImageHolder: ");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_matters_layout_image, parent, false);
        ImageHolder holder = new ImageHolder(v);
        v.setOnClickListener(v1 -> listener.onClick((Integer) v1.getTag()));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageHolder vh = (ImageHolder) holder;
        vh.getContext().setText(list.get(position).getContent());
        vh.getName().setText(list.get(position).getName());
        vh.getReleaseTime().setText(TimeUtil.agoTime(list.get(position).getReleaseTime()));
        Glide.with(context).load(list.get(position).getHeadImg()).into(vh.getHeadIv());
        Glide.with(context).asDrawable().load(R.drawable.star).into(vh.getIdentityIv());
        if (list.get(position).getImageList() != null) {
            vh.getNineImageView().setSize(list.get(position).getImageList().size());
            vh.getNineImageView().setUrlList(list.get(position).getImageList());
        } else {
            vh.getNineImageView().setVisibility(View.GONE);
        }
        vh.itemView.setTag(list.get(position).getId());

        vh.getHeadIv().setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("id", list.get(position).getId());
            i.setClass(context, PersonalImActivity.class);
            context.startActivity(i);
        });
        vh.getName().setOnClickListener(v -> {
            Intent i = new Intent();
            i.putExtra("id", list.get(position).getId());
            i.setClass(context, PersonalImActivity.class);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
