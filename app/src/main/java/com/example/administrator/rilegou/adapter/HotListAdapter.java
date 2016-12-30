package com.example.administrator.rilegou.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.HotAdapterItem;
import com.example.administrator.rilegou.data.MyMessageItem;
import com.example.administrator.rilegou.utils.BannerImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2016/12/30.
 */

public class HotListAdapter extends BaseAdapter {

    private Activity activity;
    private List<HotAdapterItem> list;

    public HotListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setList(List<HotAdapterItem> list) {
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
        return HotAdapterItem.Content_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);   //确认当前要加载的布局的类型
        switch (type) {
            /**
             * Banner 不需要设置优化,否则会导致刷新页面的时候,Banner内的图片总数异常增多
             */
            case HotAdapterItem.BannerAndContent:
                HotListBanner_Holder holder;
                convertView = activity.getLayoutInflater().inflate(R.layout.bannerview, null);
                holder = new HotListBanner_Holder();

                holder.banner = (Banner) convertView.findViewById(R.id.found_hot_banner);
                holder.banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                holder.banner.setDelayTime(5000);
                List<String> bannerList = new ArrayList<>();
                bannerList.add("http://ohpgig5p4.bkt.clouddn.com/PM.jpg");
                bannerList.add("http://ohpgig5p4.bkt.clouddn.com/pm1.jpg");
                bannerList.add("http://ohpgig5p4.bkt.clouddn.com/pm2.jpg");
                bannerList.add("http://ohpgig5p4.bkt.clouddn.com/daxue.jpg");
                holder.banner.setImageLoader(new BannerImageLoader());
                //设置图片集合
                holder.banner.setImages(bannerList);
                //banner设置方法全部调用完毕时最后调用
                holder.banner.start();

                //凭借该方法添加标志,以判断是否以前创建过布局
                convertView.setTag(holder);

                break;

            /**
             * 消息布局
             */
            case HotAdapterItem.HotContent:
                HotListContent_Holder viewHolder;
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                    convertView = inflater.inflate(R.layout.nearby_listv_item, parent, false);
                    viewHolder = new HotListContent_Holder();


                    viewHolder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
                    viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);

                    viewHolder.nearby_listv_item_content_image = (ImageView) convertView.findViewById(R.id.nearby_listv_item_content_image);

                    viewHolder.nearby_listv_item_content_Address = (TextView) convertView.findViewById(R.id.nearby_listv_item_content_Address);

                    viewHolder.nearby_list_item_content = (TextView) convertView.findViewById(R.id.nearby_list_item_content);
                    viewHolder.nearby_list_item_time = (TextView) convertView.findViewById(R.id.nearby_list_item_time);


                    viewHolder.recyclerView_user_icon = (RecyclerView) convertView.findViewById(R.id.recyclerView_user_icon);
                    viewHolder.recyclerView_user_icon.setHasFixedSize(true);
                    viewHolder.recyclerView_user_icon.setLayoutManager(new StaggeredGridLayoutManager(12, StaggeredGridLayoutManager.VERTICAL));

                    convertView.setTag(viewHolder);

                } else {
                    viewHolder = (HotListContent_Holder) convertView.getTag();
                }

                HotAdapterItem hotAdapterItem = list.get(position);

                ImageLoader.getInstance().displayImage(hotAdapterItem.getImage(), viewHolder.nearby_listv_item_content_image);

                viewHolder.nearby_listv_item_content_Address.setText(hotAdapterItem.getAddress());

                switch (hotAdapterItem.getState()) {
                    case 0:     //正常
                        viewHolder.tv1.setText("正常");
                        viewHolder.tv1.setTextColor(0xFF00FF00);
                        break;
                    case 1:     //危险
                        viewHolder.tv1.setText("危险");
                        viewHolder.tv1.setTextColor(0xFFFF0000);
                        break;
                    case 2:     //伤病
                        viewHolder.tv1.setText("伤病");
                        viewHolder.tv1.setTextColor(0xFFFF9900);
                        break;
                }

                viewHolder.nearby_list_item_content.setText(hotAdapterItem.getContent());
                viewHolder.nearby_list_item_time.setText(hotAdapterItem.getTime());

                //设置顶部消息分类数据
                viewHolder.tv.setText("附近消息");


                //设置点赞数据
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    data.add("11");
                }
                NearbyUserIconAdapter adapter = new NearbyUserIconAdapter(activity, data);
                viewHolder.recyclerView_user_icon.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                break;
        }
        return convertView;
    }

    private static class HotListBanner_Holder {
        Banner banner;
    }

    private static class HotListContent_Holder {
        TextView tv;
        RecyclerView recyclerView_user_icon;
        TextView tv1;
        ImageView nearby_listv_item_content_image;
        TextView nearby_listv_item_content_Address;
        TextView nearby_list_item_content;
        TextView nearby_list_item_time;
    }

}
