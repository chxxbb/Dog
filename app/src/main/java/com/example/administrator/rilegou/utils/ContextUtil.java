package com.example.administrator.rilegou.utils;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Chen on 2016/6/8.
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.color.a) //加载中图片
//                .showImageOnFail(R.drawable.defeated_picture) //加载失败图片
//                .showImageForEmptyUri(R.drawable.defeated_picture)  //图片地址失效图片
                .cacheInMemory(true) // 打开内存缓存
                .cacheOnDisk(true) // 打开硬盘缓存
                .resetViewBeforeLoading(true) // 在ImageView加载前清除它上面的图片
                .build();
        // ImageLoader配置
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheSize(5 * 1024 * 1024) // 内存缓存
                .defaultDisplayImageOptions(options) // 设置显示选项
                .threadPoolSize(3)
                .build();

        // 初始化ImageLoad
        ImageLoader.getInstance().init(config);

        SDKInitializer.initialize(getApplicationContext());


    }
}
