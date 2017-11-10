package com.renyu.commonlibrary.views.web;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.impl.X5WebAppImpl;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by renyu on 16/2/16.
 */
public class X5WebActivity extends BaseActivity {

    ImageButton ib_nav_left;
    public WebView web_webview;
    public TextView tv_nav_title;

    X5WebAppImpl impl;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    public void initParams() {
        ib_nav_left = (ImageButton) findViewById(R.id.ib_nav_left);
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_black_left);
        ib_nav_left.setOnClickListener(v -> finish());
        tv_nav_title = (TextView) findViewById(R.id.tv_nav_title);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            tv_nav_title.setText(getIntent().getStringExtra("title"));
        }
        web_webview = (WebView) findViewById(R.id.web_x5webview);
        web_webview.setSaveEnabled(true);
        web_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
        web_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings=web_webview.getSettings();
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setBlockNetworkImage(false);
        settings.setBlockNetworkLoads(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        impl=getIntent().getParcelableExtra("WebAppImpl");
        if (impl!=null) {
            impl.setContext(this);
            impl.setWebView(web_webview);
            web_webview.addJavascriptInterface(impl, getIntent().getStringExtra("WebAppImplName"));
        }
        web_webview.removeJavascriptInterface("searchBoxJavaBridge_");
        web_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        });
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
        return R.layout.activity_x5web;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onDestroy() {
        if (web_webview!=null) {
            ViewParent parent = web_webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(web_webview);
            }
            web_webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            web_webview.getSettings().setJavaScriptEnabled(false);
            web_webview.clearHistory();
            web_webview.clearView();
            web_webview.removeAllViews();
            try {
                web_webview.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (web_webview.canGoBack() && getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
            web_webview.goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (impl!=null) {
                for (Method method : impl.getClass().getDeclaredMethods()) {
                    String name=method.getName();
                    if (name.startsWith("onActivityResult_") && name.split("_")[1].equals(""+requestCode)) {
                        try {
                            method.invoke(impl);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
