package com.renyu.androidcommonlibrary.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.core.view.LayoutInflaterCompat
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
        viewDataBinding.refreshSmart.autoRefresh()
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

                beans.apply {
                    for (i in 0 until 20) {
                        add("$i")
                    }
                }
                viewDataBinding.adapter!!.notifyDataSetChanged()

                if (viewDataBinding.adapter!!.itemCount < 100) {
                    viewDataBinding.refreshSmart.finishLoadMore()
                } else {
                    viewDataBinding.refreshSmart.finishLoadMoreWithNoMoreData()
                }
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                super.onRefresh(refreshLayout)

                beans.clear()
                beans.apply {
                    for (i in 0 until 20) {
                        add("$i")
                    }
                }

                if (beans.size == 0) {
                    viewDataBinding.refreshSmart.finishRefreshWithNoMoreData()
                } else {
                    viewDataBinding.refreshSmart.finishRefresh()
                }

                viewDataBinding.adapter!!.notifyDataSetChanged()
            }
        })

        viewDataBinding.rvSmart.layoutManager = LinearLayoutManager(this)
        viewDataBinding.rvSmart.itemAnimator = SlideInLeftAnimator()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        // 统一修改页面样式
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String,
                context: Context,
                attrs: AttributeSet
            ): View? {
                for (i in 0 until attrs.attributeCount) {
                    Log.d(
                        "TAGTAGTAG",
                        "${attrs.getAttributeName(i)}  ${attrs.getAttributeValue(i)}"
                    )
                }
                var view: View? = null
                if (name.indexOf(".") == 1) {
                    // 表示自定义View
                    // 通过反射创建
                    view = layoutInflater.createView(name, null, attrs)
                }
                if (view == null) {
                    //通过系统创建一系列 appcompat 的 View
                    view = delegate.createView(parent, name, context, attrs)
                }
                if (view is TextView) {
                    //如果是 TextView 或其子类，则进行相应处理
                    view.setTextColor(Color.WHITE)
                }
                return view
            }

            override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
                return null
            }
        })
        super.onCreate(savedInstanceState)
    }

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