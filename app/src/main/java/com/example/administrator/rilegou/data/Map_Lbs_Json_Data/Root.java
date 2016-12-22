package com.example.administrator.rilegou.data.Map_Lbs_Json_Data;

import java.util.List;

public class Root {
    private int status;

    private int total;

    private int size;

    private List<Contents> contents;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }

    public void setContents(List<Contents> contents) {
        this.contents = contents;
    }

    public List<Contents> getContents() {
        return this.contents;
    }

}