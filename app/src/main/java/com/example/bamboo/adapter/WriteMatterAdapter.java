package com.example.bamboo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bamboo.R;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.adapter
 * @class describe
 * @time 2019/1/29 23:33
 * @change
 * @chang time
 * @class describe
 */
public class WriteMatterAdapter extends RecyclerView.Adapter {
    private ArrayList<String> list;
    private Context context;
    private static final String TAG = "WriteMatterAdapter";

    public WriteMatterAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv, remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_write_matters_iv);
            remove = itemView.findViewById(R.id.item_write_matters_remove);
            remove.setOnClickListener(v -> {
                int i = (int) itemView.getTag();
                Log.e(TAG, "MyViewHolder: "+i );
                list.remove(list.get(i));
                notifyItemRemoved((Integer) itemView.getTag());
                notifyItemRangeChanged(i, list.size());
            });
        }

        public ImageView getIv() {
            return iv;
        }

        public ImageView getRemove() {
            return remove;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_write_matters, parent, false);
        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Glide.with(context).load(list.get(position)).into(viewHolder.getIv());
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
