package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Handler
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.utils.ExampleUtils
import com.renyu.androidcommonlibrary.viewmodel.SharedViewModel
import com.renyu.commonlibrary.baseact.BaseActivity

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBusActivity : BaseActivity() {
    // 两者所使用的ViewModelOwner是不相同的，所以他们的ViewModelStore也是不同的
    private val sharedViewModel by lazy {
        ExampleUtils.getAppViewModel(SharedViewModel::class.java)
    }

    private val sharedViewModel2 by lazy {
        ExampleUtils.getActivityViewModel(this, SharedViewModel::class.java)
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