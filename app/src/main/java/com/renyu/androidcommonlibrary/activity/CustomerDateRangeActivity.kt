package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.utils.CustomerDateRangeUtils
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFactory
import com.renyu.commonlibrary.views.actionsheet.fragment.ActionSheetFragment
import com.renyu.commonlibrary.views.actionsheet.fragment.CustomActionSheetFragment
import kotlinx.android.synthetic.main.activity_actionsheet.*

class CustomerDateRangeActivity : BaseActivity() {
    var fragment: CustomActionSheetFragment? = null
    private val dateRangeUtils: CustomerDateRangeUtils by lazy {
        CustomerDateRangeUtils(
            System.currentTimeMillis(),
            1617615637000L,
            false
        )
    }
    private val dateRangeUtils2: CustomerDateRangeUtils by lazy {
        CustomerDateRangeUtils(
            System.currentTimeMillis(),
            1617615637000L,
            false
        )
    }

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 1

    override fun initParams() {
        val viewDateChoice = addAction()

        btn_click.setOnClickListener {
            fragment = ActionSheetFactory.createCustomActionSheetFragment(
                this, "", "自定义视图", Color.BLUE,
                "确定", Color.RED,
                "取消", Color.GRAY,
                false,
                viewDateChoice,
                ActionSheetFragment.OnOKListener {
                    ToastUtils.showLong("${dateRangeUtils.result} 至 ${dateRangeUtils2.result}")
                    fragment?.dismiss()
                },
                ActionSheetFragment.OnCancelListener {

                })
        }
    }

    override fun initViews() = R.layout.activity_actionsheet

    private fun addAction(): View {
        val view = LayoutInflater.from(this)
            .inflate(R.layout.activity_customerdaterange, null, false)
        dateRangeUtils.showDateRange(
            (view.findViewById<ViewGroup>(R.id.layout_customerdaterange)).getChildAt(
                0
            )
        )
        dateRangeUtils2.showDateRange(
            (view.findViewById<ViewGroup>(R.id.layout_customerdaterange)).getChildAt(
                2
            )
        )
        return view
    }
}