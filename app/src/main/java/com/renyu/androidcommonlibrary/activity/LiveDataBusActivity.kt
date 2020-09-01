package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.viewmodel.SharedViewModel
import com.renyu.commonlibrary.baseact.BaseActivity

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBusActivity : BaseActivity() {
    private val sharedViewModel by lazy {
        ViewModelProvider(
            (Utils.getApp()) as ExampleApp,
            ViewModelProvider.AndroidViewModelFactory.getInstance(Utils.getApp())
        ).get(SharedViewModel::class.java)
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    override fun loadData() {
        sharedViewModel.liveDataBus.setValue(true)
    }

    override fun initParams() {
        Handler().postDelayed({
            sharedViewModel.liveDataBus.observe(this,
                Observer<Boolean> { t ->
                    run {
                        if (t != null) {
                            ToastUtils.showShort("value:$t")
                        }
                    }
                })
            sharedViewModel.liveDataBus.setValue(false)
        }, 3000)
    }

    override fun initViews() = R.layout.activity_main
}