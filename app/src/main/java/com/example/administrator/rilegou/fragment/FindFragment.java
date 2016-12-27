package com.example.administrator.rilegou.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.rilegou.activity.NewMessageActivity;
import com.example.administrator.rilegou.adapter.HomeViewPageAdapter;
import com.example.administrator.rilegou.R;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */
public class FindFragment extends TakePhotoFragment {
    View view;

    ViewPager viewPager;

    List<Fragment> flist = new ArrayList<>();

    HomeViewPageAdapter adapter;

    View view_hot, view_nearby, view_newest;

    RelativeLayout rl_hot, rl_nearby, rl_newest;

    ImageView home_camera;
    TakePhoto takePhoto;

    private static final int MENU_ITEM_COUNTER = Menu.FIRST;
    File file;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_ITEM_COUNTER + 1, 0, "照相");
        menu.add(0, MENU_ITEM_COUNTER + 2, 0, "相册");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_COUNTER + 1:

                takePhoto = getTakePhoto();

                file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");

                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                Uri imageUri = Uri.fromFile(file);

                takePhoto.onPickFromCapture(imageUri);

                break;
            case MENU_ITEM_COUNTER + 2:

                takePhoto = getTakePhoto();
                takePhoto.onPickFromDocuments();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

        home_camera = (ImageView) view.findViewById(R.id.home_camera);
        registerForContextMenu(home_camera);

        viewPager.addOnPageChangeListener(viewPagerListener);

        rl_hot.setOnClickListener(listener);

        rl_nearby.setOnClickListener(listener);

        rl_newest.setOnClickListener(listener);

        home_camera.setOnClickListener(listener);

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
                case R.id.home_camera:
                    home_camera.showContextMenu();
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

    @Override
    public void takeSuccess(TResult result) {
        TImage tImage = result.getImage();
        String imageStr = tImage.getOriginalPath();
        Intent intent = new Intent(getActivity(), NewMessageActivity.class);
        intent.putExtra("image", imageStr);
        intent.putExtra("file", file);
        startActivity(intent);
        super.takeSuccess(result);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        System.out.println("图片获取失败");
        super.takeFail(result, msg);
    }

    @Override
    public void takeCancel() {
        System.out.println("取消获取");
        super.takeCancel();
    }

}
