package com.renyu.commonlibrary.network.impl;

/**
 * Created by renyu on 15/12/16.
 */
public interface IResponse<T> {

    T getData();

    void setData(T data);

    int getResult();

    void setResult(int result);

    String getMessage();

    void setMessage(String message);
}
