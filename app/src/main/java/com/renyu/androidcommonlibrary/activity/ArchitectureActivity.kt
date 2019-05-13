package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModelProviders
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.Demo
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.impl.EventImpl
import com.renyu.androidcommonlibrary.utils.BaseObserver2
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModel
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModelFactory
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.commonutils.Utils
import com.renyu.commonlibrary.network.other.Resource

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

    val demo: Demo by lazy {
        Demo(ObservableField(""), ObservableInt(0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.also {
            val timestamp = (System.currentTimeMillis() / 1000).toInt()
            val random = "abcdefghijklmn"
            val signature = "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
                    Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp
            it.tokenRequest =
                AccessTokenRequest(
                    "nj",
                    timestamp,
                    "46877648",
                    random,
                    Utils.getMD5(signature),
                    Utils.getUniquePsuedoID()
                )
            it.eventImpl = this
            it.tokenResponse = AccessTokenResponse("", 0)
            it.demo = demo

            vm = ViewModelProviders.of(this, ArchitectureViewModelFactory(it))
                .get(ArchitectureViewModel::class.java)
            vm?.tokenResponse?.observe(this, object : BaseObserver2<AccessTokenResponse>() {
                override fun onError(tResource: Resource<AccessTokenResponse>?) {

                }

                override fun onSucess(tResource: Resource<AccessTokenResponse>?) {
                    if (tResource?.data != null) {
                        vm?.refreshUI(tResource.data!!)
                        demo.access_token.set(tResource.data!!.access_token)
                        demo.expires_in.set(tResource.data!!.expires_in)
                    }
                }
            })
        }
    }

    override fun click(view: View, request: AccessTokenRequest) {
        vm?.sendRequest(request)
    }
}