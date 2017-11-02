package com.renyu.androidcommonlibrary.impl;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.renyu.commonlibrary.impl.WebAppImpl;
import com.tencent.smtt.sdk.WebView;

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
        this.context=context;
    }

    @Override
    public void setWebView(WebView webView) {
        this.webView=webView;
    }
}
