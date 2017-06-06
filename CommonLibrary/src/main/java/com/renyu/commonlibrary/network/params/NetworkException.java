package com.renyu.commonlibrary.network.params;

/**
 * Created by renyu on 2017/2/20.
 */

public class NetworkException extends Exception {
    int result;
    String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
