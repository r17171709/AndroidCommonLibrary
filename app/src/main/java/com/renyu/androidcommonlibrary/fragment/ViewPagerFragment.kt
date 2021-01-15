package com.renyu.androidcommonlibrary.fragment

import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.basefrag.BaseFragment
import kotlinx.android.synthetic.main.fragment_viewpager.*

/**
 * Created by renyu on 2018/1/18.
 */
class ViewPagerFragment(val color: Int) : BaseFragment() {
    override fun initParams() {
        layout_vp.setBackgroundColor(color)
    }

    override fun initViews() = R.layout.fragment_viewpager

    override fun loadData() {
    }
}