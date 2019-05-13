package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.dialog.CodeDialog
import com.renyu.commonlibrary.dialog.view.VerificationCodeInput
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        btn_code.setOnClickListener {
            val view = LinearLayout(this)
            view.setBackgroundColor(Color.RED)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(150f))
            view.layoutParams = layoutParams
            CodeDialog().setMarginTop(SizeUtils.dp2px(100f)).setBoxNum(3)
                    .setVarificationInputType(VerificationCodeInput.VerificationCodeInputType.TYPE_NUMBER)
                    .setCustomerView(view).setOnCodeListener { value ->
                        ToastUtils.showShort(value)
                    }.show(this)
        }
    }
}