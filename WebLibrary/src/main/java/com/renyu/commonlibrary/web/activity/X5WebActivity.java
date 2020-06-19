package com.renyu.commonlibrary.web.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.renyu.commonlibrary.web.R;
import com.renyu.commonlibrary.web.params.InitParams;
import com.renyu.commonlibrary.web.util.HtmlFormat;
import com.renyu.commonlibrary.web.util.PreloadWebView;

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

    public abstract boolean shouldOverrideUrlLoading(WebView view, String url);

    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        super.onCreate(savedInstanceState);
    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    public void initViews() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            getTitleView().setText(getIntent().getStringExtra("title"));
        }
        getNavBack().setOnClickListener(v -> onBackPressed());

        webView = PreloadWebView.getInstance().getWebView(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
        } else if (!TextUtils.isEmpty(getIntent().getStringExtra("htmlCode"))) {
            webView.getSettings().setUseWideViewPort(false);
            webView.getSettings().setLoadWithOverviewMode(false);
        }
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
                if (super.getDefaultVideoPoster() == null) {
                    return BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_webview);
                } else {
                    return super.getDefaultVideoPoster();
                }
            }
        });
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                X5WebActivity.this.onPageFinished(url);
                // 判断是否展示Close按钮
                if (getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
                    if (webView.canGoBack()) {
                        getNavClose().setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (X5WebActivity.this.shouldOverrideUrlLoading(view, url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
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
        if (!TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
            webView.loadUrl(getIntent().getStringExtra("url"));
        } else if (!TextUtils.isEmpty(getIntent().getStringExtra("htmlCode"))) {
            webView.loadDataWithBaseURL(null, HtmlFormat.getNewContent(getIntent().getStringExtra("htmlCode")), "text/html", "UTF-8", null);
        }
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
