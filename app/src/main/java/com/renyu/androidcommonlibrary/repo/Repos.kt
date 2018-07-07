package com.renyu.androidcommonlibrary.repo

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.impl.RetrofitImpl
import com.renyu.commonlibrary.network.BaseObserver
import com.renyu.commonlibrary.network.Retrofit2Utils

/**
 * Created by Administrator on 2018/7/8.
 */
class Repos {
    companion object {
        @Volatile
        var instance: Repos? = null

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

    fun getTokenResponse(input: TokenRequest) : MutableLiveData<TokenResponse> {
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
                .subscribe(object : BaseObserver<AccessTokenResponse>(true) {
                    override fun onNext(accessTokenResponse: AccessTokenResponse) {
                        val tempResponse = TokenResponse(ObservableField(accessTokenResponse.access_token), ObservableInt(accessTokenResponse.expires_in))
                        tokenResponse.postValue(tempResponse)
                    }
                })
        return tokenResponse
    }
}