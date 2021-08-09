package com.renyu.androidcommonlibrary.utils

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.renyu.commonlibrary.network.other.Resource
import com.renyu.commonlibrary.network.other.Status

class BaseObserver3<T>(listenerBuilder: BaseObserver3<T>.ListenerBuilder.() -> Unit) :
    Observer<Resource<T>> {
    private val listener = ListenerBuilder().also(listenerBuilder)

    override fun onChanged(tResource: Resource<T>?) {
        if (tResource != null) {
            when (tResource.status) {
                Status.ERROR -> {
                    if (tResource.exception!!.result == -1) {
                        ToastUtils.showShort("网络数据异常,请稍后再试")
                    } else {
                        ToastUtils.showShort(tResource.exception!!.message)
                    }
                    listener.onErrorAction!!.invoke(tResource)
                }
                Status.SUCESS -> {
                    listener.onSuccessAction!!.invoke(tResource)
                }
                Status.LOADING -> {
                    listener.onLoadingAction!!.invoke()
                }
            }
        }
    }

    inner class ListenerBuilder {
        var onSuccessAction: ((Resource<T>) -> Unit)? = null
        var onErrorAction: ((Resource<T>) -> Unit)? = null
        var onLoadingAction: (() -> Unit)? = null

        fun onSuccess(onSuccessAction: ((Resource<T>) -> Unit)) {
            this.onSuccessAction = onSuccessAction
        }

        fun onError(onErrorAction: ((Resource<T>) -> Unit)) {
            this.onErrorAction = onErrorAction
        }

        fun onLoading(onLoadingAction: () -> Unit) {
            this.onLoadingAction = onLoadingAction
        }
    }
}