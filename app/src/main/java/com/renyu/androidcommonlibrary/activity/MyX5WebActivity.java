package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.views.web.X5WebActivity;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyX5WebActivity extends X5WebActivity {

    ImageButton ib_nav_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ib_nav_right = (ImageButton) findViewById(R.id.ib_nav_right);
        ib_nav_right.setImageResource(R.mipmap.ic_launcher);
        web_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    tv_nav_title.setText(getIntent().getStringExtra("title"));
                }
                else {
                    tv_nav_title.setText(title);
                }
            }
        });
        ib_nav_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
