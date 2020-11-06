package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SavedStateViewModel(stateHandle: SavedStateHandle) : ViewModel() {
    private val savedStateHandle = stateHandle
    val liveId = savedStateHandle.getLiveData<String>("LIVE")

    fun saveCurrentUser(id: String) {
        savedStateHandle.set("ID", id)
    }

    fun getCurrentUser(): String {
        return savedStateHandle.get<String>("ID") ?: ""
    }

    fun updateLIVE(value: String) {
        savedStateHandle.set("LIVE", value)
    }
}