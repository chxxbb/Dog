package com.example.administrator.rilegou.data;

import android.view.View;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Chen on 2016/12/15.
 */
public class MapData {
    //地图相关
    public static MapView mMapView;
    public static BaiduMap mBaiduMap;
    public static LocationClient mLocClient;
    public static View view;
    public static String path;
    public static String mCode = "mcode=77:27:1B:6B:44:71:FE:CB:78:27:F6:2E:F9:1B:23:AC:5C:2A:7E:DA;com.example.administrator.rilegou";
    public static String ServiceAk = "Y8uhZLWpinsI8IjhhI8VLqZfNKDfRg0G";
    public static String Ak = "6lKaHhn3GieVEaTZFCaEC3XrFdOFbHMX";
    public static String ServiceId = "160652";  //云检索数据库ID
    public static String LocType = "3";
    public static String ServiceUrl = "http://api.map.baidu.com/geodata/v3/poi/create";     //云检索的服务器url
    public static LatLng now_mark_loc;    //当前点聚合的定位点
    public static ClusterManager<MyItem> mClusterManager;
}
