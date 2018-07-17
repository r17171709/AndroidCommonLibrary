package com.renyu.androidcommonlibrary.repository

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.impl.DataActionImpl
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import com.renyu.commonlibrary.network.BaseObserver
import com.renyu.commonlibrary.network.Retrofit2Utils
import io.reactivex.disposables.Disposable

/**
 * Created by Administrator on 2018/7/8.
 */
class Repos {
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

    fun getTokenResponse(input: TokenRequest, uiHandlers: MutableLiveData<DataActionImpl<TokenResponse>>) : MutableLiveData<TokenResponse> {
        val tokenResponse = MutableLiveData<TokenResponse>()
        Retrofit2Utils.getBaseRetrofit().create(RetrofitImpl::class.java).getAccessToken(
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
//                        uiHandlers.value = object : DataActionImpl<TokenResponse> {
//                            override fun execute(dataCallBackImpl: DataCallBackImpl<TokenResponse>) {
//                                dataCallBackImpl.onLoading()
//                            }
//                        }
                    }

                    override fun onNext(accessTokenResponse: AccessTokenResponse) {
                        val tempResponse = TokenResponse(ObservableField(accessTokenResponse.access_token), ObservableInt(accessTokenResponse.expires_in))
                        tokenResponse.value = tempResponse
//                        uiHandlers.value = object : DataActionImpl<TokenResponse> {
//                            override fun execute(dataCallBackImpl: DataCallBackImpl<TokenResponse>) {
//                                dataCallBackImpl.onNext(tempResponse)
//                            }
//                        }
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        tokenResponse.value = null
//                        uiHandlers.value = object : DataActionImpl<TokenResponse> {
//                            override fun execute(dataCallBackImpl: DataCallBackImpl<TokenResponse>) {
//                                dataCallBackImpl.onError(e)
//                            }
//                        }
                    }
                })
        return tokenResponse
    }
}