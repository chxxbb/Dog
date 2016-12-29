package com.example.administrator.rilegou.data;

import android.graphics.Bitmap;

/**
 * Created by Chen on 2016/12/17.
 */
public class AdapterItemList {
    //设定List的布局状态
    public static final int Content = 0;
    //设置一共有多少种布局
    public static final int Content_COUNT = 1;

    private String mContent;
    private String mBitmap;
    private String mHeadBitmap;
    private String mUserName;
    private String mLoc;
    private String mTime;
    private int mState;
    private int mLove;
    private int mComments;

    private int type;

    public AdapterItemList(int type) {
        this.type = type;
    }

    public AdapterItemList(int type, String mContent, String mUserName, String mLoc, String mTime, int mState, String mBitmap) {
        this.mContent = mContent;
        this.mUserName = mUserName;
        this.mLoc = mLoc;
        this.mTime = mTime;
        this.mState = mState;
        this.type = type;
        this.mBitmap = mBitmap;
    }

    public int getType() {
        return type;
    }

    public String getmContent() {
        return mContent;
    }

    public String getmBitmap() {
        return mBitmap;
    }

    public String getmHeadBitmap() {
        return mHeadBitmap;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmLoc() {
        return mLoc;
    }

    public String getmTime() {
        return mTime;
    }

    public int getmState() {
        return mState;
    }

    public int getmLove() {
        return mLove;
    }

    public int getmComments() {
        return mComments;
    }
}
