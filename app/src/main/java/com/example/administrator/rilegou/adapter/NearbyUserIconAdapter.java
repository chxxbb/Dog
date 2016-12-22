package com.example.administrator.rilegou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.rilegou.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/12/9 0009.
 */
public class NearbyUserIconAdapter extends RecyclerView.Adapter {

    List<String> list;
    Context context;
    LayoutInflater inflater;
    View view;


    public NearbyUserIconAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    class Viewholder extends RecyclerView.ViewHolder {
        private CircleImageView iv_comment_user_icon;


        public Viewholder(View itemView) {
            super(itemView);
            iv_comment_user_icon = (CircleImageView) itemView.findViewById(R.id.iv_comment_user_icon);

            //设置点击事件

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_comment_user_icon_item, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Viewholder viewholder = (Viewholder) holder;

        if (position == 0) {
            viewholder.iv_comment_user_icon.setImageResource(R.drawable.nearby_user_praise);
        } else {
            viewholder.iv_comment_user_icon.setImageResource(R.drawable.fuck);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
