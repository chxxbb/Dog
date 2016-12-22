package com.example.administrator.rilegou.data;

/**
 * Created by Chen on 2016/12/14.
 */

import android.graphics.Bitmap;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.rilegou.R;

/**
 * 每个Marker点，包含Marker点坐标以及图标
 */
public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public String mContent;
    public Bitmap mBitmap;
    public Bitmap mHeadBitmap;
    public String mUserName;
    public String mLoc;
    public String mTime;
    public int mState;
    public int mLove;
    public int mComments;

    public MyItem(LatLng latLng) {
        mPosition = latLng;
    }


    public String getmContent() {
        return mContent;
    }

    public MyItem setmContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public MyItem setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
        return this;
    }

    public Bitmap getmHeadBitmap() {
        return mHeadBitmap;
    }

    public MyItem setmHeadBitmap(Bitmap mHeadBitmap) {
        this.mHeadBitmap = mHeadBitmap;
        return this;
    }

    public String getmUserName() {
        return mUserName;
    }

    public MyItem setmUserName(String mUserName) {
        this.mUserName = mUserName;
        return this;
    }

    public String getmLoc() {
        return mLoc;
    }

    public MyItem setmLoc(String mLoc) {
        this.mLoc = mLoc;
        return this;
    }

    public String getmTime() {
        return mTime;
    }

    public MyItem setmTime(String mTime) {
        this.mTime = mTime;
        return this;
    }

    public int getmState() {
        return mState;
    }

    public MyItem setmState(int mState) {
        this.mState = mState;
        return this;
    }

    public int getmLove() {
        return mLove;
    }

    public MyItem setmLove(int mLove) {
        this.mLove = mLove;
        return this;
    }

    public int getmComments() {
        return mComments;
    }

    public MyItem setmComments(int mComments) {
        this.mComments = mComments;
        return this;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
    }
}