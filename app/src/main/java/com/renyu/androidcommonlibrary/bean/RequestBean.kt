package com.renyu.androidcommonlibrary.bean

import android.databinding.ObservableField
import android.databinding.ObservableInt

/**
 * Created by Administrator on 2018/7/7.
 */
data class TokenRequest(
        var city: String,
        var timestamp: Int,
        var app_id: String,
        var rand_str: String,
        var signature: String,
        var device_id: String
)

data class TokenResponse(
        var access_token: ObservableField<String>,
        var expires_in: ObservableInt
)

data class Teacher2(var name: ObservableField<String>?, var age: ObservableField<Int>?)