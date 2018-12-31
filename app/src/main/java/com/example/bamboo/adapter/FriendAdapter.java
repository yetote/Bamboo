package com.example.bamboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.PersonalImActivity;
import com.example.bamboo.R;
import com.example.bamboo.model.PersonalBean;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.IdentityUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2018/12/27 9:59
 * @change
 * @chang time
 * @class describe
 */
public class FriendAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<PersonalBean> list;
    private RecyclerViewOnClickListener clickListener;

    public void setClickListener(RecyclerViewOnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public FriendAdapter(Context context, ArrayList<PersonalBean> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView headIv;
        private ImageView identityIv, chatIv;
        private TextView uName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.item_friend_headIv);
            identityIv = itemView.findViewById(R.id.item_friend_identity);
            chatIv = itemView.findViewById(R.id.item_friend_chat);
            uName = itemView.findViewById(R.id.item_friend_uName);
            headIv.setOnClickListener(v -> {
                Intent i = new Intent();
                i.putExtra("id", (Integer) itemView.getTag());
                i.setClass(context, PersonalImActivity.class);
                context.startActivity(i);
            });
        }

        public CircleImageView getHeadIv() {
            return headIv;
        }

        public ImageView getIdentityIv() {
            return identityIv;
        }

        public TextView getuName() {
            return uName;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        v.setOnClickListener(v1 -> clickListener.onClick((Integer) v1.getTag(), (Integer) v1.getTag(R.id.list_position)));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        vh.getuName().setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getHeadImg()).into(vh.getHeadIv());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getIdentity())).into(vh.getIdentityIv());
        vh.itemView.setTag(list.get(position).getUid());
        vh.itemView.setTag(R.id.list_position, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
