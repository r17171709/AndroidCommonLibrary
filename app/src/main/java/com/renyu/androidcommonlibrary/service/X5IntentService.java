package com.renyu.androidcommonlibrary.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

public class X5IntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public X5IntentService(String name) {
        super(name);
    }

    public X5IntentService(){
        super("X5IntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initX5Web();
    }

    public void initX5Web() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d(getPackageName(), " x5加载成功：" + arg0);
            }

            @Override
            public void onCoreInitFinished() {

            }
        };
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}