package com.renyu.commonlibrary.network.other;

/**
 * Created by renyu on 2017/2/20.
 */

public class NetworkException extends Exception {
    private int result;
    private String message;

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
