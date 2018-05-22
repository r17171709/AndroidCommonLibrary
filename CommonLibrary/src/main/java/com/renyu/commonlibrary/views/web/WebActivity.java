package com.renyu.commonlibrary.views.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.commonutils.sonic.SonicRuntimeImpl;
import com.renyu.commonlibrary.commonutils.sonic.SonicSessionClientImpl;
import com.renyu.commonlibrary.impl.WebAppImpl;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by renyu on 16/2/16.
 * 缺少样式不可以直接使用
 */
public class WebActivity extends BaseActivity {

    ImageButton ib_nav_left;
    public WebView web_webview;
    public TextView tv_nav_title;

    private SonicSession sonicSession;
    private SonicSessionClientImpl sonicSessionClient = null;

    WebAppImpl impl;

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

    @Override
    public void initParams() {
        ib_nav_left = (ImageButton) findViewById(R.id.ib_nav_left);
        ib_nav_left.setImageResource(R.mipmap.ic_arrow_black_left);
        ib_nav_left.setOnClickListener(v -> finish());
        tv_nav_title = (TextView) findViewById(R.id.tv_nav_title);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title"))) {
            tv_nav_title.setText(getIntent().getStringExtra("title"));
        }
        web_webview = (WebView) findViewById(R.id.web_webview);
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
        WebSettings settings=web_webview.getSettings();
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }
        });
        // 设置cookies
        HashMap<String, String> cookies = new HashMap<>();
        if (getIntent().getStringExtra("cookieUrl") != null) {
            ArrayList<String> cookieValues = getIntent().getStringArrayListExtra("cookieValues");
            for (int i = 0; i < cookieValues.size()/2; i++) {
                cookies.put(cookieValues.get(i*2), cookieValues.get(i*2+1));
            }
        }
        // cookies同步方法要在WebView的setting设置完之后调用，否则无效。
        syncCookie(this, getIntent().getStringExtra("cookieUrl"), cookies);
        if (sonicSessionClient != null) {
            sonicSessionClient.bindWebView(web_webview);
            sonicSessionClient.clientReady();
        } else {
            web_webview.loadUrl(getIntent().getStringExtra("url"));
        }
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
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
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

    private static class OfflinePkgSessionConnection extends SonicSessionConnection {

        public OfflinePkgSessionConnection(Context context, SonicSession session, Intent intent) {
            super(session, intent);
        }

        @Override
        protected int internalConnect() {
            return SonicConstants.ERROR_CODE_UNKNOWN;
        }

        @Override
        protected BufferedInputStream internalGetResponseStream() {
            return responseStream;
        }

        @Override
        public void disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public int getResponseCode() {
            return 200;
        }

        @Override
        public Map<String, List<String>> getResponseHeaderFields() {
            return new HashMap<>(0);
        }

        @Override
        public String getResponseHeaderField(String key) {
            return "";
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
