package com.example.bamboo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bamboo.PersonalImActivity;
import com.example.bamboo.R;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.AddImBean;
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
 * @time 2019/1/15 16:57
 * @change
 * @chang time
 * @class describe
 */
public class ContactAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<AddImBean> list;
    private static final String TAG = "ContactAdapter";
    /**
     * 作为申请人
     */
    private final int APPLY_USER = 0;
    /**
     * 作为被申请人
     */
    private final int APPLIED_USER = 0;

    private OnRecyclerViewItemViewClickListener itemViewClickListener;

    public void setItemViewClickListener(OnRecyclerViewItemViewClickListener itemViewClickListener) {
        this.itemViewClickListener = itemViewClickListener;
    }

    public ContactAdapter(Context context, ArrayList<AddImBean> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView headIv;
        private ImageView identityIv;
        private Button addBtn;
        private TextView uName;

        public Button getAddBtn() {
            return addBtn;
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.item_contact_state_headIv);
            identityIv = itemView.findViewById(R.id.item_contact_state_identity);
            addBtn = itemView.findViewById(R.id.item_contact_state_add);
            uName = itemView.findViewById(R.id.item_contact_state_uName);
            headIv.setOnClickListener(v -> {
                Intent i = new Intent();
                String name = (String) itemView.getTag(R.id.list_item_person_name);
                Log.e(TAG, "MyViewHolder: " + name);
                i.putExtra("u_name", (String) itemView.getTag(R.id.list_item_person_name));
                i.setClass(context, PersonalImActivity.class);
                context.startActivity(i);
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_contact_state, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        String text = "";
        if (list.get(position).getUsername().equals(MyApplication.uName)) {
            //当前用户作为申请人
            vh.getuName().setText(list.get(position).getContactname());
            switch (list.get(position).getState()) {
                case "apply":
                    //同意
                    text = "对方已同意";
                    break;
                case "waiting":
                    text = "等待中";
                    break;
                case "refuse":
                    text = "对方已拒绝";
                    break;
                default:
                    break;
            }
            vh.itemView.setTag(R.id.list_item_person_name, list.get(position).getContactname());
        } else {
            vh.getAddBtn().setTextColor(Color.WHITE);
            vh.getuName().setText(list.get(position).getUsername());
            //被申请人
            switch (list.get(position).getState()) {
                case "apply":
                    //同意
                    text = "√已同意";
                    vh.getAddBtn().setBackgroundColor(Color.WHITE);
                    vh.getAddBtn().setTextColor(context.getResources().getColor(R.color.gray_pressed));
                    break;
                case "waiting":
                    text = "同意";
                    vh.getAddBtn().setBackgroundColor(context.getResources().getColor(R.color.mediumaquamarine));
                    vh.getAddBtn().setTextColor(Color.WHITE);
                    vh.getAddBtn().setOnClickListener(v -> itemViewClickListener.onClick((Integer) vh.itemView.getTag()));
                    break;
                case "refuse":
                    text = "已拒绝";
                    vh.getAddBtn().setTextColor(Color.WHITE);
                    vh.getAddBtn().setBackgroundColor(Color.RED);
                    break;
                default:
                    break;
            }
            vh.itemView.setTag(R.id.list_item_person_name, list.get(position).getUsername());
        }
        vh.addBtn.setText(text);
        Glide.with(context).load(list.get(position).getuHeader()).into(vh.getHeadIv());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getuIdentity())).into(vh.getIdentityIv());
        vh.itemView.setTag(position);
//        vh.itemView.setTag();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
