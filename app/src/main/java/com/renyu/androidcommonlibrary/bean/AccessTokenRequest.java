package com.renyu.androidcommonlibrary.bean;

public class AccessTokenRequest {
    private String city;
    private int timestamp;
    private String app_id;
    private String rand_str;
    private String signature;
    private String device_id;

    public AccessTokenRequest(String city, int timestamp, String app_id, String rand_str, String signature, String device_id) {
        this.city = city;
        this.timestamp = timestamp;
        this.app_id = app_id;
        this.rand_str = rand_str;
        this.signature = signature;
        this.device_id = device_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getRand_str() {
        return rand_str;
    }

    public void setRand_str(String rand_str) {
        this.rand_str = rand_str;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}

