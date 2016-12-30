package com.example.administrator.rilegou.data;

/**
 * Created by Chen on 2016/12/30.
 */

public class HotAdapterItem {
    //设定List的布局状态
    public static final int BannerAndContent = 0;
    public static final int HotContent = 1;
    //设置一共有多少种布局
    public static final int Content_COUNT = 2;

    private String image;
    private int state;
    private String address;
    private String content;
    private String time;

    private int type;

    public HotAdapterItem(int type) {
        this.type = type;
    }

    /**
     * 创建时,传入需要加载的布局编码和数据
     *
     * @param type    需要调用的布局编码
     * @param image   内容图片
     * @param state   动物状态
     * @param address 地址信息
     * @param content 文本内容
     * @param time    发布时间
     */
    public HotAdapterItem(int type, String image, int state, String address, String content, String time) {
        this.type = type;
        this.image = image;
        this.state = state;
        this.address = address;
        this.content = content;
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public int getState() {
        return state;
    }

    public String getAddress() {
        return address;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
