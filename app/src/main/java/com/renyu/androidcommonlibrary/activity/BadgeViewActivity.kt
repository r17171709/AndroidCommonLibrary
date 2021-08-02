package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.view.Gravity
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import q.rorbin.badgeview.QBadgeView

/**
 * Created by renyu on 2017/12/19.
 */
class BadgeViewActivity : BaseActivity() {
    override fun initParams() {
        val badge = QBadgeView(this).bindTarget(findViewById(R.id.tv_badge))
        badge.badgeGravity = Gravity.END or Gravity.TOP
        badge.badgeTextColor = Color.WHITE
        badge.badgeBackgroundColor = Color.RED
        badge.setGravityOffset(0f, 0f, true)
        badge.setBadgeTextSize(8f, true)
        badge.setBadgePadding(3f, true)
        badge.badgeText = "99+"
    }

    override fun initViews() = R.layout.activity_badgeview

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}