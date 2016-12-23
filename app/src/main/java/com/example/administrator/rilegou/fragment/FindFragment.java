package com.example.administrator.rilegou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.rilegou.adapter.HomeViewPageAdapter;
import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.data.MapData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class FindFragment extends Fragment {
    View view;

    ViewPager viewPager;

    List<Fragment> flist = new ArrayList<>();

    HomeViewPageAdapter adapter;

    View view_hot, view_nearby, view_newest;

    RelativeLayout rl_hot, rl_nearby, rl_newest;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, null);


        findView();

        init();

        return view;
    }


    private void findView() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        view_hot = view.findViewById(R.id.view_hot);
        view_nearby = view.findViewById(R.id.view_nearby);
        view_newest = view.findViewById(R.id.view_newest);

        rl_hot = (RelativeLayout) view.findViewById(R.id.rl_hot);

        rl_nearby = (RelativeLayout) view.findViewById(R.id.rl_nearby);

        rl_newest = (RelativeLayout) view.findViewById(R.id.rl_newest);

        viewPager.addOnPageChangeListener(viewPagerListener);

        rl_hot.setOnClickListener(listener);

        rl_nearby.setOnClickListener(listener);

        rl_newest.setOnClickListener(listener);

    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_hot:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.rl_nearby:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.rl_newest:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
        }
    };

    private void init() {
        HotFragment hotFragment = new HotFragment();
        NearbyFragment nearbyFragment = new NearbyFragment();
        NewestFragment newestFragment = new NewestFragment();

        flist.add(hotFragment);
        flist.add(nearbyFragment);
        flist.add(newestFragment);

        adapter = new HomeViewPageAdapter(getChildFragmentManager(), flist);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(flist.size());

        viewPager.setCurrentItem(1, true);
    }


    private ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                view_hot.setVisibility(View.VISIBLE);
                view_nearby.setVisibility(View.GONE);
                view_newest.setVisibility(View.GONE);
            } else if (position == 1) {
                view_hot.setVisibility(View.GONE);
                view_nearby.setVisibility(View.VISIBLE);
                view_newest.setVisibility(View.GONE);
            } else {
                view_hot.setVisibility(View.GONE);
                view_nearby.setVisibility(View.GONE);
                view_newest.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
