package com.renyu.androidcommonlibrary.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding


/**
 * Created by Administrator on 2018/7/8.
 */
class ArchitectureViewModelFactory(private val dataBinding: ActivityArchitectureBinding) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (ViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                return ArchitectureViewModel(dataBinding) as T
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return super.create<T>(modelClass)
    }
}