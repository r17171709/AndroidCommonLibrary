package com.renyu.commonlibrary.network.params;

/**
 * Created by renyu on 15/12/16.
 */
public class Response<T> {

    T data;
    int result;
    String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
