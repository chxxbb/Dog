package com.example.administrator.rilegou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.HomeViewPageAdapter;
import com.example.administrator.rilegou.fragment.AttentionFragment;
import com.example.administrator.rilegou.fragment.FansFragment;

import java.util.ArrayList;
import java.util.List;

public class AttentionActivity extends AppCompatActivity {

    ViewPager vp_attention;

    List<Fragment> list = new ArrayList<>();

    HomeViewPageAdapter adapter;

    View view1, view2;

    RelativeLayout rl_attention,rl_fans;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);

        findView();

        init();
    }


    private void findView() {
        vp_attention = (ViewPager) findViewById(R.id.vp_attention);

        view1 = findViewById(R.id.view1);

        view2 = findViewById(R.id.view2);

        rl_attention= (RelativeLayout) findViewById(R.id.rl_attention);

        rl_fans= (RelativeLayout) findViewById(R.id.rl_fans);

        vp_attention.addOnPageChangeListener(viewPageListener);

        rl_attention.setOnClickListener(listener);

        rl_fans.setOnClickListener(listener);
    }


    private void init() {
        AttentionFragment attentionFragment = new AttentionFragment();

        FansFragment fansFragment = new FansFragment();

        list.add(attentionFragment);

        list.add(fansFragment);

        adapter = new HomeViewPageAdapter(getSupportFragmentManager(), list);

        vp_attention.setAdapter(adapter);
        vp_attention.setOffscreenPageLimit(list.size());
    }


    private ViewPager.OnPageChangeListener viewPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                view1.setVisibility(View.VISIBLE);

                view2.setVisibility(View.GONE);
            } else {
                view1.setVisibility(View.GONE);

                view2.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_fans:
                    view2.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                    vp_attention.setCurrentItem(1, true);

                    break;

                case R.id.rl_attention:
                    view2.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                    vp_attention.setCurrentItem(0, true);

                    break;
            }
        }
    };
}
