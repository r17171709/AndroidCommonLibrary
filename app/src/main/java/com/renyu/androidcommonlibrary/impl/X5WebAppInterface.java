package com.renyu.androidcommonlibrary.impl;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;


/**
 * Created by renyu on 2017/8/14.
 */

public class X5WebAppInterface implements Parcelable, IX5WebApp {

    private Context context;
    private WebView webView;

    public X5WebAppInterface() {
        super();
    }

    private X5WebAppInterface(Parcel in) {

    }

    public static final Creator<X5WebAppInterface> CREATOR = new Creator<X5WebAppInterface>() {
        @Override
        public X5WebAppInterface createFromParcel(Parcel in) {
            return new X5WebAppInterface(in);
        }

        @Override
        public X5WebAppInterface[] newArray(int size) {
            return new X5WebAppInterface[size];
        }
    };

    @JavascriptInterface
    public void showToast(String string) {
//        ((WebActivity) context).startActivityForResult(new Intent(context, LoginActivity.class), 111);
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    // 可以通过反射的方式去获取所有需onActivityResult处理的方法
    public void onActivityResult_111() {
        webView.loadUrl("javascript:showAndroidToast('waawo')");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setWebView(WebView webView) {
        this.webView = webView;
    }
}
