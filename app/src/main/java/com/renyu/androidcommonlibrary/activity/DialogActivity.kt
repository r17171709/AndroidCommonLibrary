package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.databinding.ActivityDialogBinding
import com.renyu.commonlibrary.dialog.CodeDialog
import com.renyu.commonlibrary.dialog.view.VerificationCodeInput

class DialogActivity : AppCompatActivity() {
    private lateinit var viewDataBinding: ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(viewDataBinding.root)

        viewDataBinding.btnCode.text = "设置验证码"
        viewDataBinding.btnCode.setOnClickListener {
            val view = LinearLayout(this)
            view.setBackgroundColor(Color.RED)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                SizeUtils.dp2px(150f)
            )
            view.layoutParams = layoutParams
            CodeDialog().setMarginTop(SizeUtils.dp2px(100f)).setBoxNum(3)
                .setVarificationInputType(VerificationCodeInput.VerificationCodeInputType.TYPE_NUMBER)
                .setCustomerView(view).setOnCodeListener { value ->
                    ToastUtils.showShort(value)
                }.show(this)
        }
    }
}