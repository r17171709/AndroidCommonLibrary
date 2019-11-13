package com.renyu.androidcommonlibrary.fragment

import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.FragmentCoroutinesdemoBinding
import com.renyu.androidcommonlibrary.utils.bindArgument
import com.renyu.commonlibrary.basefrag.BaseDataBindingFragment

class CoroutinesDemoFragment : BaseDataBindingFragment<FragmentCoroutinesdemoBinding>() {
    val value: Int by bindArgument("key")

    companion object {
        fun getInstance(): CoroutinesDemoFragment {
            val fragment = CoroutinesDemoFragment()
            val bundle = Bundle()
            bundle.putInt("key", 1)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initParams() {
        ToastUtils.showShort("$value")
    }

    override fun initViews() = R.layout.fragment_coroutinesdemo

    override fun loadData() {

    }

    fun updateValue(value: String) {
        viewDataBinding.setVariable(BR.CoroutinesDemoValue, value)
    }
}