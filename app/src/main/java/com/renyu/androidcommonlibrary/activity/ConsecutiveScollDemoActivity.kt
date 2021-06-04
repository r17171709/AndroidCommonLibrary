package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.ScrollRVAdapter
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activity_consecutivescolldemo.*
import java.util.*

class ConsecutiveScollDemoActivity : BaseActivity() {
    override fun initParams() {

    }

    override fun initViews() = R.layout.activity_consecutivescolldemo

    override fun loadData() {
        web_consecutive!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                scrollerLayout.checkLayoutChange()
            }
        }
        web_consecutive!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }
        web_consecutive!!.loadUrl("https://www.baidu.com/")

        rv_consecutive1.setHasFixedSize(true)
        rv_consecutive1.layoutManager = LinearLayoutManager(this)
        rv_consecutive1.isNestedScrollingEnabled = false
        val linearLayoutBeans1 = ArrayList<Any>()
        linearLayoutBeans1.addAll(getBeans(15))
        val adapter1 = ScrollRVAdapter(this, linearLayoutBeans1)
        rv_consecutive1.adapter = adapter1

        rv_consecutive2.setHasFixedSize(true)
        rv_consecutive2.layoutManager = LinearLayoutManager(this)
        rv_consecutive2.isNestedScrollingEnabled = false
        val linearLayoutBeans2 = ArrayList<Any>()
        linearLayoutBeans2.addAll(getBeans(15))
        val adapter2 = ScrollRVAdapter(this, linearLayoutBeans2)
        rv_consecutive2.adapter = adapter2
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private fun getBeans(num_: Int): ArrayList<Any> {
        var num = num_
        val linearLayoutBeans = ArrayList<Any>()
        if (num == -1) {
            val random = Random()
            num = 10 + random.nextInt(20)
        }
        for (i in 0 until num) {
            val random = Random()
            linearLayoutBeans.add("" + i + "-" + random.nextInt(20))
        }
        return linearLayoutBeans
    }
}