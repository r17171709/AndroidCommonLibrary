package com.renyu.androidcommonlibrary.bean

import android.databinding.ObservableField
import android.databinding.ObservableInt

/**
 * Created by Administrator on 2019/5/9.
 */
data class Demo(
        // 可以直接在DataBiding中通过对象刷新
        var access_token: ObservableField<String>,
        var expires_in: ObservableInt
)