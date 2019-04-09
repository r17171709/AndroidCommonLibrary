package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.web.activity.X5WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyX5WebActivity extends X5WebActivity {
    @Override
    public WebView getWebView() {
        return findViewById(R.id.web_x5webview);
    }

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
    public void onPageFinished(String url) {
        ToastUtils.showShort(url);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5web);

        getNavClose().setImageResource(R.mipmap.ic_web_close);
        getNavBack().setImageResource(R.mipmap.ic_arrow_black_left);
        initViews();
    }
}
