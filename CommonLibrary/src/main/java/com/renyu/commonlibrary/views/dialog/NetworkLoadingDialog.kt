package com.renyu.commonlibrary.views.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.blankj.utilcode.util.ScreenUtils
import com.renyu.commonlibrary.R

/**
 * Created by Administrator on 2018/2/27 0027.
 */
class NetworkLoadingDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun getInstance(): NetworkLoadingDialog {
            return NetworkLoadingDialog()
        }
    }

    private var isDismiss = true
    private var manager: FragmentManager? = null
    private var onDialogDismissListener: OnDialogDismiss? = null

    fun setDialogDismissListener(onDialogDismissListener: OnDialogDismiss) {
        this.onDialogDismissListener = onDialogDismissListener
    }

    interface OnDialogDismiss {
        fun onDismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //无标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //透明状态栏
        dialog.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mWindowAttributes = dialog.window.attributes
        mWindowAttributes.width = ScreenUtils.getScreenWidth()
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        }

        val view = inflater.inflate(R.layout.dialog_networkloading, container, false)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            isDismiss = savedInstanceState.getBoolean("isDismiss")
            val activity = activity
            if (activity != null) {
                manager = activity.supportFragmentManager
            }
            dismissDialog()
        }

        val manager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (manager.isActive) {
            val focusView = activity!!.currentFocus
            if (focusView != null) {
                manager.hideSoftInputFromWindow(focusView.windowToken, 0)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        val windowParams = window!!.attributes
        windowParams.dimAmount = 0.0f
        window.attributes = windowParams
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isDismiss", isDismiss)
    }

    fun show(fragmentActivity: FragmentActivity) {
        show(fragmentActivity, "loadingDialog")
    }

    fun show(fragmentActivity: FragmentActivity, tag: String) {
        if (fragmentActivity.isDestroyed || !isDismiss) {
            return
        }
        manager = fragmentActivity.supportFragmentManager
        Handler().post {
            super.show(manager, tag)

            isDismiss = false
        }
    }

    fun close() {
        dismissDialog()
        if (onDialogDismissListener != null) {
            onDialogDismissListener!!.onDismiss()
        }
    }

    /**
     * 带有文字提示
     */
    fun closeWithText(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        close()
    }

    /**
     * 带有图文提示
     */
    fun closeWithTextAndImage(text: String, imageRes: Int) {
        val toast = Toast.makeText(activity?.applicationContext, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        val toastView = toast.view as LinearLayout
        val image = ImageView(activity?.applicationContext)
        image.setImageResource(imageRes)
        toastView.addView(image, 0)
        toast.show()
        close()
    }

    private fun dismissDialog() {
        Handler().post {
            if (isDismiss) {
                return@post
            }
            isDismiss = true
            try {
                dismissAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}