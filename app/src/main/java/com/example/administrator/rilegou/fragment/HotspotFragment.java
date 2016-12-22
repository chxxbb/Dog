package com.example.administrator.rilegou.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.HotspotListViewAdapter;
import com.example.administrator.rilegou.data.AdapterItemList;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Root;
import com.example.administrator.rilegou.data.MyItem;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class HotspotFragment extends Fragment {
    View view;
    //UI相关
    RelativeLayout locImagereLativelayout;  //定位按钮

    RelativeLayout focusRelativeLayout;     //我的关注相关
    ImageView focusImageView;
    int focus_i = 0;

    RelativeLayout loveRelativeLayout;      //我点赞的相关
    ImageView loveImageView;
    int love_i = 0;

    ListView listView;                      //详情列表Listview相关
    AdapterItemList adapterItemList;
    List<AdapterItemList> list = new ArrayList<AdapterItemList>();
    HotspotListViewAdapter hotspotListViewAdapter;
    RelativeLayout list_relativelayout;
    RelativeLayout hotspot_list_top_relativelayout;


    //基础地图相关
    MapView mMapView;
    BaiduMap mBaiduMap;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位

    //点聚合相关
    private ClusterManager<MyItem> mClusterManager;
    MapStatus ms;

    public HotspotFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = MapData.view;
        this.mBaiduMap = MapData.mBaiduMap;
        this.mLocClient = MapData.mLocClient;
        this.mMapView = MapData.mMapView;

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        //开始定位、定位焦点到当前位置
        startLoc();

        //设置点聚合
        Point_aggregation();

        //获取按钮和设置布局的监听器
        init();

        //传送地图数据到MainActivity,以便销毁
        MapData.mBaiduMap = this.mBaiduMap;
        MapData.mLocClient = this.mLocClient;
        MapData.mMapView = this.mMapView;

        return view;
    }

    private void init() {

        locImagereLativelayout = (RelativeLayout) view.findViewById(R.id.hotspot_location_relativelayout);

        locImagereLativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isFirstLoc = true;

            }
        });


        focusRelativeLayout = (RelativeLayout) view.findViewById(R.id.hotspot_focus_relativelayout);
        focusImageView = (ImageView) view.findViewById(R.id.hotspot_focus_image);

        focusRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (focus_i) {
                    case 0:
                        focusImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.hotspot_focus_image1));
                        focus_i = 1;
                        break;
                    case 1:
                        focusImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.hotspot_focus_image));
                        focus_i = 0;
                        break;
                }

            }
        });

        loveRelativeLayout = (RelativeLayout) view.findViewById(R.id.hotspot_love_relativelayout);
        loveImageView = (ImageView) view.findViewById(R.id.hotspot_love_image);

        loveRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (love_i) {
                    case 0:
                        loveImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.hotspot_love_image1));
                        love_i = 1;
                        break;
                    case 1:
                        loveImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.hotspot_love_image));
                        love_i = 0;
                        break;
                }
            }
        });


        list_relativelayout = (RelativeLayout) view.findViewById(R.id.list_relativelayout);
        hotspot_list_top_relativelayout = (RelativeLayout) view.findViewById(R.id.hotspot_list_top_relativelayout);
        hotspot_list_top_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_relativelayout.setVisibility(View.GONE);
            }
        });


        listView = (ListView) view.findViewById(R.id.hotspot_list_listview);
        hotspotListViewAdapter = new HotspotListViewAdapter(getActivity());
        listView.setAdapter(hotspotListViewAdapter);
    }

    private void Point_aggregation() {
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(getActivity(), mBaiduMap);
        // 添加Marker点
        addMarkers();

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);

        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(getActivity(),
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                setMarkers(cluster);
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                Toast.makeText(getActivity(),
                        "点击单个Item", Toast.LENGTH_SHORT).show();
                if (item.getmContent() != null) {
                    Toast.makeText(getActivity(),
                            item.mContent, Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    private void setMarkers(Cluster<MyItem> cluster) {

        list_relativelayout.setVisibility(View.VISIBLE);
        list.clear();

        for (MyItem item : cluster.getItems()) {
            adapterItemList = new AdapterItemList(0, item.mContent, item.mUserName, item.mLoc, item.mTime, item.mState);
            list.add(adapterItemList);
        }

        hotspotListViewAdapter.setList(list);
        hotspotListViewAdapter.notifyDataSetChanged();

    }


    private void startLoc() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            //获取位置信息
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    //获取纬度
                    .latitude(location.getLatitude())
                    //获取经度
                    .longitude(location.getLongitude())
                    .build();
            //向API输入获取的位置信息
            mBaiduMap.setMyLocationData(locData);

            //判断是否首次打开地图.若是,则调整焦距到当前定位位置
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                ms = builder.build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {

        OkHttpUtils
                .get()
                .url("http://api.map.baidu.com/geosearch/v3/nearby" + "?" + MapData.mCode)
                .addParams("ak", "6lKaHhn3GieVEaTZFCaEC3XrFdOFbHMX")
                .addParams("geotable_id", "160652")
                .addParams("location", "104.069145" + "," + "30.619118")
                .addParams("radius", "1000")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                System.out.println(response);
                Root root = gson.fromJson(response, Root.class);
                if (root.getStatus() == 0) {
                    System.out.println("检索反馈正常");
                    System.out.println("经度 : " + root.getContents().get(0).getLocation().get(0));
                } else {
                    Toast.makeText(getActivity(), "" + response, Toast.LENGTH_LONG).show();
                }
            }
        });


        // 添加Marker点
        LatLng llA = new LatLng(30.621016, 104.063456);
        LatLng llB = new LatLng(30.621016, 104.064456);
        LatLng llC = new LatLng(30.621016, 104.065456);
        LatLng llD = new LatLng(30.621016, 104.066456);
        LatLng llE = new LatLng(30.621016, 104.067456);
        LatLng llF = new LatLng(30.621016, 104.068456);
        LatLng llG = new LatLng(30.621016, 104.069456);

        List<MyItem> items = new ArrayList<MyItem>();

        items.add(new MyItem(llA).setmUserName("二营长").setmTime("9月17日").setmContent("你他娘的意大利炮呢···").setmLoc("人民南路46号").setmState(0));
        items.add(new MyItem(llB).setmUserName("大营长").setmTime("9月10日").setmContent("你他娘的德国山炮呢···").setmLoc("人民南路41号").setmState(1));
        items.add(new MyItem(llC).setmUserName("三营长").setmTime("9月13日").setmContent("你他娘的法国巨炮呢···").setmLoc("人民南路43号").setmState(2));
        items.add(new MyItem(llD).setmUserName("五营长").setmTime("9月15日").setmContent("你他娘的中国二炮呢···").setmLoc("人民南路45号").setmState(0));
        items.add(new MyItem(llE).setmUserName("六营长").setmTime("9月16日").setmContent("你他娘的人体大炮呢···").setmLoc("人民南路42号").setmState(1));
        items.add(new MyItem(llF).setmUserName("七营长").setmTime("9月12日").setmContent("你他娘的超电磁炮呢···").setmLoc("人民南路47号").setmState(2));
        items.add(new MyItem(llG).setmUserName("八营长").setmTime("9月18日").setmContent("你他娘的正阳子炮呢···").setmLoc("人民南路48号").setmState(1));

        mClusterManager.addItems(items);

    }

}
