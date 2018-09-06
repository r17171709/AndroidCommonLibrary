package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.impl.WebAppInterface;
import com.renyu.commonlibrary.impl.WebAppImpl;
import com.renyu.commonlibrary.views.web.WebActivity;

/**
 * Created by Administrator on 2017/9/13.
 */

public class MyWebActivity extends WebActivity {

    ImageButton ib_nav_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ib_nav_right = findViewById(R.id.ib_nav_right);
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
        ib_nav_right.setOnClickListener(v -> {
            WebAppImpl impl=getIntent().getParcelableExtra("WebAppImpl");
            ((WebAppInterface) impl).call1();
        });
    }
}
