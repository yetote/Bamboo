package com.example.bamboo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.model.AddImBean;
import com.example.bamboo.util.IdentityUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2019/1/25 17:53
 * @change
 * @chang time
 * @class describe
 */
public class ContactApplyAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<AddImBean> list;

    public ContactApplyAdapter(Context context, ArrayList<AddImBean> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView headIv, identityIv;
        private TextView nickname, tel, state;

        public ImageView getHeadIv() {
            return headIv;
        }

        public ImageView getIdentityIv() {
            return identityIv;
        }

        public TextView getNickname() {
            return nickname;
        }

        public TextView getTel() {
            return tel;
        }

        public TextView getState() {
            return state;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.item_contact_apply_headIv);
            identityIv = itemView.findViewById(R.id.item_contact_apply_identity);
            nickname = itemView.findViewById(R.id.item_contact_apply_uName);
            tel = itemView.findViewById(R.id.item_contact_apply_tel);
            state = itemView.findViewById(R.id.item_contact_apply_state);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact_apply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        Glide.with(context).load(list.get(position).getuHeader()).into(vh.getHeadIv());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getuIdentity())).into(vh.getIdentityIv());
        vh.getNickname().setText(list.get(position).getuName());
        vh.getTel().setText(list.get(position).getuTel());
        String s = "";
        switch (list.get(position).getState()) {
            case "waiting":
                s = "等待审核";
                break;
            case "agree":
                s = "已同意";
                break;
            default:
                break;
        }
        vh.getState().setText(s);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
