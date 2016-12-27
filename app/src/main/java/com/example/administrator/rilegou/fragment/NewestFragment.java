package com.example.administrator.rilegou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.NearbyListViewAdapter;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Contents;
import com.example.administrator.rilegou.data.Map_Lbs_Json_Data.Root;
import com.example.administrator.rilegou.data.MyMessageItem;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class NewestFragment extends Fragment {

    View view;

    ListView listView;

    //定位服务
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();

    //数据
    NearbyListViewAdapter adapter;
    List<MyMessageItem> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_newest, null);
        findView();

        init();

        return view;
    }

    private void init() {
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

    private void findView() {

        listView = (ListView) view.findViewById(R.id.lv_newest);

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
                    .url("http://api.map.baidu.com/geosearch/v3/local" + "?" + MapData.mCode)
                    .addParams("ak", MapData.Ak)
                    .addParams("geotable_id", MapData.ServiceNumber)
                    .addParams("location", location.getLongitude() + "," + location.getLatitude())
                    .addParams("sortby", "distance:1")
                    .addParams("region", location.getCountry())
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

                            if (root.getStatus() == 0) {
                                System.out.println("最新页面检索反馈正常");
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
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                mLocClient.stop();
                            } else {
                                System.out.println(response);
                            }
                        }
                    });

        }


        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
