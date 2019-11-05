package com.renyu.commonlibrary.web.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.renyu.commonlibrary.web.R;
import com.renyu.commonlibrary.web.impl.IX5WebApp;
import com.renyu.commonlibrary.web.params.InitParams;
import com.renyu.commonlibrary.web.util.PreloadWebView;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by renyu on 16/2/16.
 * 缺少样式不可以直接使用
 */
public abstract class X5WebActivity extends AppCompatActivity {
    public abstract TextView getTitleView();

    public abstract ImageButton getNavClose();

    public abstract ImageButton getNavBack();

    public abstract ViewGroup getRootView();

    public abstract void onPageFinished(String url);

    public WebView webView;

    // 是否需要展示Close按钮
    private int finishTimes = 0;

    IX5WebApp impl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
    }

    public void initViews() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            getTitleView().setText(getIntent().getStringExtra("title"));
        }
        getNavBack().setOnClickListener(v -> onBackPressed());

        webView = PreloadWebView.getInstance().getWebView(this);
        getRootView().addView(webView);
        PreloadWebView.getInstance().preload();

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    getTitleView().setText(getIntent().getStringExtra("title"));
                } else {
                    getTitleView().setText(title);
                }
            }

            @Override
            public Bitmap getDefaultVideoPoster() {
                // 使用webview的视频全屏播放功能，Android8.0以上的手机可以会遇到如下崩溃
                try {
                    return BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.ic_default_webview);
                } catch (Exception e) {
                    return super.getDefaultVideoPoster();
                }
            }
        });
        impl = getIntent().getParcelableExtra("IWebApp");
        if (impl != null) {
            impl.setContext(this);
            impl.setWebView(webView);
            webView.addJavascriptInterface(impl, getIntent().getStringExtra("IWebAppName"));
        }
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                X5WebActivity.this.onPageFinished(url);
                // 判断是否展示Close按钮
                if (getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
                    finishTimes++;
                    if (finishTimes > 1) {
                        getNavClose().setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        });
        // 设置cookies
        if (getIntent().getStringExtra("cookieUrl") != null) {
            HashMap<String, String> cookies = new HashMap<>();
            ArrayList<String> cookieValues = getIntent().getStringArrayListExtra("cookieValues");
            for (int i = 0; i < cookieValues.size() / 2; i++) {
                cookies.put(cookieValues.get(i * 2), cookieValues.get(i * 2 + 1));
            }
            // cookies同步方法要在WebView的setting设置完之后调用，否则无效。
            syncCookie(this, getIntent().getStringExtra("cookieUrl"), cookies);
        }
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                webView.stopLoading();
                webView.clearCache(true);
                webView.clearHistory();
                webView.removeAllViews();
                webView.destroy();
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(webView);
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack() && getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (impl != null) {
                for (Method method : impl.getClass().getDeclaredMethods()) {
                    String name = method.getName();
                    if (name.startsWith("onActivityResult_") && name.split("_")[1].equals("" + requestCode)) {
                        try {
                            method.invoke(impl);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加Cookie
     *
     * @param context
     * @param url
     * @param cookies
     */
    private void syncCookie(Context context, String url, HashMap<String, String> cookies) {
        // 如果API是21以下的话，需要在CookieManager前加
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        Iterator it = cookies.entrySet().iterator();
        // 注意使用for循环进行setCookie(String url, String value)调用。网上有博客表示使用分号手动拼接的value值会导致cookie不能完整设置或者无效
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String value = entry.getKey() + "=" + entry.getValue();
            cookieManager.setCookie(url, value);
        }
        // 如果API是21以下的话,在for循环结束后加
        CookieSyncManager.getInstance().sync();
    }
}
