package androidx.lifecycle

import android.os.Handler
import android.os.Looper
import com.renyu.commonlibrary.commonutils.livedatabus.BaseLiveDataBusObserverWrapper
import com.renyu.commonlibrary.commonutils.livedatabus.LiveDataBusAlwaysActiveObserver
import com.renyu.commonlibrary.commonutils.livedatabus.LiveDataBusLifecycleObserver

class BusLiveData<T> : MutableLiveData<T>() {
    private val mObserverMap: MutableMap<Observer<in T>, BaseLiveDataBusObserverWrapper<T>> by lazy { mutableMapOf() }

    private val mHandler = Handler(Looper.getMainLooper())

    override fun postValue(value: T) {
        mHandler.post {
            setValue(value)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val exist = mObserverMap.getOrPut(observer, {
            LiveDataBusLifecycleObserver(owner, this, observer).apply {
                mObserverMap[observer] = this
                owner.lifecycle.addObserver(this)
            }
        })
        super.observe(owner, exist)
    }

    override fun observeForever(observer: Observer<in T>) {
        val exist = mObserverMap.getOrPut(observer, {
            LiveDataBusAlwaysActiveObserver(this, observer).apply {
                mObserverMap[observer] = this
            }
        })
        super.observeForever(exist)
    }

    override fun removeObserver(observer: Observer<in T>) {
        mObserverMap.remove(observer)
        super.removeObserver(observer)
    }

    override fun removeObservers(owner: LifecycleOwner) {
        mObserverMap.clear()
        super.removeObservers(owner)
    }

    public override fun getVersion(): Int {
        return super.getVersion()
    }
}