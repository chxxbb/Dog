package com.example.administrator.rilegou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.rilegou.R;
import com.example.administrator.rilegou.adapter.AttentionListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/21 0021.
 */
public class AttentionFragment extends Fragment {

    View view;

    ListView lv_attention_List;

    AttentionListAdapter adapter;

    List<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention, null);

        findView();

        init();
        return view;
    }


    private void findView() {
        lv_attention_List = (ListView) view.findViewById(R.id.lv_attention_List);

        lv_attention_List.setVerticalScrollBarEnabled(false);
    }


    private void init() {
        for (int i = 0; i < 10; i++) {
            list.add(1 + "");
        }

        adapter = new AttentionListAdapter(getContext(), list);

        lv_attention_List.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

}
