package com.renyu.commonlibrary.network;

import okhttp3.OkHttpClient;

/**
 * Created by RG on 2015/10/15.
 */
public class OKHttpHelper {

    private static volatile OKHttpHelper instance = null;
    private OKHttpUtils okHttpUtils;

    private OKHttpHelper() {
        okHttpUtils = new OKHttpUtils();
    }

    private OKHttpHelper(OkHttpClient okHttpClient) {
        okHttpUtils = new OKHttpUtils(okHttpClient);
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

    public static OKHttpHelper getInstanceWithOKHttpClient(OkHttpClient okHttpClient) {
        if (instance == null) {
            synchronized (OKHttpHelper.class) {
                if (instance == null) {
                    instance = new OKHttpHelper(okHttpClient);
                }
            }
        }
        return instance;
    }

    public OKHttpUtils getOkHttpUtils() {
        return okHttpUtils;
    }
}
