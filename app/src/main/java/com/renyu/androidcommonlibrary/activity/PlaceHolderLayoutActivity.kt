package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import me.samlss.broccoli.Broccoli
import me.samlss.broccoli.BroccoliGradientDrawable
import me.samlss.broccoli.PlaceholderParameter

class PlaceHolderLayoutActivity : BaseActivity() {
    private val broccoli by lazy { Broccoli() }

    override fun initParams() {
        broccoli.addPlaceholders(
            PlaceholderParameter.Builder().setView(findViewById(R.id.iv_placeholder))
                .setDrawable(createOvalDrawable(Color.parseColor("#DDDDDD"))).build(),
            PlaceholderParameter.Builder().setView(findViewById(R.id.tv_placeholder))
                .setDrawable(createRectangleDrawable(Color.parseColor("#DDDDDD"), 0f)).build(),
            PlaceholderParameter.Builder().setView(findViewById(R.id.iv_placeholder2))
                .setDrawable(
                    BroccoliGradientDrawable(
                        Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"),
                        0f,
                        1000,
                        LinearInterpolator()
                    )
                ).build()
        )
        broccoli.show()
        Handler(Looper.myLooper()!!).postDelayed({
            broccoli.removeAllPlaceholders()
        }, 1500)
    }

    override fun initViews() = R.layout.activity_placeholderlayout

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private fun createOvalDrawable(color: Int): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setColor(color)
        return gradientDrawable
    }

    private fun createRectangleDrawable(color: Int, radius: Float): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.shape = GradientDrawable.OVAL
        gradientDrawable.setColor(color)
        gradientDrawable.cornerRadius = radius
        return gradientDrawable
    }
}