package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.ViewModel
import com.renyu.commonlibrary.commonutils.LiveDataBus

/**
 * Created by Administrator on 2020/9/1.
 */
public class SharedViewModel : ViewModel() {
    val liveDataBus by lazy {
        LiveDataBus<Boolean>()
    }
}