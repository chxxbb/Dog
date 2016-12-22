package com.example.administrator.rilegou.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.rilegou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class NearbyListViewAdapter extends BaseAdapter {
    Context context;
    List<String> list;

    NearbyUserIconAdapter adapter;

    public NearbyListViewAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.nearby_listv_item, parent, false);

            viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.recyclerView_user_icon = (RecyclerView) convertView.findViewById(R.id.recyclerView_user_icon);

            viewHolder.recyclerView_user_icon.setHasFixedSize(true);

            viewHolder.recyclerView_user_icon.setLayoutManager(new StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL));

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText("日了狗");


        List<String> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            data.add("11");
        }

        adapter = new NearbyUserIconAdapter(context, data);

        viewHolder.recyclerView_user_icon.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return convertView;
    }

    static class ViewHolder {
        TextView tv;
        RecyclerView recyclerView_user_icon;
        TextView tv1;

    }
}
