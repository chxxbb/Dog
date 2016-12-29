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
import android.widget.LinearLayout;
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
    RelativeLayout hot_list_relativelayout;
    RelativeLayout.LayoutParams linearParams;   //用来动态设置高度
    double height;
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

        hot_list_relativelayout = (RelativeLayout) view.findViewById(R.id.hot_list_relativelayout);
        linearParams = (RelativeLayout.LayoutParams) hot_list_relativelayout.getLayoutParams();
        height = linearParams.height;   //保存一下控件初始高度

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


        hotspot_list_top_relativelayout = (RelativeLayout) view.findViewById(R.id.hotspot_list_top_relativelayout);
        hotspot_list_top_relativelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hot_list_relativelayout.setVisibility(View.GONE);
            }
        });


        listView = (ListView) view.findViewById(R.id.hotspot_list_listview);
        hotspotListViewAdapter = new HotspotListViewAdapter(getActivity());
        listView.setAdapter(hotspotListViewAdapter);
    }

    /**
     * 设置点聚合及相关的监听器
     * <p>
     * ClusterManager 代替了OnMapStatusChangeListener
     */
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
                setMarkers(cluster, null);

                if (linearParams.height != height) {    //若高度不等于初始高度,则说明已经被改变,改回来之.
                    linearParams.height = linearParams.height * 2;
                    hot_list_relativelayout.setLayoutParams(linearParams);
                }

                return false;
            }
        });
        MapData.mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                Toast.makeText(getActivity(),
                        "点击单个Item", Toast.LENGTH_SHORT).show();
                setMarkers(null, item);

                if (linearParams.height != height / 2) {    //若不等于初始高度除以二,则除以二以适应单个item的布局.
                    linearParams.height = linearParams.height / 2;
                    hot_list_relativelayout.setLayoutParams(linearParams);
                }

                return false;
            }
        });

    }

    /**
     * 根据传入不同的参数来添加Mark的详情数据到展示列表
     *
     * @param cluster 点聚合后的多个Mark数据列表
     * @param myItem  点聚合之前的单个Mark数据类
     */
    private void setMarkers(Cluster<MyItem> cluster, MyItem myItem) {

        hot_list_relativelayout.setVisibility(View.VISIBLE);    //显示展示列表
        list.clear();   //清除之前的列表数据

        if (cluster != null) {      //多个Mark列表
            for (MyItem item : cluster.getItems()) {
                adapterItemList = new AdapterItemList(0, item.mContent, item.mUserName, item.mLoc, item.mTime, item.mState, item.mBitmap);
                list.add(adapterItemList);
            }
        } else if (myItem != null) {    //单个Mark的数据
            adapterItemList = new AdapterItemList(0, myItem.mContent, myItem.mUserName, myItem.mLoc, myItem.mTime, myItem.mState, myItem.mBitmap);
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
            if (latitude != null && longitude != null) {    //若本次定位与上次定位后设置的"焦点"相距不超过XY轴任意一边0.005的经纬度单位,则不刷新Mark点.
                if (Math.abs(latitude - location.getLatitude()) > 0.005 || Math.abs(longitude - location.getLongitude()) > 0.005) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    addMarkers(latLng, null);
                }
            } else {    //若是页面的第一次定位,则会进入这里执行.
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
     * 向地图添加Mark点
     *
     * @param location  Mark点的坐标
     * @param mapStatus 当前的地图缩放级别,用来解决一个BUG
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
                    }

                } else {
                    Toast.makeText(ActivityData.Hot, "" + response, Toast.LENGTH_LONG).show();
                }
            }

            /**
             * 拿到单个点的数据,填充到Mark点里面去
             * @param contents  单个点的数据
             * @param items 所有已经填充好的点数据列表(一般没用,不知道为什么我当时要传进来- -|||)
             * @return 填充好后, 返回单个点的相关类
             */
            private MyItem addMarkersData(Contents contents, List<MyItem> items) {
                LatLng llA = new LatLng(contents.getLocation().get(1), contents.getLocation().get(0));  //设置经纬度(纬度1,经度0)
                return new MyItem(llA)
                        .setmUserName("二营长")
                        .setmContent("你他娘的意大利炮呢···")
                        .setmLoc(contents.getTitle())               //设置地名
                        .setmState(contents.getState())            //设置目标状态 正常 危险 伤病
                        .setmTime(contents.getTime())
                        .setmBitmap(contents.getImage().getBig());
            }
        });

    }

}
