package com.renyu.commonlibrary.network.params;

import java.util.List;

/**
 * Created by renyu on 15/12/25.
 */
public interface ResponseList<T> {
    List<T> getData();

    void setData(List<T> data);

    int getResult();

    void setResult(int result);

    String getMessage();

    void setMessage(String message);
}
