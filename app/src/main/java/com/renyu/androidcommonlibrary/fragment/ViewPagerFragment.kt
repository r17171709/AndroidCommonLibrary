package com.renyu.androidcommonlibrary.fragment

import android.os.Handler
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.basefrag.BaseFragment
import com.renyu.commonlibrary.dialog.NetworkLoadingDialog
import kotlinx.android.synthetic.main.fragment_viewpager.*

/**
 * Created by renyu on 2018/1/18.
 */
class ViewPagerFragment(val color: Int) : BaseFragment() {
    private var dialog = NetworkLoadingDialog.getInstance()

    override fun initParams() {
        tv_vp.setTextColor(color)
    }

    override fun initViews() = R.layout.fragment_viewpager

    override fun loadData() {
        dialog = NetworkLoadingDialog.getInstance()
        dialog.show(activity)
        Handler().postDelayed({
            dialog.close()
        }, 1000)
    }
}