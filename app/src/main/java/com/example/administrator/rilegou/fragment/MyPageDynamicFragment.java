package com.example.administrator.rilegou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.MyPageDynamicAdapter;
import com.example.administrator.rilegou.view.ListViewForScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class MyPageDynamicFragment extends Fragment {
    View view;

    ListViewForScrollView lv_myPage_dynamic;

    MyPageDynamicAdapter adapter;

    List<String> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mypagedynamic, null);

        findView();

        page();

        return view;
    }


    private void findView() {

        lv_myPage_dynamic = (ListViewForScrollView) view.findViewById(R.id.lv_myPage_dynamic);

    }

    private void page() {

        for (int i = 0; i < 3; i++) {
            data.add(i + "");
        }

        adapter = new MyPageDynamicAdapter(getContext(), data);

        lv_myPage_dynamic.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
}
