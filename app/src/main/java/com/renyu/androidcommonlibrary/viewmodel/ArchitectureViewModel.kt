package com.renyu.androidcommonlibrary.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import com.renyu.androidcommonlibrary.bean.TokenRequest
import com.renyu.androidcommonlibrary.bean.TokenResponse
import com.renyu.androidcommonlibrary.impl.DataActionImpl
import com.renyu.androidcommonlibrary.repository.Repos

/**
 * Created by Administrator on 2018/7/7.
 */
class ArchitectureViewModel(private val token: TokenResponse) : ViewModel() {

    private val tokenRequest: MutableLiveData<TokenRequest> = MutableLiveData()
    var tokenResponse: LiveData<TokenResponse>? = null
    val uiHandlers: MutableLiveData<DataActionImpl<TokenResponse>> = MutableLiveData()

    init {
        tokenResponse = Transformations.switchMap(tokenRequest) { input ->
            if (input == null) {
                MutableLiveData()
            } else {
                Repos.getReposInstance().getTokenResponse(input, uiHandlers)
            }
        }
    }

    fun sendRequest(request: TokenRequest) {
        tokenRequest.value = request
    }

    fun refreshUI(response: TokenResponse) {
        token.access_token.set(response.access_token.get())
        token.expires_in.set(response.expires_in.get())
    }
}