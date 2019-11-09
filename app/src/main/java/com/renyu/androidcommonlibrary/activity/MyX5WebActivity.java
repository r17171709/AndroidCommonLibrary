package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
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
}
