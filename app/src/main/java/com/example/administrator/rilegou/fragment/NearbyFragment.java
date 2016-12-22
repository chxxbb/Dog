package com.example.administrator.rilegou.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.administrator.rilegou.utils.BannerImageLoader;
import com.example.administrator.rilegou.view.ListViewForScrollView;
import com.example.administrator.rilegou.adapter.NearbyListViewAdapter;
import com.example.administrator.rilegou.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class NearbyFragment extends Fragment {
    View view;
    Banner banner;
    List<String> bannerList = new ArrayList<>();
    ListViewForScrollView lv_nearby;
    NearbyListViewAdapter adapter;
    List<String> data = new ArrayList<>();
    ScrollView scrollView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nearbt, null);
        findView();

        init();
        return view;
    }


    private void findView() {
        banner = (Banner) view.findViewById(R.id.banner);

        lv_nearby = (ListViewForScrollView) view.findViewById(R.id.lv_nearby);

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        lv_nearby.setVerticalScrollBarEnabled(false);

        scrollView.setVerticalScrollBarEnabled(false);

        scrollView.smoothScrollTo(1, 1);

    }


    @Override
    public void onStart() {
        super.onStart();

        scrollView.smoothScrollTo(1, 1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv_nearby.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        scrollView.smoothScrollTo(1, 1);

    }


    private void init() {
        for (int i = 0; i < 3; i++) {
            data.add("1");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter = new NearbyListViewAdapter(getContext(), data);

                handler.sendEmptyMessage(0);
            }
        }).start();

        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);

        banner.setDelayTime(5000);

        bannerList.add("http://ohpgig5p4.bkt.clouddn.com/PM.jpg");
        bannerList.add("http://ohpgig5p4.bkt.clouddn.com/pm1.jpg");
        bannerList.add("http://ohpgig5p4.bkt.clouddn.com/pm2.jpg");
        bannerList.add("http://ohpgig5p4.bkt.clouddn.com/daxue.jpg");

        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(bannerList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}
