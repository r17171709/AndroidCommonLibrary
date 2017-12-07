package com.renyu.commonlibrary.network;

/**
 * Created by RG on 2015/10/15.
 */
public class OKHttpHelper {

    private static volatile OKHttpHelper instance = null;
    private OKHttpUtils okHttpUtils = null;

    private OKHttpHelper() {
        okHttpUtils = new OKHttpUtils();
    }

    public static OKHttpHelper getInstance() {
        if (instance == null) {
            synchronized (OKHttpHelper.class) {
                if (instance == null) {
                    instance = new OKHttpHelper();
                }
            }
        }
        return instance;
    }

    public OKHttpUtils getOkHttpUtils() {
        return okHttpUtils;
    }
}
