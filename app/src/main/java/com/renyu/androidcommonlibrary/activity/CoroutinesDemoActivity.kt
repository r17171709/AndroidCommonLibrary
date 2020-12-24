package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.ActivityCoroutinesdemoBinding
import com.renyu.androidcommonlibrary.fragment.CoroutinesDemoFragment
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity

class CoroutinesDemoActivity : BaseDataBindingActivity<ActivityCoroutinesdemoBinding>() {
    private val fragment by lazy {
        CoroutinesDemoFragment.getInstance()
    }

    override fun initParams() {
        supportFragmentManager.beginTransaction().replace(R.id.layout_coroutine, fragment)
            .commitAllowingStateLoss()
        viewDataBinding.setVariable(BR.CoroutinesDemoTitle, "标题")
    }

    override fun initViews() = R.layout.activity_coroutinesdemo

    override fun loadData() {

    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}