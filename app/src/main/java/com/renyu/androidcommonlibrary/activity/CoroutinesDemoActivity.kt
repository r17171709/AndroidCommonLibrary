package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.widget.TextView
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

class CoroutinesDemoActivity : BaseActivity() {

    private var job: Job = Job()

    override fun initParams() {
    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        launch(UI, parent = job) {
            showNetworkDialog("正在加载数据")
            try {
                findViewById<TextView>(R.id.tv_main).text = getRemoteData()
            } catch (e: Exception) {

            } finally {
                dismissNetworkDialog()
            }
        }.invokeOnCompletion {
            // 被取消了
            println(it)
            if (it != null) {
                dismissNetworkDialog()
            }
        }
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private suspend fun getRemoteData() = withContext(CommonPool) {
        val responseBody = httpHelper.okHttpUtils.syncGet("http://www.mocky.io/v2/5943e4dc1200000f08fcb4d4").body()
        if (responseBody == null) {
            throw Exception("出现异常")
        }
        else {
            responseBody.string()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}