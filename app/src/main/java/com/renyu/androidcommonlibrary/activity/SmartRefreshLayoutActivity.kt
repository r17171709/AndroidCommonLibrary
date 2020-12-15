package com.renyu.androidcommonlibrary.activity

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.ActivitySmartrefreshlayoutBinding
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.views.GrayFrameLayout
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

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        try {
            if ("FrameLayout" == name) {
                val count = attrs.attributeCount
                for (i in 0 until count) {
                    val attributeName = attrs.getAttributeName(i)
                    val attributeValue = attrs.getAttributeValue(i)
                    if (attributeName == "id") {
                        val id = attributeValue.substring(1).toInt()
                        val idVal = resources.getResourceName(id)
                        if ("android:id/content" == idVal) {
                            return GrayFrameLayout(context, attrs)
                        }
                    }
                }
            }
        } catch (e: Exception) {

        }
        return super.onCreateView(name, context, attrs)
    }
}