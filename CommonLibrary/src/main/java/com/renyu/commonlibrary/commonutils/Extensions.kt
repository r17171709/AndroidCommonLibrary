package com.renyu.commonlibrary.commonutils

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