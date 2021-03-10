package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.*
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.databinding.ActivityArchitectureBinding
import com.renyu.androidcommonlibrary.repository.CoroutineRepos
import com.renyu.androidcommonlibrary.repository.Repos
import com.renyu.commonlibrary.network.other.Resource
import kotlinx.coroutines.Dispatchers

/**
 * Created by Administrator on 2018/7/7.
 */
class ArchitectureViewModel(private val dataBinding: ActivityArchitectureBinding) : ViewModel() {
    private val tokenRequest: MutableLiveData<AccessTokenRequest> = MutableLiveData()
    var tokenResponse: LiveData<Resource<AccessTokenResponse>>? = null

    private val tokenRequest2: MutableLiveData<AccessTokenRequest> = MutableLiveData()
    var tokenResponse2: LiveData<Resource<AccessTokenResponse>>? = null

    private val tokenRequest3: MutableLiveData<AccessTokenRequest> = MutableLiveData()
    var tokenResponse3: LiveData<Resource<AccessTokenResponse>>? = null

    init {
        tokenResponse = Transformations.switchMap(tokenRequest) { input ->
            if (input == null) {
                MutableLiveData()
            } else {
                Repos.getReposInstance().getTokenResponse(input, "getAccessToken")
            }
        }

        tokenResponse2 = Transformations.switchMap(tokenRequest2) { input ->
            if (input == null) {
                MutableLiveData()
            } else {
                CoroutineRepos.getCoroutineReposInstance().getAccessToken2(
                    viewModelScope.coroutineContext + Dispatchers.Main.immediate,
                    input
                )
            }
        }

        tokenResponse3 = Transformations.switchMap(tokenRequest3) { input ->
            if (input == null) {
                MutableLiveData()
            } else {
                CoroutineRepos.getCoroutineReposInstance().getAccessToken3(viewModelScope, input)
            }
        }
    }

    fun sendRequest(request: AccessTokenRequest) {
        tokenRequest.value = request
    }

    fun refreshUI(response: AccessTokenResponse) {
        dataBinding.tokenResponse = response
    }

    fun sendRequest2(request: AccessTokenRequest) {
        tokenRequest2.value = request
    }

    fun sendRequest3(request: AccessTokenRequest) {
        tokenRequest3.value = request
    }
}
