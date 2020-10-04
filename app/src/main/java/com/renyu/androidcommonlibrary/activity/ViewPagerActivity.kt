package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.fragment.ColorFragment
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.commonutils.BarUtils
import com.renyu.commonlibrary.views.LineIndicatorView

/**
 * Created by renyu on 2017/9/15.
 * 不错的文章：https://www.jianshu.com/p/266861496508
 * adapter.notifyDataSetChanged起作用的关键，在于Fragment在增删改查之后要重新创建
 */
class ViewPagerActivity : BaseActivity() {
    override fun initParams() {
        findViewById<LineIndicatorView>(R.id.indicator_vp).setIndicatorNums(2)
        findViewById<LineIndicatorView>(R.id.indicator_vp).setCurrentPosition(0)

        supportFragmentManager.beginTransaction().replace(R.id.layout_vp, ColorFragment(), "tag")
            .commitAllowingStateLoss()

        Handler().postDelayed({
            findViewById<LineIndicatorView>(R.id.indicator_vp).setCurrentPosition(1)
        }, 4000)
    }

    override fun initViews(): Int {
        return R.layout.activity_viewpager
    }

    override fun loadData() {

    }

    override fun setStatusBarColor(): Int {
        return Color.BLUE
    }

    override fun setStatusBarTranslucent(): Int {
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setDark(this)
        super.onCreate(savedInstanceState)
    }
}