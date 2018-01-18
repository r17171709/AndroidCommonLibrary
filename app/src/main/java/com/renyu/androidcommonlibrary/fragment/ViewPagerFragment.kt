package com.renyu.androidcommonlibrary.fragment

import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.basefrag.BaseFragment
import com.tencent.mars.xlog.Log

/**
 * Created by renyu on 2018/1/18.
 */
class ViewPagerFragment : BaseFragment() {
    override fun initParams() {

    }

    override fun initViews() = R.layout.fragment_viewpager

    override fun loadData() {
        Log.d("ViewPagerFragment", hashCode().toString()+"loadData")
    }
}