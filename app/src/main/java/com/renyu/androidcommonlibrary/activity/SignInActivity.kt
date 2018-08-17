package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.graphics.Rect
import android.widget.FrameLayout
import com.blankj.utilcode.util.BarUtils as BarUtils1
import com.renyu.commonlibrary.commonutils.BarUtils as BarUtils2
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import kotlinx.android.synthetic.main.activity_signin.*

class SignInActivity : BaseActivity() {
    private var previousKeyboardHeight = -1

    override fun initParams() {
        val rootView = findViewById<FrameLayout>(android.R.id.content).getChildAt(0)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val displayHeight = rect.bottom - rect.top
            val decorViewHeight = window.decorView.height - BarUtils1.getStatusBarHeight() - BarUtils2.getNavBarHeight()
            // 输入法的高度
            val keyboardHeight = decorViewHeight - displayHeight
            if (previousKeyboardHeight != keyboardHeight) {
                val hide = displayHeight.toDouble() / decorViewHeight > 0.8
                if (hide) {
                    rootView.scrollTo(0, 0)
                }
                else {
                    btn_signin.post {
                        rootView.scrollBy(0, keyboardHeight - (decorViewHeight - btn_signin.bottom))
                    }
                }
            }
            previousKeyboardHeight = decorViewHeight
        }
    }

    override fun initViews() = R.layout.activity_signin

    override fun loadData() {}

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}