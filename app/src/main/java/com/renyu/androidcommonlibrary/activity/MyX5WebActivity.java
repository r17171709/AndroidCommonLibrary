package com.renyu.androidcommonlibrary.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.impl.IX5WebApp;
import com.renyu.commonlibrary.web.activity.X5WebActivity;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyX5WebActivity extends X5WebActivity {
    IX5WebApp impl;

    // 文件上传使用
    private ValueCallback<Uri[]> uploadFilePathCallback;

    @Override
    public TextView getTitleView() {
        return findViewById(R.id.tv_nav_title);
    }

    @Override
    public ImageButton getNavClose() {
        return findViewById(R.id.ib_nav_close);
    }

    @Override
    public ImageButton getNavBack() {
        return findViewById(R.id.ib_nav_left);
    }

    @Override
    public FrameLayout getRootView() {
        return findViewById(R.id.layout_x5webview);
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 判断是否需要拦截
//        if (url.contains("weixin")) {
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        uploadFilePathCallback = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5web);

        getNavClose().setImageResource(R.mipmap.ic_web_close);
        getNavBack().setImageResource(R.mipmap.ic_arrow_black_left);
        initViews();
        impl = getIntent().getParcelableExtra("IWebApp");
        if (impl != null) {
            impl.setContext(this);
            impl.setWebView(webView);
            webView.addJavascriptInterface(impl, getIntent().getStringExtra("IWebAppName"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (null == uploadFilePathCallback) {
                return;
            }
            uploadFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            uploadFilePathCallback = null;
        } else if (resultCode == RESULT_CANCELED) {
            // 没有选择图片要重置
            if (uploadFilePathCallback != null) {
                uploadFilePathCallback.onReceiveValue(null);
                uploadFilePathCallback = null;
            }
        }
    }
}
