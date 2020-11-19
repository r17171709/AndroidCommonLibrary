package com.renyu.androidcommonlibrary.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveDataScope
import com.renyu.commonlibrary.network.Retrofit2Utils
import com.renyu.commonlibrary.network.impl.IResponse
import com.renyu.commonlibrary.network.other.EmptyResponse
import com.renyu.commonlibrary.network.other.NetworkException
import com.renyu.commonlibrary.network.other.ResourceCoroutine
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class BindLoader<U, T>(val key: String) {
    operator fun provideDelegate(thisRef: U, prop: KProperty<*>): ReadOnlyProperty<U, T> {
        // 创建委托
        return IntentDelegate(key)
    }
}

private class IntentDelegate<U, T>(val key: String) : ReadOnlyProperty<U, T> {
    override fun getValue(thisRef: U, property: KProperty<*>): T {
        return when (thisRef) {
            is Fragment -> thisRef.arguments?.get(key) as T
            is Activity -> thisRef.intent.extras?.get(key) as T
            else -> throw Exception("Error Delegate")
        }
    }
}

fun <U, T> Activity.bindExtra(key: String) = BindLoader<U, T>(key)
fun <U, T> Fragment.bindArgument(key: String) = BindLoader<U, T>(key)

suspend inline fun <T> LiveDataScope<ResourceCoroutine<T>>.commonRequest(block: () -> IResponse<T>?) {
    emit(ResourceCoroutine.loading())
    try {
        val t = block.invoke()
        if (t != null && t.result == Retrofit2Utils.sucessedCode) {
            if (t.data is EmptyResponse) {
                (t.data as EmptyResponse).setMessage(t.message)
            }
            emit(ResourceCoroutine.sucess(t.data))
        } else if (t != null) {
            val exception = NetworkException()
            exception.setMessage(t.message)
            exception.result = t.result
            emit(ResourceCoroutine.error(exception))
        } else {
            val exception = NetworkException()
            exception.result = -1
            exception.setMessage("未知错误")
            emit(ResourceCoroutine.error(exception))
        }
    } catch (e: Exception) {
        if (e is NetworkException) {
            emit(ResourceCoroutine.error(e))
        } else {
            val exception = NetworkException()
            exception.result = -1
            exception.setMessage("未知错误")
            emit(ResourceCoroutine.error(exception))
        }
    }
}