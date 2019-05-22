package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Handler
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.commonutils.LiveDataBus

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBusActivity : BaseActivity() {
    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    override fun loadData() {
        Handler().postDelayed({
            LiveDataBus.getInstance().post("123")
        }, 2000)

        Handler().postDelayed({
            LiveDataBus.getInstance().post("123")
        }, 6000)
    }

    override fun initParams() {
        LiveDataBus.getInstance().register<String>(this) {
            ToastUtils.showShort(it)
        }
    }

    override fun initViews() = R.layout.activity_main

    override fun onDestroy() {
        super.onDestroy()
        LiveDataBus.getInstance().unRegister(this)
    }
}