package com.example.administrator.rilegou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.activity.AttentionActivity;
import com.example.administrator.rilegou.adapter.HomeViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class MyPageFragment extends Fragment {
    View view;
    FrameLayout fragment;

    MyPageDynamicFragment myPageDynamicFragment;
    MyPageProductFragment myPageProductFragment;

    LinearLayout ll_myPage_left_view;
    RelativeLayout rl_myPage_right_view;

    FragmentManager fm;
    FragmentTransaction ft;

    ScrollView scrollView;

    RelativeLayout mypage_attention_relativelayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypage, null);

        findView();

        viewPageView();

        return view;
    }


    private void findView() {
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        scrollView.setVerticalScrollBarEnabled(false);

        fragment = (FrameLayout) view.findViewById(R.id.fragment);

        ll_myPage_left_view = (LinearLayout) view.findViewById(R.id.ll_myPage_left_view);

        rl_myPage_right_view = (RelativeLayout) view.findViewById(R.id.rl_myPage_right_view);

        mypage_attention_relativelayout = (RelativeLayout) view.findViewById(R.id.mypage_attention_relativelayout);

        ll_myPage_left_view.setOnClickListener(listener);

        rl_myPage_right_view.setOnClickListener(listener);

        mypage_attention_relativelayout.setOnClickListener(listener);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("111", "111");
        scrollView.smoothScrollTo(1, 1);

    }

    private void viewPageView() {
        fm = getChildFragmentManager();
        ll_myPage_left_view.performClick();


    }


    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (myPageDynamicFragment != null) fragmentTransaction.hide(myPageDynamicFragment);
        if (myPageProductFragment != null) fragmentTransaction.hide(myPageProductFragment);

    }


    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ft = fm.beginTransaction();
            hideAllFragment(ft);

            switch (v.getId()) {
                case R.id.ll_myPage_left_view:

                    ll_myPage_left_view.setBackgroundResource(R.drawable.mypage_left_onclick_background);
                    rl_myPage_right_view.setBackgroundResource(R.drawable.mypage_right_unclick_background);

                    if (myPageDynamicFragment == null) {
                        myPageDynamicFragment = new MyPageDynamicFragment();
                        ft.add(R.id.fragment, myPageDynamicFragment);
                    } else {
                        ft.show(myPageDynamicFragment);
                    }

                    scrollView.smoothScrollTo(1, 1);
                    break;
                case R.id.rl_myPage_right_view:

                    ll_myPage_left_view.setBackgroundResource(R.drawable.mypage_left_unclick_background);
                    rl_myPage_right_view.setBackgroundResource(R.drawable.mypage_right_onclick_background);

                    if (myPageProductFragment == null) {
                        myPageProductFragment = new MyPageProductFragment();
                        ft.add(R.id.fragment, myPageProductFragment);
                    } else {
                        ft.show(myPageProductFragment);
                    }


                    scrollView.smoothScrollTo(1, 1);
                    break;

                case R.id.mypage_attention_relativelayout:
                    Intent intent = new Intent(getActivity(), AttentionActivity.class);
                    startActivity(intent);
                    break;

            }

            ft.commitAllowingStateLoss();
        }
    };


}
