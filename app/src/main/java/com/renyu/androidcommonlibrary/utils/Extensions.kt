package com.renyu.androidcommonlibrary.utils

import android.app.Activity
import androidx.fragment.app.Fragment
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