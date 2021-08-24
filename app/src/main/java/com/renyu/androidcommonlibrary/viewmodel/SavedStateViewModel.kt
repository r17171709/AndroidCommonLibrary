package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * 1、颗粒度不一样。onSaveInstanceState()是保存到Bundle中，只能保存Bundle能接受的数据类型，比如一些基本类型的数据。而onRetainNonConfigurationInstance()可以保存任何类型的数据，数据类型是Object。
 * 2、onSaveInstanceState()数据最终存储到ActivityManagerService的ActivityRecord中了，也就是存到系统进程中去了。而onRetainNonConfigurationInstance() 数据是存储到ActivityClientRecord中，也就是存到应用本身的进程中了。
 * 3、onSaveInstanceState存到系统进程中，所以App被杀之后还是能恢复的。而onRetainNonConfigurationInstance存到本身进程中，App被杀是没法恢复的。
 */
class SavedStateViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    fun saveCurrentUser(id: String) {
        stateHandle.set("ID", id)
    }

    fun getCurrentUserByLiveData(): MutableLiveData<String> {
        return stateHandle.getLiveData<String>("ID")
    }

    fun getCurrentUser(): String {
        return stateHandle.get<String>("ID") ?: ""
    }
}