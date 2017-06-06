package com.renyu.commonlibrary.network.params;

import java.util.List;

/**
 * Created by renyu on 15/12/25.
 */
public class ResponseList<T> {
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
