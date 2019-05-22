package com.renyu.commonlibrary.commonutils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Created by Administrator on 2019/5/22.
 */
class LiveDataBus {
    val requests = HashMap<Class<*>, ArrayList<HashMap<LifecycleOwner, MutableLiveData<*>>>>()
    private val responses = ArrayList<MediatorLiveData<*>>()

    companion object {
        @Volatile
        private var bus: LiveDataBus? = null

        fun getInstance(): LiveDataBus = bus ?: synchronized(LiveDataBus::class.java) {
            bus ?: LiveDataBus().also {
                bus = it
            }
        }
    }

    inline fun <reified T> register(owner: LifecycleOwner, noinline func: (T) -> Unit) {
        val request = MutableLiveData<T>()
        val response = MediatorLiveData<T>()
        response.addSource(request) {
            response.value = it
        }
        response.observe(owner, Observer(func))
        val list = if (requests.containsKey(T::class.java)) requests[T::class.java] else ArrayList()
        val hashMap = HashMap<LifecycleOwner, MutableLiveData<*>>()
        hashMap[owner] = request
        list!!.add(hashMap)
        requests[T::class.java] = list

    }

    fun unRegister(owner: LifecycleOwner) {
        responses.forEach {
            it.removeObservers(owner)
        }
        for ((_, v) in requests) {
            val remove = ArrayList<HashMap<LifecycleOwner, MutableLiveData<*>>>()
            v.filter {
                it.containsKey(owner)
            }.forEach {
                remove.add(it)
            }
            remove.forEach {
                v.remove(it)
            }
        }
    }

    inline fun <reified T> post(value: T) {
        if (requests.containsKey(T::class.java)) {
            requests[T::class.java]!!.forEach {
                for ((k, v) in it) {
                    v.value = value
                }
            }
        }
    }
}