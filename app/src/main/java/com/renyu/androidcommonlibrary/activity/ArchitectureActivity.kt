package com.renyu.androidcommonlibrary.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.impl.DataCallBackImpl
import com.renyu.androidcommonlibrary.impl.EventImpl
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModel
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModelFactory
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.commonutils.Utils

/**
 * Created by Administrator on 2018/7/7.
 * 用了两种方式来实现请求数据刷新
 */
class ArchitectureActivity : BaseDataBindingActivity<ActivityArchitectureBinding>(), EventImpl, DataCallBackImpl<TokenResponse> {

    override fun initParams() {

    }

    override fun initViews() = R.layout.activity_architecture

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private var vm: ArchitectureViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.also {
            val timestamp = (System.currentTimeMillis() / 1000).toInt()
            val random = "abcdefghijklmn"
            val signature = "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
                    Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp
            it.tokenRequest = TokenRequest("nj", timestamp, "46877648", random, Utils.getMD5(signature), Utils.getUniquePsuedoID())
            it.eventImpl = this
            it.tokenResponse = TokenResponse(ObservableField(""), ObservableInt(0))

            vm = ViewModelProviders.of(this, ArchitectureViewModelFactory(it.tokenResponse!!)).get(ArchitectureViewModel::class.java)
            vm?.tokenResponse?.observe(this, Observer {
                if (it != null) {
                    vm?.refreshUI(it)
                }
            })
            vm?.uiHandlers?.observe(this, Observer {
                it?.execute(this@ArchitectureActivity)
            })
        }
    }

    override fun click(view: View, request: TokenRequest) {
        vm?.sendRequest(request)
    }

    override fun onLoading() {

    }

    override fun onNext(response: TokenResponse) {
        vm?.refreshUI(response)
    }

    override fun onError(e: Throwable) {
        Toast.makeText(this, "出现错误", Toast.LENGTH_LONG).show()
    }
}