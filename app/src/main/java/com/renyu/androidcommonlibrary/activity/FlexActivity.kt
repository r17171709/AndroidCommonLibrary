package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.adapter.FlexAdapter
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activity_flex.*

/**
 * Created by Administrator on 2019/5/23.
 */
class FlexActivity : BaseActivity() {
    override fun initParams() {
        val beans = ArrayList<String>()
        beans.add("1")
        beans.add("11111")
        beans.add("111111111")
        beans.add("1111111111111")
        beans.add("11111111111111111")
        beans.add("1111111111111")
        beans.add("111111111")
        beans.add("11111")
        beans.add("1")
        beans.add("11111")
        beans.add("111111111")
        beans.add("1111111111111")
        beans.add("11111111111111111")
        beans.add("1111111111111")
        beans.add("111111111")
        beans.add("11111")
        beans.add("1")

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.FLEX_START
        layoutManager.alignItems = AlignItems.CENTER
        layoutManager.flexWrap = FlexWrap.WRAP
        rv_flex.layoutManager = layoutManager
        rv_flex.adapter = FlexAdapter(beans)
    }

    override fun initViews() = R.layout.activity_flex

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}