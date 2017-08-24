package com.renyu.commonlibrary.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.commonutils.sonic.SonicRuntimeImpl;
import com.renyu.commonlibrary.commonutils.sonic.SonicSessionClientImpl;
import com.renyu.commonlibrary.impl.WebAppImpl;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.sonic.sdk.SonicCacheInterceptor;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicConstants;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;
import com.tencent.sonic.sdk.SonicSessionConnection;
import com.tencent.sonic.sdk.SonicSessionConnectionInterceptor;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renyu on 16/2/16.
 */
public class WebActivity extends BaseActivity {

    WebView web_webview;
    TextView tv_nav_title;

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
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    public void initParams() {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
        sonicSessionClient=new SonicSessionClientImpl();
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
            @Override
            public String getCacheData(SonicSession session) {
                return null;
            }
        });
        sessionConfigBuilder.setConnectionIntercepter(new SonicSessionConnectionInterceptor() {
            @Override
            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
                return new OfflinePkgSessionConnection(WebActivity.this, session, intent);
            }
        });
        sonicSession = SonicEngine.getInstance().createSession(getIntent().getStringExtra("url"), sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(sonicSessionClient);
        } else {
            finish();
        }

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
            }
        });
        web_webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings settings=web_webview.getSettings();
        settings.setDomStorageEnabled(true);
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
        impl.setContext(this);
        impl.setWebView(web_webview);
        web_webview.addJavascriptInterface(impl, getIntent().getStringExtra("WebAppImplName"));
        web_webview.removeJavascriptInterface("searchBoxJavaBridge_");
        web_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WebActivity", url);
            }
        });
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setNeedInitialFocus(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setBuiltInZoomControls(false);
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
        super.onDestroy();
        web_webview.removeAllViews();
        web_webview.destroy();
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
}
