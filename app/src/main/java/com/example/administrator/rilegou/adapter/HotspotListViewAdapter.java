package com.example.administrator.rilegou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.AdapterItemList;

import java.util.List;

/**
 * Created by Chen on 2016/12/17.
 */
public class HotspotListViewAdapter extends BaseAdapter {

    private Activity activity;
    private List<AdapterItemList> list;

    public HotspotListViewAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setList(List<AdapterItemList> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        //判断是否为空,是否已经到list结尾
        if (list != null && position < list.size()) {
            return list.get(position).getType();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return AdapterItemList.Content_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);   //确认当前要加载的布局的类型
        switch (type) {
            case AdapterItemList.Content:
                Hotspot_Holder holder = null;

                if (convertView == null) {  //if判断该布局是否以前创建过,若是,则直接填充数据.此方法优化运行速度和缓存
                    convertView = activity.getLayoutInflater().inflate(R.layout.hotspot_listview_item, null);
                    holder = new Hotspot_Holder();

                    holder.hotspot_listview_item_username = (TextView) convertView.findViewById(R.id.hotspot_listview_item_username);
                    holder.hotspot_listview_item_loc = (TextView) convertView.findViewById(R.id.hotspot_listview_item_loc);
                    holder.hotspot_listview_item_state = (TextView) convertView.findViewById(R.id.hotspot_listview_item_state);
                    holder.hotspot_listview_item_time = (TextView) convertView.findViewById(R.id.hotspot_listview_item_time);
                    holder.hotspot_listview_item_content = (TextView) convertView.findViewById(R.id.hotspot_listview_item_content);

                    //凭借该方法添加标志,以判断是否以前创建过布局
                    convertView.setTag(holder);
                } else {
                    holder = (Hotspot_Holder) convertView.getTag();
                }

                //给布局初始化(接着上面)该处的初始化每次创建都会被执行,一般用来输入数据.
                holder.hotspot_listview_item_username.setText(list.get(position).getmUserName());
                holder.hotspot_listview_item_loc.setText(list.get(position).getmLoc());
                holder.hotspot_listview_item_time.setText(list.get(position).getmTime());
                holder.hotspot_listview_item_content.setText(list.get(position).getmContent());
                switch (list.get(position).getmState()) {
                    case 0:     //正常
                        holder.hotspot_listview_item_state.setTextColor(0xFF00FF00);
                        holder.hotspot_listview_item_state.setText("正常");
                        break;
                    case 1:     //危险
                        holder.hotspot_listview_item_state.setTextColor(0xFFFF0000);
                        holder.hotspot_listview_item_state.setText("危险");
                        break;
                    case 2:     //伤病
                        holder.hotspot_listview_item_state.setTextColor(0xFFFF9900);
                        holder.hotspot_listview_item_state.setText("伤病");
                        break;
                }

                break;
        }
        return convertView;
    }

    static class Hotspot_Holder {

        TextView hotspot_listview_item_username;
        TextView hotspot_listview_item_loc;
        TextView hotspot_listview_item_content;
        TextView hotspot_listview_item_state;
        TextView hotspot_listview_item_time;

    }

    private String getState(int mState) {


        return null;
    }

}
