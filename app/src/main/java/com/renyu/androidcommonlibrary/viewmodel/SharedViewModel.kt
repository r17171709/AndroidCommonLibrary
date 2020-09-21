package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.ViewModel
import com.renyu.commonlibrary.commonutils.UnPeekLiveData

/**
 * Created by Administrator on 2020/9/1.
 */
class SharedViewModel : ViewModel() {
    val liveDataBus by lazy {
        UnPeekLiveData<Boolean>()
    }
}