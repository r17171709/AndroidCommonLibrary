package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_signin.*
import java.util.concurrent.TimeUnit
import com.blankj.utilcode.util.BarUtils as BarUtils1
import com.renyu.commonlibrary.commonutils.BarUtils as BarUtils2

class SignInActivity : BaseActivity() {
    private var previousKeyboardHeight = -1

    override fun initParams() {
        val rootView = findViewById<FrameLayout>(android.R.id.content).getChildAt(0)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            window.decorView.getWindowVisibleDisplayFrame(rect)
            val displayHeight = rect.bottom - rect.top
            val decorViewHeight =
                window.decorView.height - BarUtils1.getStatusBarHeight() - BarUtils2.getNavBarHeight()
            // 输入法的高度
            val keyboardHeight = decorViewHeight - displayHeight
            if (previousKeyboardHeight != keyboardHeight) {
                val hide = displayHeight.toDouble() / decorViewHeight > 0.8
                if (hide) {
                    rootView.scrollTo(0, 0)
                } else {
                    btn_signin.post {
                        rootView.scrollBy(0, keyboardHeight - (decorViewHeight - btn_signin.bottom))
                    }
                }
            }
            previousKeyboardHeight = decorViewHeight
        }
    }

    override fun initViews() = R.layout.activity_signin

    override fun loadData() {
        val textChanges: Array<Observable<CharSequence>> = arrayOf(
            tv_signin_uname.textChanges(),
            tv_signin_password.textChanges()
        )
        Observable.combineLatestArray(textChanges) {
            it.all { value ->
                value.toString().isNotEmpty()
            }
        }.subscribe {
            if (it) {
                ToastUtils.showShort("可以点击")
            } else {
                ToastUtils.showShort("不可点击")
            }
        }
        btn_signin.apply {
            onAntiShakeClick {
                ToastUtils.showShort("点击了")
            }
        }
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}

fun View.onAntiShakeClick(block: (View) -> Unit) {
    clicks().throttleFirst(2, TimeUnit.SECONDS).subscribe {
        block.invoke(this)
    }
}