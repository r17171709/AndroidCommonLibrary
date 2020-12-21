package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.Demo
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.impl.EventImpl
import com.renyu.androidcommonlibrary.utils.BaseObserver2
import com.renyu.androidcommonlibrary.utils.BaseObserver3
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModel
import com.renyu.androidcommonlibrary.viewmodel.ArchitectureViewModelFactory
import com.renyu.androidcommonlibrary.viewmodel.SavedStateViewModel
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.commonutils.Utils
import com.renyu.commonlibrary.network.other.Resource
import com.renyu.commonlibrary.network.other.ResourceCoroutine

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
    private val dataVM by lazy {
        ViewModelProvider(
            this,
            SavedStateViewModelFactory(application, this@ArchitectureActivity)
        ).get(
            SavedStateViewModel::class.java
        )
    }

    val demo: Demo by lazy {
        Demo(ObservableField(""), ObservableInt(0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.also {
            val timestamp = (System.currentTimeMillis() / 1000).toInt()
            val random = "abcdefghijklmn"
            val signature =
                "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
                        Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp
            it.tokenRequest =
                AccessTokenRequest(
                    "nj",
                    timestamp,
                    "46877648",
                    random,
                    EncryptUtils.encryptMD5ToString(signature),
                    Utils.getUniquePsuedoID()
                )
            it.eventImpl = this
            it.tokenResponse = AccessTokenResponse("", 0)
            it.demo = demo

            vm = getSelfViewModel<ArchitectureViewModel> {
                tokenResponse?.observe(
                    this@ArchitectureActivity,
                    object : BaseObserver2<AccessTokenResponse>(this@ArchitectureActivity) {
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

            dataVM.liveId.observe(this,
                { t -> ToastUtils.showShort(t) })

        }
    }

    override fun click(view: View, request: AccessTokenRequest) {
        vm?.sendRequest(request)
    }

    override fun clickCourtine(view: View, request: AccessTokenRequest) {
        vm?.getAccessToken2()
            ?.observe(this, object : BaseObserver3<AccessTokenResponse>(this) {
                override fun onError(tResource: ResourceCoroutine<AccessTokenResponse>?) {

                }

                override fun onSucess(tResource: ResourceCoroutine<AccessTokenResponse>?) {
                    if (tResource?.data != null) {
                        vm?.refreshUI(tResource.data!!)
                        demo.access_token.set(tResource.data!!.access_token)
                        demo.expires_in.set(tResource.data!!.expires_in)
                    }
                }
            })
    }

    private inline fun <reified T : ViewModel> ViewModelStoreOwner.getSelfViewModel(configLiveData: T.() -> Unit): T {
        return getViewModel(this, ArchitectureViewModelFactory(viewDataBinding), configLiveData)
    }

    private inline fun <reified T : ViewModel> getViewModel(
        owner: ViewModelStoreOwner,
        factory: ViewModelProvider.NewInstanceFactory,
        configLiveData: T.() -> Unit
    ): T {
        return ViewModelProvider(owner, factory).get(T::class.java).apply {
            configLiveData()
        }
    }

    override fun clickSaveData(view: View) {
        // 当与key相对应的value改变时，MutableLiveData也会更新
        dataVM.updateLIVE(dataVM.getCurrentUser())
        dataVM.saveCurrentUser("ABC")
    }
}