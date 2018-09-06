package com.renyu.androidcommonlibrary.impl;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.renyu.commonlibrary.impl.WebAppImpl;

/**
 * Created by renyu on 2017/8/14.
 */

public class WebAppInterface implements Parcelable, WebAppImpl {

    Context context;
    WebView webView;

    public WebAppInterface() {
        super();
    }

    protected WebAppInterface(Parcel in) {

    }

    public static final Creator<WebAppInterface> CREATOR = new Creator<WebAppInterface>() {
        @Override
        public WebAppInterface createFromParcel(Parcel in) {
            return new WebAppInterface(in);
        }

        @Override
        public WebAppInterface[] newArray(int size) {
            return new WebAppInterface[size];
        }
    };

    @JavascriptInterface
    public void showToast(String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    // SDK可以通过反射的方式去获取所有需onActivityResult处理的方法
    public void onActivityResult_111() {
        webView.loadUrl("javascript:showAndroidToast('waawo')");
    }

    @JavascriptInterface
    public void call() {
        Toast.makeText(context, "call", Toast.LENGTH_SHORT).show();
    }

    public void call1(String string) {
        webView.loadUrl("javascript:onData('"+string+"')");
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
        this.context=context;
    }

    @Override
    public void setWebView(WebView webView) {
        this.webView=webView;
    }
}
