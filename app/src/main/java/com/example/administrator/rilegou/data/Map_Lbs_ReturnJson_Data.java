package com.example.administrator.rilegou.data;

/**
 * Created by Chen on 2016/12/24.
 * 用于获取百度地图反馈的信息
 * status 为返回码
 * id     为本次操作的ID
 * message为返回文本消息
 */
public class Map_Lbs_ReturnJson_Data {
    private int status;

    private int id;

    private String message;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
