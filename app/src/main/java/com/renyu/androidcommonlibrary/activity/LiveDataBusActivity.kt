package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.commonutils.LiveDataBus

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBusActivity : BaseActivity() {
    override fun initParams() {
        LiveDataBus.with<String>("login").observe(this,
            { t -> Log.d("TAGTAGTAG", t) })

        LiveDataBus.with<String>("signIn").postStickData("signInHello")
    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        Handler(Looper.myLooper()!!).postDelayed({
            LiveDataBus.with<String>("login").postData("LoginHello")
            LiveDataBus.with<String>("signIn").observeStick(this,
                { t -> Log.d("TAGTAGTAG", t) })
        }, 3000)
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}