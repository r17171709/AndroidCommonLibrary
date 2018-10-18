package com.renyu.androidcommonlibrary.bean;

import com.google.gson.annotations.SerializedName;
import com.renyu.commonlibrary.network.impl.IResponse;

/**
 * Created by renyu on 2017/6/6.
 */

public class ExampleResponse<T> implements IResponse<T> {

    @SerializedName("data")
    T data;
    @SerializedName("result")
    int result;
    @SerializedName("msg")
    String message;

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getResult() {
        return result;
    }

    @Override
    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
