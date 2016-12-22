package com.example.administrator.rilegou.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.baidu.mapapi.map.MapView;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.MapData;
import com.example.administrator.rilegou.fragment.FindFragment;
import com.example.administrator.rilegou.fragment.HotspotFragment;
import com.example.administrator.rilegou.fragment.MyPageFragment;


public class MainActivity extends AppCompatActivity {
    FrameLayout fragment;

    FragmentManager fm;
    FragmentTransaction ft;

    FindFragment findFragment;
    HotspotFragment hotspotFragment;
    MyPageFragment myPageFragment;

    RadioButton rb_home_find, rb_home_hotspot, rb_home_mypage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapData.mMapView.setCustomMapStylePath(MapData.path);

        MapData.view = getLayoutInflater().inflate(R.layout.fragment_hotspot, null);

        // 地图初始化
        MapData.mMapView = (MapView) MapData.view.findViewById(R.id.bmapView);
        //隐藏缩放按钮
        MapData.mMapView.showZoomControls(false);

        MapData.mBaiduMap = MapData.mMapView.getMap();

        findView();

        page();

    }


    private void findView() {
        fragment = (FrameLayout) findViewById(R.id.fragment);
        rb_home_find = (RadioButton) findViewById(R.id.rb_home_find);
        rb_home_hotspot = (RadioButton) findViewById(R.id.rb_home_hotspot);
        rb_home_mypage = (RadioButton) findViewById(R.id.rb_home_mypage);
        rb_home_find.setChecked(true);

        rb_home_find.setOnClickListener(listener);
        rb_home_hotspot.setOnClickListener(listener);
        rb_home_mypage.setOnClickListener(listener);
    }

    private void page() {
        fm = getSupportFragmentManager();
        rb_home_find.performClick();
    }


    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (findFragment != null) fragmentTransaction.hide(findFragment);
        if (hotspotFragment != null) fragmentTransaction.hide(hotspotFragment);
        if (myPageFragment != null) fragmentTransaction.hide(myPageFragment);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ft = fm.beginTransaction();
            hideAllFragment(ft);
            switch (v.getId()) {
                case R.id.rb_home_find:
                    if (findFragment == null) {
                        findFragment = new FindFragment();
                        ft.add(R.id.fragment, findFragment);
                    } else {
                        ft.show(findFragment);
                    }
                    break;
                case R.id.rb_home_hotspot:
                    if (hotspotFragment == null) {
                        hotspotFragment = new HotspotFragment();
                        ft.add(R.id.fragment, hotspotFragment);
                    } else {
                        ft.show(hotspotFragment);
                    }
                    break;

                case R.id.rb_home_mypage:

                    if (myPageFragment == null) {
                        myPageFragment = new MyPageFragment();
                        ft.add(R.id.fragment, myPageFragment);
                    } else {
                        ft.show(myPageFragment);
                    }

                    break;


            }
            ft.commit();
        }

    };

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        MapData.mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        MapData.mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        MapData.mLocClient.stop();
        // 关闭定位图层
        MapData.mBaiduMap.setMyLocationEnabled(false);

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        MapData.mMapView.onDestroy();
        MapData.mMapView = null;
        super.onDestroy();
    }


}
