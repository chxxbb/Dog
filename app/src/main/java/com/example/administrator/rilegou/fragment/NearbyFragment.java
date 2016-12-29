package com.example.administrator.rilegou.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Contents;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Root;
import com.example.administrator.rilegou.data.MyMessageItem;
import com.example.administrator.rilegou.utils.BannerImageLoader;
import com.example.administrator.rilegou.view.ListViewForScrollView;
import com.example.administrator.rilegou.adapter.NearbyListViewAdapter;
import com.example.administrator.rilegou.R;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class NearbyFragment extends Fragment {
    View view;
    Banner banner;
    List<String> bannerList = new ArrayList<>();
    ListViewForScrollView lv_nearby;
    NearbyListViewAdapter adapter;
    List<MyMessageItem> data = new ArrayList<>();
    ScrollView scrollView;

    //定位服务
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        scrollView.smoothScrollTo(1, 1);

    }


    private void init() {
        //准备数据
        startLoc();

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

    /**
     * 定位初始化
     * option.setIsNeedAddress(true)设置接收地址数据
     */
    private void startLoc() {
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }

            //基于定位位置进行云检索,检索周边的帖子数据
            OkHttpUtils
                    .get()
                    .url("http://api.map.baidu.com/geosearch/v3/nearby" + "?" + MapData.mCode)
                    .addParams("ak", MapData.Ak)
                    .addParams("geotable_id", MapData.ServiceId)
                    .addParams("location", location.getLongitude() + "," + location.getLatitude())
                    .addParams("radius", "100000")
                    .addParams("sortby", "distance:1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            Root root = gson.fromJson(response, Root.class);

                            if (root.getStatus() == 0) {        //若返回码为0,则代表成功
                                System.out.println("附近页面检索反馈正常");
                                for (Contents contents : root.getContents()) {
                                    MyMessageItem myMessageItem = new MyMessageItem();

                                    myMessageItem.setContentImage(contents.getImage().getBig());
                                    myMessageItem.setState(contents.getState());
                                    myMessageItem.setAddress(contents.getTitle());
                                    myMessageItem.setContent(contents.getContent());
                                    myMessageItem.setTime(contents.getTime());

                                    data.add(myMessageItem);

                                }
                                adapter = new NearbyListViewAdapter(getContext(), data);
                                lv_nearby.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                mLocClient.stop();  //只定位一次
                            } else {
                                System.out.println(response);
                            }
                        }
                    });

        }


        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onPause() {
        if (mLocClient != null) {
            mLocClient.stop();
        }

        mLocClient = null;
        adapter = null;
        data = null;
        super.onPause();
    }
}
