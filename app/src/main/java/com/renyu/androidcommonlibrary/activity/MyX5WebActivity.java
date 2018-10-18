package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.web.activity.X5WebActivity;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyX5WebActivity extends X5WebActivity {

    ImageButton ib_nav_right;

    @Override
    public WebView getWebView() {
        return findViewById(R.id.web_x5webview);
    }

    @Override
    public TextView getTitleView() {
        return findViewById(R.id.tv_nav_title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5web);

        ib_nav_right = findViewById(R.id.ib_nav_right);
        ib_nav_right.setImageResource(R.mipmap.ic_launcher);
        ib_nav_right.setOnClickListener(v -> {

        });
        initViews();
    }
}
