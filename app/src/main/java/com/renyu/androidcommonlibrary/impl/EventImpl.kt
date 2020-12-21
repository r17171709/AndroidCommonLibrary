package com.renyu.androidcommonlibrary.impl

import android.view.View
import com.renyu.androidcommonlibrary.bean.AccessTokenRequest

/**
 * Created by Administrator on 2018/7/7.
 */
interface EventImpl {
    fun click(view: View, request: AccessTokenRequest)
    fun clickCourtine(view: View, request: AccessTokenRequest)
    fun clickSaveData(view: View)
}