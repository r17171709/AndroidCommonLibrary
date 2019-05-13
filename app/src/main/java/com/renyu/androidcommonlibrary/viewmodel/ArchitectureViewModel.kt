package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.repository.Repos
import com.renyu.commonlibrary.network.other.Resource

/**
 * Created by Administrator on 2018/7/7.
 */
class ArchitectureViewModel(private val dataBinding: ActivityArchitectureBinding) : ViewModel() {
    private val tokenRequest: MutableLiveData<AccessTokenRequest> = MutableLiveData()
    var tokenResponse: LiveData<Resource<AccessTokenResponse>>? = null

    init {
        tokenResponse = Transformations.switchMap(tokenRequest) { input ->
            if (input == null) {
                MutableLiveData<Resource<AccessTokenResponse>>()
            } else {
                Repos.getReposInstance().getTokenResponse(input, "getAccessToken")
            }
        }
    }

    fun sendRequest(request: AccessTokenRequest) {
        tokenRequest.value = request
    }

    fun refreshUI(response: AccessTokenResponse) {
        dataBinding.tokenResponse = response
    }
}