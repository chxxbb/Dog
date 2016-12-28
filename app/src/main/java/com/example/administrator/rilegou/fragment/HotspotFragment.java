package com.example.administrator.rilegou.fragment;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.baidu.mapapi.clusterutil.MarkerManager;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.HotspotListViewAdapter;
import com.example.administrator.rilegou.data.ActivityData;
import com.example.administrator.rilegou.data.AdapterItemList;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Contents;
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
    public MyLocationConfiguration.LocationMode mCurrentMode;
    boolean isFirstLoc = true; // 是否首次定位
    Double latitude, longitude;

    //点聚合相关

    MapStatus ms;


    public HotspotFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ActivityData.Hot = getActivity();

        view = MapData.view;
        this.mBaiduMap = MapData.mBaiduMap;
        this.mMapView = MapData.mMapView;

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        //设置点聚合的类定义
        Point_aggregation();

        //开始定位、定位焦点到当前位置、添加Mark点数据
        startLoc();

        //获取按钮和设置布局的监听器
        init();

        //传送地图数据到MainActivity,以便销毁
        MapData.mBaiduMap = this.mBaiduMap;
        MapData.mMapView = this.mMapView;
        MapData.mLocClient = this.mLocClient;

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
        MapData.mClusterManager = new ClusterManager<MyItem>(getContext(), mBaiduMap);

        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(MapData.mClusterManager);

        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(MapData.mClusterManager);

        MapData.mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(getActivity(),
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                setMarkers(cluster);
                return false;
            }
        });
        MapData.mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
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
        option.setScanSpan(10000);
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
            if (latitude != null && longitude != null) {
                if (Math.abs(latitude - location.getLatitude()) > 0.005 || Math.abs(longitude - location.getLongitude()) > 0.005) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    addMarkers(latLng, null);
                }
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                addMarkers(latLng, null);
            }

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 向地图添加Marker点
     */
    public static void addMarkers(LatLng location, final MapStatus mapStatus) {

        MapData.now_mark_loc = location;

        OkHttpUtils
                .get()
                .url("http://api.map.baidu.com/geosearch/v3/nearby" + "?" + MapData.mCode)
                .addParams("ak", "6lKaHhn3GieVEaTZFCaEC3XrFdOFbHMX")
                .addParams("geotable_id", "160652")
                .addParams("location", location.longitude + "," + location.latitude)
                .addParams("radius", "3000")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(ActivityData.Hot, "网络连接失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                List<MyItem> items = new ArrayList<MyItem>();

                Root root = gson.fromJson(response, Root.class);

                if (root.getStatus() == 0) {
                    System.out.println("热点页面检索反馈正常");
                    //添加Mark点
                    for (Contents contents : root.getContents()) {
                        items.add(addMarkersData(contents, items));
                    }
                    MapData.mClusterManager.clearItems();
                    MapData.mClusterManager.addItems(items);
                    if (mapStatus != null) {        //这是为了解决重新填入Mark点后,必须手动缩放地图才能刷新显示出来的BUG而采用的奇葩办法...(强行-0.001的缩放级别- -|||)
                        MapData.mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom((float) (mapStatus.zoom - 0.001)).build()));
                        System.out.println("------------!null-----------");
                    }

                } else {
                    Toast.makeText(ActivityData.Hot, "" + response, Toast.LENGTH_LONG).show();
                }
            }

            private MyItem addMarkersData(Contents contents, List<MyItem> items) {
                LatLng llA = new LatLng(contents.getLocation().get(1), contents.getLocation().get(0));  //设置经纬度(纬度1,经度0)
                return new MyItem(llA)
                        .setmUserName("二营长")
                        .setmTime("9月17日")
                        .setmContent("你他娘的意大利炮呢···")
                        .setmLoc(contents.getTitle())               //设置地名
                        .setmState(contents.getState());            //设置目标状态 正常 危险 伤病
            }
        });

    }

}
