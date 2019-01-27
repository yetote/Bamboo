package com.example.bamboo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;
import com.example.bamboo.application.MyApplication;
import com.example.bamboo.model.AddImBean;
import com.example.bamboo.model.JsonBean;
import com.example.bamboo.myinterface.services.UserService;
import com.example.bamboo.util.IdentityUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.bamboo.util.NetworkUtil.NETWORK_RESULT_OK;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2019/1/25 17:54
 * @change
 * @chang time
 * @class describe
 */
public class ContactReceiveAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<AddImBean> list;

    public ContactReceiveAdapter(Context context, ArrayList<AddImBean> list) {
        this.context = context;
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView headIv, identityIv;
        private TextView nickname, tel;
        private Button add;

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

        public Button getAdd() {
            return add;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            headIv = itemView.findViewById(R.id.item_contact_receive_headIv);
            identityIv = itemView.findViewById(R.id.item_contact_receive_identity);
            nickname = itemView.findViewById(R.id.item_contact_receive_uName);
            tel = itemView.findViewById(R.id.item_contact_receive_tel);
            add = itemView.findViewById(R.id.item_contact_receive_add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) itemView.getTag();
                    MyApplication.retrofit.create(UserService.class)
                            .changeAddState(list.get(position).getId(), "agree", MyApplication.uName, list.get(position).getuTel())
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integerJsonBean -> {
                                if (integerJsonBean.getCode() == NETWORK_RESULT_OK) {
                                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                                    add.setBackgroundColor(Color.TRANSPARENT);
                                    add.setTextColor(context.getResources().getColor(R.color.gray_pressed));
                                } else {
                                    Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact_receive, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder vh = (MyViewHolder) holder;
        Glide.with(context).load(list.get(position).getuHeader()).into(vh.getHeadIv());
        Glide.with(context).load(IdentityUtils.getIdentityDrawable(list.get(position).getuIdentity())).into(vh.getIdentityIv());
        vh.getNickname().setText(list.get(position).getuName());
        vh.getTel().setText(list.get(position).getuTel());
        switch (list.get(position).getState()) {
            case "waiting":
                vh.getAdd().setText("添加");
                vh.getAdd().setBackgroundColor(context.getResources().getColor(R.color.mediumseagreen));
                vh.getAdd().setTextColor(Color.WHITE);
                break;
            case "agree":
                vh.getAdd().setText("已同意");
                vh.getAdd().setBackgroundColor(context.getResources().getColor(R.color.gray_pressed));
                vh.getAdd().setTextColor(context.getResources().getColor(R.color.mediumseagreen));
                break;
            default:
                break;
        }
        vh.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
