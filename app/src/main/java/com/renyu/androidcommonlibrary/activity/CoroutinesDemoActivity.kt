package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.widget.TextView
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.network.OKHttpUtils
import kotlinx.coroutines.*
import javax.inject.Inject

class CoroutinesDemoActivity : BaseActivity() {

    @JvmField
    @Inject
    var okHttpUtils: OKHttpUtils? = null

    private var job: Job? = null

    override fun initParams() {
        (Utils.getApp() as ExampleApp).appComponent.plusAct().inject(this)
    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        job = GlobalScope.launch(Dispatchers.Main) {
            showNetworkDialog("正在加载数据")
            try {
                findViewById<TextView>(R.id.tv_main).text = getRemoteData()
            } catch (e: Exception) {

            } finally {
                dismissNetworkDialog()
            }
        }
        job!!.invokeOnCompletion {
            // 被取消了
            println(it)
            if (it != null) {
                dismissNetworkDialog()
            }
        }
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private suspend fun getRemoteData() = withContext(Dispatchers.Default) {
        val responseBody = okHttpUtils!!.syncGet("http://www.mocky.io/v2/5943e4dc1200000f08fcb4d4").body()
        if (responseBody == null) {
            throw Exception("出现异常")
        }
        else {
            responseBody.string()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}