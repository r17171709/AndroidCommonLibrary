package com.renyu.commonlibrary.web.util;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.Utils;

import java.util.Stack;

/**
 * Created by Administrator on 2019/6/27.
 */
public class PreloadWebView {
    private static volatile PreloadWebView preloadWebView = null;

    private static final int CACHED_WEBVIEW_MAX_NUM = 2;
    private static final Stack<WebView> mCachedWebViewStack = new Stack<>();

    private PreloadWebView() {
    }

    public static PreloadWebView getInstance() {
        if (preloadWebView == null) {
            synchronized (PreloadWebView.class) {
                if (preloadWebView == null) {
                    preloadWebView = new PreloadWebView();
                }
            }
        }
        return preloadWebView;
    }

    private WebView createWebView() {
        WebView webView = new WebView(new MutableContextWrapper(Utils.getApp()));
        webView.setSaveEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
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
        settings.setBlockNetworkImage(false);
        settings.setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        return webView;
    }

    public void preload() {
        Looper.myQueue().addIdleHandler(() -> {
            if (mCachedWebViewStack.size() < CACHED_WEBVIEW_MAX_NUM) {
                mCachedWebViewStack.push(createWebView());
            }
            return false;
        });
    }

    public WebView getWebView(Context context) {
        WebView web;
        if (mCachedWebViewStack == null || mCachedWebViewStack.size() == 0) {
            web = createWebView();
        } else {
            web = mCachedWebViewStack.pop();
        }
        // webView不为空，则开始使用预创建的WebView,并且替换Context
        MutableContextWrapper contextWrapper = (MutableContextWrapper) web.getContext();
        contextWrapper.setBaseContext(context);
        return web;
    }
}
