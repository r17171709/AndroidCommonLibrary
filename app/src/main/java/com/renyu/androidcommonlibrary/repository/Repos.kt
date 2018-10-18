package com.renyu.androidcommonlibrary.repository

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.di.module.ReposModule
import com.renyu.commonlibrary.commonutils.BaseObserver
import com.renyu.commonlibrary.network.Retrofit2Utils
import com.renyu.commonlibrary.network.other.Resource
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Administrator on 2018/7/8.
 */
class Repos {

    @JvmField
    @Inject
    var retrofitImpl: RetrofitImpl? = null

    companion object {
        @Volatile
        private var instance: Repos? = null

        fun getReposInstance(): Repos {
            if (instance == null) {
                synchronized(Repos::class) {
                    if (instance == null) {
                        instance = Repos()
                    }
                }
            }
            return instance!!
        }
    }

    init {
        (Utils.getApp() as ExampleApp).appComponent.plus(ReposModule()).inject(this)
    }

    fun getTokenResponse(input: TokenRequest) : MutableLiveData<Resource<TokenResponse>> {
        val tokenResponse = MutableLiveData<Resource<TokenResponse>>()
        retrofitImpl!!.getAccessToken(
                input.city,
                input.timestamp,
                input.app_id,
                input.rand_str,
                input.signature,
                input.device_id,
                "v1.0",
                0,
                1,
                6000)
                .compose(Retrofit2Utils.background<AccessTokenResponse>())
                .subscribe(object : BaseObserver<AccessTokenResponse>(false) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        tokenResponse.value = Resource.loading()
                    }

                    override fun onNext(accessTokenResponse: AccessTokenResponse) {
                        val tempResponse = TokenResponse(ObservableField(accessTokenResponse.access_token), ObservableInt(accessTokenResponse.expires_in))
                        tokenResponse.value = Resource.sucess(tempResponse)
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        tokenResponse.value = Resource.error(e.message)
                    }
                })
        return tokenResponse
    }
}