package com.renyu.androidcommonlibrary.repository

import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.api.RetrofitImpl
import javax.inject.Inject

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

    suspend fun getAccessToken2() = retrofitImpl?.getAccessToken2()
}