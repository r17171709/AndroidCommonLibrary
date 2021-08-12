package com.renyu.commonlibrary.commonutils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified T : ViewBinding> AppCompatActivity.binding(): T {
    val inflateMethod = T::class.java.getMethod("inflate", LayoutInflater::class.java)
    val binding = inflateMethod.invoke(null, layoutInflater)
    setContentView((binding as T).root)
    return binding
}

inline fun <reified T : ViewBinding> Fragment.binding() = FragmentBindingDelegate(T::class.java)

@MainThread
class FragmentBindingDelegate<T : ViewBinding>(private val clazz: Class<T>) :
    ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        // 已经绑定，直接返回
        binding?.let {
            return it
        }

        val lifecycle = thisRef.viewLifecycleOwner.lifecycle
        val bindMethod = clazz.getMethod("bind", View::class.java)
        val binding = bindMethod.invoke(null, thisRef.requireView()) as T
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            // 定义视图生命周期监听者
            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    owner.lifecycle.removeObserver(this)
                    // 为什么 onDestroy() 要采用 Handler#post(Message) 完成？ 因为 Fragment#viewLifecycleOwner 通知生命周期事件 ON_DESTROY 的时机在 Fragment#onDestryoView 之前。
                    Handler(Looper.getMainLooper()).post {
                        this@FragmentBindingDelegate.binding = null
                    }
                }
            })
            // 缓存绑定类对象
            this@FragmentBindingDelegate.binding = binding
        } else {
            // 如果视图生命周期为 DESTROYED，说明视图被销毁，此时不缓存绑定类对象（避免内存泄漏）
        }
        return binding
    }
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

/**
 * RecyclerView滚动到指定位置
 */
fun RecyclerView.smoothMoveToPosition(position: Int) {
    //目标项是否在最后一个可见项之后
    var mShouldScroll = false
    //记录目标项位置
    var mToPosition = 0

    // 第一个可见位置
    val firstItem = getChildLayoutPosition(getChildAt(0))
    // 最后一个可见位置
    val lastItem = getChildLayoutPosition(getChildAt(childCount - 1))
    if (position < firstItem) {
        // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
        smoothScrollToPosition(position)
    } else if (position <= lastItem) {
        // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
        val movePosition = position - firstItem
        if (movePosition in 0 until childCount) {
            val top = getChildAt(movePosition).top
            // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
            smoothScrollBy(0, top)
        }
    } else {
        // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
        // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
        smoothScrollToPosition(position)
        mToPosition = position
        mShouldScroll = true
    }
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                mShouldScroll = false
                smoothMoveToPosition(mToPosition)
            }
        }
    })
}