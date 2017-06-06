package com.renyu.androidcommonlibrary.bean;

import com.renyu.commonlibrary.network.params.ResponseList;

import java.util.List;

/**
 * Created by renyu on 2017/6/6.
 */

public class ExampleResponseList<T> implements ResponseList<T> {
    List<T> data;
    int result;
    String message;

    public List<T> getData() {
        return data;
    }

    @Override
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
