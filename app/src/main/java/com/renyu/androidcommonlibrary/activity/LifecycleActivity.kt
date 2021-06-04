package com.renyu.androidcommonlibrary.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.databinding.ActivityLifecycleBinding
import com.renyu.commonlibrary.commonutils.binding

class LifecycleActivity : AppCompatActivity() {
    private var lifecycleRegistry: LifecycleRegistry? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(ActivityLifecycleBinding.inflate(layoutInflater).root)

        val binding = binding<ActivityLifecycleBinding>()
        binding.btnLifecycle.setOnClickListener {
            ToastUtils.showShort("LifeCycleActivity")
        }

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry?.currentState = Lifecycle.State.CREATED

        // 自定义LifecycleOwner
//        lifecycleRegistry?.addObserver(MyObserver())
        lifecycleRegistry?.addObserver(MyObserver2())

//        lifecycle.addObserver(MyObserver())

        lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry?.currentState = Lifecycle.State.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry?.currentState = Lifecycle.State.RESUMED
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry?.currentState = Lifecycle.State.DESTROYED
    }

    class MyObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreated(owner: LifecycleOwner) {

        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_START)
        fun connect() {
            Log.d("TAGTAGTAG", "connect")
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
        fun connect2() {
            Log.d("TAGTAGTAG", "connect2")
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
        fun disConnect() {
            Log.d("TAGTAGTAG", "disConnect")
        }

        @OnLifecycleEvent(value = Lifecycle.Event.ON_DESTROY)
        fun disConnect2() {
            Log.d("TAGTAGTAG", "disConnect2")
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {

        }
    }

    class MyObserver2 : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
        }
    }
}