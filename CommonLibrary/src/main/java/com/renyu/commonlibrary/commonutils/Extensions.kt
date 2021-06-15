package com.renyu.commonlibrary.commonutils

import android.app.Activity
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentBindingDelegate<VB : ViewBinding>(var clazz: Class<VB>) :
    ReadOnlyProperty<Fragment, VB> {
    private var _bind: VB? = null
    private var isInitialized = false
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (!isInitialized) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestoryView() {
                    _bind = null
                }
            })
        }
        isInitialized = true
        _bind = clazz.getMethod("bind", View::class.java).invoke(null, thisRef.requireView()) as VB
        return _bind!!
    }
}

inline fun <reified VB : ViewBinding> Fragment.binding() = FragmentBindingDelegate(VB::class.java)

inline fun <reified VB : ViewBinding> AppCompatActivity.binding() =
    (VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB).apply {
        setContentView(root)
    }


// 获取传递参数
fun <U, T> bindStringExtra(key: String) = DataStringDelegate<U, T>(key)

fun <U, T> bindStringArgument(key: String) = DataStringDelegate<U, T>(key)

fun <U, T> bindIntExtra(key: String) = DataStringDelegate<U, T>(key)

fun <U, T> bindIntArgument(key: String) = DataStringDelegate<U, T>(key)

fun <U, T> bindSerializableExtra(key: String) = DataSerializableDelegate<U, T>(key)

fun <U, T> bindSerializableArgument(key: String) = DataSerializableDelegate<U, T>(key)

fun <U, T : Parcelable?> bindParcelableExtra(key: String) =
    DataParcelableDelegate<U, T?>(key)

fun <U, T : Parcelable> bindParcelableArgument(key: String) =
    DataParcelableDelegate<U, T>(key)

class DataStringDelegate<U, T>(private val params: String) : ReadOnlyProperty<U, T?> {
    override fun getValue(thisRef: U, property: KProperty<*>): T? {
        return when (thisRef) {
            is Activity -> thisRef.intent.getStringExtra(params) as T?
            is Fragment -> thisRef.arguments?.getString(params) as T?
            else -> null
        }
    }
}

class DataIntDelegate<U, T>(private val params: String) : ReadOnlyProperty<U, T?> {
    override fun getValue(thisRef: U, property: KProperty<*>): T? {
        return when (thisRef) {
            is Activity -> thisRef.intent.getIntExtra(params, -1) as T?
            is Fragment -> thisRef.arguments?.getInt(params) as T?
            else -> null
        }
    }
}

class DataSerializableDelegate<U, T>(private val params: String) : ReadOnlyProperty<U, T?> {
    override fun getValue(thisRef: U, property: KProperty<*>): T? {
        return when (thisRef) {
            is Activity -> thisRef.intent.getSerializableExtra(params) as T?
            is Fragment -> thisRef.arguments?.getSerializable(params) as T?
            else -> null
        }
    }
}

class DataParcelableDelegate<U, T : Parcelable?>(private val params: String) :
    ReadOnlyProperty<U, T?> {
    override fun getValue(thisRef: U, property: KProperty<*>): T? {
        return when (thisRef) {
            is Activity -> thisRef.intent.getParcelableExtra<T>(params)
            is Fragment -> thisRef.arguments?.getParcelable<T>(params)
            else -> null
        }
    }
}