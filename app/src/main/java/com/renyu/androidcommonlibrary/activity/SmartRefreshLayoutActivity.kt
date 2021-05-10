package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.SmartRefreshLayoutAdapter
import com.renyu.androidcommonlibrary.databinding.ActivitySmartrefreshlayoutBinding
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

class SmartRefreshLayoutActivity : BaseDataBindingActivity<ActivitySmartrefreshlayoutBinding>() {
    private val beans by lazy { ArrayList<String>() }
    private val adapter by lazy { SmartRefreshLayoutAdapter(beans) }

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

        viewDataBinding.rvSmart.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rvSmart.itemAnimator = SlideInLeftAnimator()
        beans.apply {
            for (i in 0 until 20) {
                add("$i")
            }
        }
        viewDataBinding.adapter = ScaleInAnimationAdapter(adapter).apply {
            setFirstOnly(true)
            setDuration(500)
            setInterpolator(OvershootInterpolator(.5f))
        }
    }

    override fun initViews() = R.layout.activity_smartrefreshlayout

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    // 页面灰色
//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        try {
//            if ("FrameLayout" == name) {
//                val count = attrs.attributeCount
//                for (i in 0 until count) {
//                    val attributeName = attrs.getAttributeName(i)
//                    val attributeValue = attrs.getAttributeValue(i)
//                    if (attributeName == "id") {
//                        val id = attributeValue.substring(1).toInt()
//                        val idVal = resources.getResourceName(id)
//                        if ("android:id/content" == idVal) {
//                            return GrayFrameLayout(context, attrs)
//                        }
//                    }
//                }
//            }
//        } catch (e: Exception) {
//
//        }
//        return super.onCreateView(name, context, attrs)
//    }
}