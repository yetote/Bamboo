package com.example.bamboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.PersonalImActivity;
import com.example.bamboo.R;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.OnRecyclerViewItemViewClickListener;
import com.example.bamboo.util.IdentityUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2019/1/15 14:10
 * @change
 * @chang time
 * @class describe
 */
public class SearchUserApapter extends RecyclerView.Adapter {
    private ArrayList<PersonalBean> list;
    private Context context;
    private OnRecyclerViewItemViewClickListener clickListener;

    public void setClickListener(OnRecyclerViewItemViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public SearchUserApapter(ArrayList<PersonalBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView headIv;
        private ImageView identityIv;
        private Button addBtn;
        private TextView uName;
        private TextView uTel;

        public CircleImageView getHeadIv() {
            return headIv;
        }

        public ImageView getIdentityIv() {
            return identityIv;
        }

        public TextView getuName() {
            return uName;
        }

        public TextView getuTel() {
            return uTel;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.item_search_user_headIv);
            identityIv = itemView.findViewById(R.id.item_search_user_identity);
            addBtn = itemView.findViewById(R.id.item_search_user_add);
            uName = itemView.findViewById(R.id.item_search_user_uName);
            headIv.setOnClickListener(v -> {
                Intent i = new Intent();
                i.putExtra("id", (Integer) itemView.getTag(R.id.list_position));
                i.setClass(context, PersonalImActivity.class);
                context.startActivity(i);
            });
            addBtn.setOnClickListener(v -> {
                clickListener.onClick((Integer) itemView.getTag(R.id.list_position));
                addBtn.setBackgroundColor(Color.TRANSPARENT);
                addBtn.setTextColor(context.getResources().getColor(R.color.gray_pressed));
                addBtn.setText("等待响应");
            });
            uTel = itemView.findViewById(R.id.item_search_user_tel);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_search_user, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.getuName().setText(list.get(position).getuName());
        vh.getuTel().setText(list.get(position).getuTel());
        Glide.with(context).load(list.get(position).getuHeader()).into(vh.getHeadIv());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getuIdentity())).into(vh.getIdentityIv());
        vh.itemView.setTag(R.id.user_name_tag, list.get(position).getuName());
        vh.itemView.setTag(R.id.list_position, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
