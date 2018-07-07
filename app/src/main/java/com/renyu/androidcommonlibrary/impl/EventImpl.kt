package com.renyu.androidcommonlibrary.impl

import android.view.View
import com.renyu.androidcommonlibrary.bean.TokenRequest

/**
 * Created by Administrator on 2018/7/7.
 */
interface EventImpl {
    fun click(view: View, request: TokenRequest)
}