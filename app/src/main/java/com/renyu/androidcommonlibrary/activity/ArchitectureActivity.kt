package com.renyu.androidcommonlibrary.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.impl.EventImpl
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModel
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModelFactory
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.commonutils.Utils
import com.renyu.commonlibrary.network.other.Status

/**
 * Created by Administrator on 2018/7/7.
 */
class ArchitectureActivity : BaseDataBindingActivity<ActivityArchitectureBinding>(), EventImpl {

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
                    if (it.status == Status.SUCESS) {
                        if (it.data != null) {
                            vm?.refreshUI(it.data!!)
                        }
                    }
                    else if (it.status == Status.ERROR) {

                    }
                    else if (it.status == Status.LOADING) {

                    }
                }
            })
        }
    }

    override fun click(view: View, request: TokenRequest) {
        vm?.sendRequest(request)
    }
}