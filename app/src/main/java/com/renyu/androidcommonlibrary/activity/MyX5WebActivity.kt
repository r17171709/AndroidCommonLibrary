package com.renyu.androidcommonlibrary.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.impl.X5WebAppInterface
import com.renyu.commonlibrary.commonutils.bindParcelableExtra
import com.renyu.commonlibrary.commonutils.bindStringExtra
import com.renyu.commonlibrary.web.activity.X5WebActivity

class MyX5WebActivity : X5WebActivity() {
    private val impl: X5WebAppInterface? by bindParcelableExtra<X5WebActivity, X5WebAppInterface>("IWebApp")
    private val iWebAppName: String? by bindStringExtra<X5WebActivity, String>("IWebAppName")

    // 文件上传使用
    private var uploadFilePathCallback: ValueCallback<Array<Uri>>? = null

    override fun getTitleView() = findViewById<TextView>(R.id.tv_nav_title)

    override fun getNavClose() = findViewById<ImageButton>(R.id.ib_nav_close)

    override fun getNavBack() = findViewById<ImageButton>(R.id.ib_nav_left)

    override fun getRootView() = findViewById<FrameLayout>(R.id.layout_x5webview)

    override fun getVideoView() = findViewById<FrameLayout>(R.id.layout_x5webview_video)

    override fun onPageFinished(url: String) {}

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        // 判断是否需要拦截
//        if (url.contains("weixin")) {
//            return true;
//        }
        return false
    }

    override fun onShowFileChooser(
        webView: WebView,
        filePathCallback: ValueCallback<Array<Uri>>,
        fileChooserParams: WebChromeClient.FileChooserParams
    ): Boolean {
        uploadFilePathCallback = filePathCallback
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 10)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5web)
        navClose.setImageResource(R.mipmap.ic_web_close)
        navBack.setImageResource(R.mipmap.ic_arrow_black_left)
        initViews()
        if (impl != null) {
            impl!!.setContext(this)
            impl!!.setWebView(webView)
            webView.addJavascriptInterface(impl!!, iWebAppName!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (null == uploadFilePathCallback) {
                return
            }
            uploadFilePathCallback!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
            uploadFilePathCallback = null
        } else if (resultCode == RESULT_CANCELED) {
            // 没有选择图片要重置
            if (uploadFilePathCallback != null) {
                uploadFilePathCallback!!.onReceiveValue(null)
                uploadFilePathCallback = null
            }
        }
    }
}
