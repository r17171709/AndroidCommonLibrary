package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.ActivitySmartrefreshlayoutBinding
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener

class SmartRefreshLayoutActivity : BaseDataBindingActivity<ActivitySmartrefreshlayoutBinding>() {
    override fun initParams() {
        //设置 Header 为 Material风格
        viewDataBinding.refreshSmart.setRefreshHeader(MaterialHeader(this))
        //设置 Footer 为 球脉冲
        viewDataBinding.refreshSmart.setRefreshFooter(BallPulseFooter(this))
        viewDataBinding.refreshSmart.setOnMultiListener(object : SimpleMultiListener() {
            override fun onStateChanged(
                refreshLayout: RefreshLayout,
                oldState: RefreshState,
                newState: RefreshState
            ) {
                super.onStateChanged(refreshLayout, oldState, newState)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                super.onLoadMore(refreshLayout)
                viewDataBinding.refreshSmart.finishLoadMore(2000)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)
                viewDataBinding.refreshSmart.finishRefresh(2000)
            }
        })
    }

    override fun initViews() = R.layout.activity_smartrefreshlayout

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}