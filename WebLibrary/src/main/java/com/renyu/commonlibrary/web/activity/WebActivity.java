package com.renyu.commonlibrary.web.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.renyu.commonlibrary.web.impl.IWebApp;
import com.renyu.commonlibrary.web.params.InitParams;
import com.renyu.commonlibrary.web.sonic.SonicRuntimeImpl;
import com.renyu.commonlibrary.web.sonic.SonicSessionClientImpl;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

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
public abstract class WebActivity extends AppCompatActivity {

    public abstract WebView getWebView();
    public abstract TextView getTitleView();

    private SonicSession sonicSession;
    private SonicSessionClientImpl sonicSessionClient = null;

    IWebApp impl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
        sonicSessionClient=new SonicSessionClientImpl();
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);
        sonicSession = SonicEngine.getInstance().createSession(getIntent().getStringExtra("url"), sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient);
        } else {
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    public void initViews() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            getTitleView().setText(getIntent().getStringExtra("title"));
        }
        getWebView().setSaveEnabled(true);
        getWebView().setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        getWebView().setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
                    getTitleView().setText(getIntent().getStringExtra("title"));
                }
                else {
                    getTitleView().setText(title);
                }
            }
        });
        WebSettings settings=getWebView().getSettings();
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
        impl=getIntent().getParcelableExtra("IWebApp");
        if (impl!=null) {
            impl.setContext(this);
            impl.setWebView(getWebView());
            getWebView().addJavascriptInterface(impl, getIntent().getStringExtra("IWebAppName"));
        }
        getWebView().removeJavascriptInterface("searchBoxJavaBridge_");
        getWebView().setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        // 设置cookies
        if (getIntent().getStringExtra("cookieUrl") != null) {
            HashMap<String, String> cookies = new HashMap<>();
            ArrayList<String> cookieValues = getIntent().getStringArrayListExtra("cookieValues");
            for (int i = 0; i < cookieValues.size()/2; i++) {
                cookies.put(cookieValues.get(i*2), cookieValues.get(i*2+1));
            }
            // cookies同步方法要在WebView的setting设置完之后调用，否则无效。
            syncCookie(this, getIntent().getStringExtra("cookieUrl"), cookies);
        }
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(getWebView());
            sonicSessionClient.clientReady();
        } else {
            getWebView().loadUrl(getIntent().getStringExtra("url"));
        }
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
        if (getWebView()!=null) {
            getWebView().stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            getWebView().getSettings().setJavaScriptEnabled(false);
            getWebView().clearHistory();
            getWebView().clearView();
            getWebView().removeAllViews();
            try {
                getWebView().destroy();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            ViewParent parent = getWebView().getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(getWebView());
            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getWebView().canGoBack() && getIntent().getExtras().getBoolean(InitParams.NEED_GOBACK, false)) {
            getWebView().goBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK) {
            if (impl!=null) {
                for (Method method : impl.getClass().getDeclaredMethods()) {
                    String name=method.getName();
                    if (name.startsWith("onActivityResult_") && name.split("_")[1].equals(""+requestCode)) {
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
