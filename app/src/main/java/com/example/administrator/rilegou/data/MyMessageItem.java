package com.example.administrator.rilegou.data;

/**
 * Created by Chen on 2016/12/23.
 */
public class MyMessageItem {
    private String name;
    private String Address;
    private int state;
    private String userImage;
    private String contentImage;
    private String time;
    private String content;

    public String getName() {
        return name;
    }

    public MyMessageItem setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return Address;
    }

    public MyMessageItem setAddress(String address) {
        Address = address;
        return this;
    }

    public int getState() {
        return state;
    }

    public MyMessageItem setState(int state) {
        this.state = state;
        return this;
    }

    public String getUserImage() {
        return userImage;
    }

    public MyMessageItem setUserImage(String userImage) {
        this.userImage = userImage;
        return this;
    }

    public String getContentImage() {
        return contentImage;
    }

    public MyMessageItem setContentImage(String contentImage) {
        this.contentImage = contentImage;
        return this;
    }

    public String getTime() {
        return time;
    }

    public MyMessageItem setTime(String time) {
        this.time = time;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MyMessageItem setContent(String content) {
        this.content = content;
        return this;
    }
}
