package com.renyu.commonlibrary.baseact;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.params.InitParams;

/**
 * Created by renyu on 16/2/16.
 */
public class WebActivity extends BaseActivity {

    WebView web_webview;
    TextView tv_nav_title;

    @Override
    public int setStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initParams() {
        tv_nav_title = (TextView) findViewById(R.id.tv_nav_title);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            tv_nav_title.setText(getIntent().getStringExtra("title"));
        }
        web_webview = (WebView) findViewById(R.id.web_webview);
        web_webview.setSaveEnabled(true);
        web_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        web_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return false;
            }
        });
        web_webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        web_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings=web_webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        web_webview.loadUrl(getIntent().getStringExtra("url"));
        findViewById(R.id.ib_nav_left).setOnClickListener(v -> finish());
    }

    @Override
    public int initViews() {
        return R.layout.activity_web;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        web_webview.removeAllViews();
        web_webview.destroy();
    }

    @Override
    public void onBackPressed() {
        //只有帮助中心可以跳转
        if (web_webview.canGoBack() && getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
            web_webview.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

}
