package com.renyu.androidcommonlibrary.activity

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.commonutils.livedatabus.LiveDataBusCore

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBusActivity : BaseActivity() {
    private val busLiveData by lazy { LiveDataBusCore.instance.getChannel<String>("event") }

    override fun initParams() {

    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        Handler(Looper.myLooper()!!).postDelayed({
            busLiveData.setValue("1")
        }, 2000)

        Handler(Looper.myLooper()!!).postDelayed({
            busLiveData.observe(this,
                { t -> ToastUtils.showShort(t) })
        }, 4000)

        Handler(Looper.myLooper()!!).postDelayed({
            busLiveData.setValue("2")
        }, 6000)

        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this, LiveDataBus2Activity::class.java))
        }, 8000)
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}