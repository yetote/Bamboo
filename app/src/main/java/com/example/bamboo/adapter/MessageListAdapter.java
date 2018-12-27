package com.example.bamboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.model.MessageListModel;
import com.example.bamboo.myinterface.RecyclerViewOnClickListener;
import com.example.bamboo.util.IdentityUtils;
import com.example.bamboo.util.MathUtil;
import com.example.bamboo.util.TimeUtil;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ether QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class 消息列表
 * @time 2018/12/25 15:36
 * @change
 * @chang time
 * @class describe
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<MessageListModel> list;
    private RecyclerViewOnClickListener onClickListener;

    public void setOnClickListener(RecyclerViewOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MessageListAdapter(Context context, ArrayList<MessageListModel> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView headImg, identityImg;
        private TextView user, content, time, msgNum;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headImg = itemView.findViewById(R.id.item_message_heads);
            identityImg = itemView.findViewById(R.id.item_message_identity);
            user = itemView.findViewById(R.id.item_message_user);
            content = itemView.findViewById(R.id.item_message_content);
            time = itemView.findViewById(R.id.item_message_time);
            msgNum = itemView.findViewById(R.id.item_message_msgNum);
        }

        public ImageView getHeadImg() {
            return headImg;
        }

        public ImageView getIdentityImg() {
            return identityImg;
        }

        public TextView getUser() {
            return user;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getMsgNum() {
            return msgNum;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v.getTag());
            }
        });

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        Glide.with(context).load(list.get(position).getHeads()).into(vh.getHeadImg());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getIdentity())).into(vh.getIdentityImg());
        vh.getUser().setText(list.get(position).getUser());
        vh.getContent().setText(list.get(position).getContent());
        vh.getTime().setText(TimeUtil.msgTime(list.get(position).getTime()));
        vh.getMsgNum().setText(MathUtil.msgNum(list.get(position).getMsgNum()));
        vh.itemView.setTag(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
