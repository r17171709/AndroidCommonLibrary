package com.renyu.androidcommonlibrary.repository

import android.arch.lifecycle.MutableLiveData
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.commonlibrary.network.Retrofit2Utils
import com.renyu.commonlibrary.network.impl.IRetryCondition
import com.renyu.commonlibrary.network.other.NetworkException
import com.renyu.commonlibrary.network.other.Resource
import com.renyu.commonlibrary.network.other.RetryFunction
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Administrator on 2018/7/8.
 */
class Repos {
    @JvmField
    @Inject
    var retrofitImpl: RetrofitImpl? = null

    // 全部网络连接
    val disposoables: HashMap<String, Disposable> by lazy {
        HashMap<String, Disposable>()
    }

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
        (Utils.getApp() as ExampleApp).appComponent.plusRepos().inject(this)
    }

    fun getTokenResponse(input: AccessTokenRequest, cancelTag: String): MutableLiveData<Resource<AccessTokenResponse>> {
        val tokenResponse = MutableLiveData<Resource<AccessTokenResponse>>()
        val disposable = retrofitImpl!!.getAccessToken(
                input.city,
                input.timestamp,
                input.app_id,
                input.rand_str,
                input.signature,
                input.device_id,
                "v1.0",
                0,
                1,
                6000
        )
                .compose(Retrofit2Utils.background<AccessTokenResponse>())
                .retryWhen(
                        RetryFunction(3, 3,
                                object : IRetryCondition {
                                    override fun canRetry(throwable: Throwable?): Boolean {
                                        return throwable is NetworkException && throwable.result == 1
                                    }

                                    override fun doBeforeRetry() {
                                        Thread.sleep(2000)
                                    }
                                })
                )
                .compose(Retrofit2Utils.withSchedulers())
                .subscribe({
                    tokenResponse.value = Resource.sucess(it)
                }, { error ->
                    tokenResponse.value = Resource.error(error as NetworkException)
                }, {

                }, {
                    tokenResponse.value = Resource.loading(it)
                })
        disposoables[cancelTag] = disposable
        return tokenResponse
    }
}