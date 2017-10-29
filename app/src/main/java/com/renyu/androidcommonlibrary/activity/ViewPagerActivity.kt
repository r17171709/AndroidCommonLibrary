package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.views.LineIndicatorView

/**
 * Created by renyu on 2017/9/15.
 */
class ViewPagerActivity : BaseActivity() {

    override fun initParams() {
        findViewById<LineIndicatorView>(R.id.indicator_index).setType(LineIndicatorView.TYPE.CIRCLE)
        findViewById<LineIndicatorView>(R.id.indicator_index).setColor(R.drawable.shape_select, R.drawable.shape_normal)
        findViewById<LineIndicatorView>(R.id.indicator_index).setCircleIndicatorNums(2)
        findViewById<LineIndicatorView>(R.id.indicator_index).setCurrentPosition(0)
    }

    override fun initViews(): Int {
        return R.layout.activity_viewpager
    }

    override fun loadData() {

    }

    override fun setStatusBarColor(): Int {
        return Color.BLACK
    }

    override fun setStatusBarTranslucent(): Int {
        return 0
    }
}