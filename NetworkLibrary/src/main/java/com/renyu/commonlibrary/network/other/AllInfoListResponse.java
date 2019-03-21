package com.renyu.commonlibrary.network.other;

import java.util.List;

public class AllInfoListResponse<T> {
    List<T> data;
    int result;
    String message;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
