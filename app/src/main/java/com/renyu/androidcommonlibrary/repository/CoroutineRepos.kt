package com.renyu.androidcommonlibrary.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.bean.RentResponse
import com.renyu.commonlibrary.network.Retrofit2Utils
import com.renyu.commonlibrary.network.other.EmptyResponse
import com.renyu.commonlibrary.network.other.NetworkException
import com.renyu.commonlibrary.network.other.Resource
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CoroutineRepos {
    @JvmField
    @Inject
    var retrofitImpl: RetrofitImpl? = null

    companion object {
        @Volatile
        private var instance: CoroutineRepos? = null

        fun getCoroutineReposInstance(): CoroutineRepos {
            if (instance == null) {
                synchronized(CoroutineRepos::class) {
                    if (instance == null) {
                        instance = CoroutineRepos()
                    }
                }
            }
            return instance!!
        }
    }

    init {
        (Utils.getApp() as ExampleApp).appComponent.plusCoroutineRepos().inject(this)
    }

    fun getAccessToken2(
        context: CoroutineContext,
        input: AccessTokenRequest
    ): LiveData<Resource<AccessTokenResponse>> {
        return liveData<Resource<AccessTokenResponse>>(context = context) {
            httpRequestWithLiveData(retrofitImpl!!) {
                apiFun {
                    this.getAccessToken2()
                }
            }
        }
    }

    fun getAccessToken3(
        scope: CoroutineScope,
        input: AccessTokenRequest
    ): MutableLiveData<Resource<AccessTokenResponse>> {
        val tokenResponse = MutableLiveData<Resource<AccessTokenResponse>>()
        httpRequest<AccessTokenResponse>(scope) {
            apiFun {
                this.getAccessToken2()
            }

            callBack {
                tokenResponse.postValue(it)
            }
        }
        return tokenResponse
    }

    private fun <T> httpRequest(
        scope: CoroutineScope,
        build: CoroutineBuilder<T>.() -> Unit
    ): Job {
        val builder = CoroutineBuilder<T>()
        builder.build()
        builder.callBack!!.invoke(Resource.loading(null))
        return scope.launch(context = SupervisorJob() + Dispatchers.Main.immediate) {
            runCatching {
                withContext(Dispatchers.IO) {
                    builder.apiFun!!.invoke(retrofitImpl!!)
                }
            }.onSuccess { response ->
                if (response.result == Retrofit2Utils.sucessedCode) {
                    if (response.data is EmptyResponse) {
                        (response.data as EmptyResponse).setMessage(response.message)
                    }
                    builder.callBack!!.invoke(Resource.sucess(response.data))
                } else {
                    val exception = NetworkException()
                    exception.setMessage(response.message)
                    exception.result = response.result
                    builder.callBack!!.invoke(Resource.error(exception))
                }
            }.onFailure {
                if (it !is CancellationException) {
                    if (it is NetworkException) {
                        builder.callBack!!.invoke(Resource.error(it))
                    } else {
                        // 未知异常均转换为NetworkException
                        val exception = NetworkException()
                        exception.setMessage(it.message)
                        exception.result = -1
                        builder.callBack!!.invoke(Resource.error(exception))
                    }
                }
            }
        }
    }
}

class CoroutineBuilder<T> {
    var apiFun: (suspend RetrofitImpl.() -> RentResponse<T>)? = null
    var callBack: ((Resource<T>) -> Unit)? = null

    fun apiFun(block: suspend RetrofitImpl.() -> RentResponse<T>) {
        apiFun = block
    }

    fun callBack(block: ((Resource<T>) -> Unit)?) {
        callBack = block
    }
}

private suspend fun <T> LiveDataScope<Resource<T>>.httpRequestWithLiveData(
    retrofitImpl: RetrofitImpl,
    build: CoroutineBuilder<T>.() -> Unit
) {
    val builder = CoroutineBuilder<T>()
    builder.build()
    emit(Resource.loading(null))
    runCatching {
        withContext(Dispatchers.IO) {
            builder.apiFun!!.invoke(retrofitImpl)
        }
    }.onSuccess { response ->
        if (response.result == Retrofit2Utils.sucessedCode) {
            if (response.data is EmptyResponse) {
                (response.data as EmptyResponse).setMessage(response.message)
            }
            emit(Resource.sucess(response.data))
        } else {
            val exception = NetworkException()
            exception.setMessage(response.message)
            exception.result = response.result
            emit(Resource.error(exception))
        }
    }.onFailure {
        if (it !is CancellationException) {
            if (it is NetworkException) {
                emit(Resource.error(it))
            } else {
                // 未知异常均转换为NetworkException
                val exception = NetworkException()
                exception.setMessage(it.message)
                exception.result = -1
                emit(Resource.error(exception))
            }
        }
    }
}