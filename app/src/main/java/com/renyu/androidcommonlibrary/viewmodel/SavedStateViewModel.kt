package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

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