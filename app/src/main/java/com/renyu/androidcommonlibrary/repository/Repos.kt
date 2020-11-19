package com.renyu.androidcommonlibrary.repository

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.commonlibrary.network.Retrofit2Utils
import com.renyu.commonlibrary.network.other.NetworkException
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

    // 全部网络连接
    private val disposoables: HashMap<String, Disposable> by lazy {
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

    fun getTokenResponse(
        input: AccessTokenRequest,
        cancelTag: String
    ): MutableLiveData<Resource<AccessTokenResponse>> {
        val tokenResponse = MutableLiveData<Resource<AccessTokenResponse>>()
        val disposable = retrofitImpl!!.getAccessToken()
            .compose(Retrofit2Utils.background<AccessTokenResponse>())
//            .retryWhen(
//                RetryFunction(3, 3,
//                    object : IRetryCondition {
//                        override fun canRetry(throwable: Throwable?): Boolean {
//                            return throwable is NetworkException && throwable.result == 1
//                        }
//
//                        override fun doBeforeRetry() {
//                            Thread.sleep(2000)
//                        }
//                    })
//            )
            .compose(Retrofit2Utils.withSchedulers())
            .subscribe({
                tokenResponse.postValue(Resource.sucess(it))
            }, { error ->
                fixNetworkError(tokenResponse, error)
            }, {

            }, {
                tokenResponse.postValue(Resource.loading(it))
            })
        disposoables[cancelTag] = disposable
        return tokenResponse
    }

    private fun <T : Any> fixNetworkError(response: MutableLiveData<Resource<T>>, t: Throwable?) {
        if (t != null && t is NetworkException) {
            response.postValue(Resource.error(t))
        } else {
            val exception = NetworkException()
            exception.result = -1
            exception.setMessage("未知错误")
            response.postValue(Resource.error(exception))
        }
    }
}