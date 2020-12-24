package com.renyu.androidcommonlibrary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.repository.CoroutineRepos
import com.renyu.commonlibrary.network.other.NetworkException
import com.renyu.commonlibrary.network.other.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutinesViewModel : ViewModel() {
    fun getHttpRequest(): MutableLiveData<Resource<AccessTokenResponse>> {
        val tokenResponse = MutableLiveData<Resource<AccessTokenResponse>>()
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    CoroutineRepos.getCoroutineReposInstance().getAccessToken2()
                }
            }.onSuccess {
                if (it == null) {
                    fixNetworkError(tokenResponse, null)
                } else {
                    tokenResponse.postValue(Resource.sucess(it.data))
                }
            }.onFailure {
                fixNetworkError(tokenResponse, it)
            }
        }
        return tokenResponse
    }

    private fun <T : Any> fixNetworkError(response: MutableLiveData<Resource<T>>, t: Throwable?) {
        if (t != null && t is NetworkException) {
            response.postValue(Resource.error(t))
        } else {
            val exception = NetworkException()
            exception.result = -1
            exception.setMessage("未知错误")
            response.postValue(Resource.error(exception))
        }
    }
}