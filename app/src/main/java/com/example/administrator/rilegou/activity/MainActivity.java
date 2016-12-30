package com.example.administrator.rilegou.activity;


import android.app.ActivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

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

    private long mExitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置地图的样式
        MapData.mMapView.setCustomMapStylePath(MapData.path);

        MapData.view = getLayoutInflater().inflate(R.layout.fragment_hotspot, null);

        //地图初始化
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

    /**
     * 监听返回键,若已打开热点页面的展示列表的情况下点击返回键,则关闭展示列表而不退出Activity
     * 2000毫秒内点击两次返回键才退出.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (hotspotFragment != null) {      //判断是否创建了热点页面
            if (hotspotFragment.isListVisibility()) {   //判断热点界面的展示列表是否处于显示状态
                /**
                 * 2000秒内点击两次返回键才退出程序
                 */
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if ((System.currentTimeMillis() - mExitTime) > 2000) {
                        Object mHelperUtils;
                        Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        mExitTime = System.currentTimeMillis();

                    } else {
                        finish();
                    }
                    return true;
                }
                return super.onKeyDown(keyCode, event);
            } else {    //关闭热点界面的展示列表
                hotspotFragment.exitList();
                return false;
            }
        } else {    //没有创建热点界面
            /**
             * 2000秒内点击两次返回键才退出程序
             */
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    finish();
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 百度地图的生命周期控制
     */
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
        if (MapData.mLocClient != null) {       //若从未进入任何需要定位的页面,则不需要销毁
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            MapData.mLocClient.stop();
            // 关闭定位图层
            MapData.mBaiduMap.setMyLocationEnabled(false);
        }
        MapData.mMapView.onDestroy();
        MapData.mMapView = null;
        MapData.mBaiduMap = null;

//        //获取应用程序管理器
//        ActivityManager manager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
//        System.out.println("------Kill进程---------");
//        int pid = android.os.Process.myPid();    //获取当前应用程序的PID
//        android.os.Process.killProcess(pid);
        System.gc();
        super.onDestroy();
    }


}
