package com.renyu.androidcommonlibrary.fragment

import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.FragmentCoroutinesdemoBinding
import com.renyu.commonlibrary.basefrag.BaseDataBindingFragment

class CoroutinesDemoFragment : BaseDataBindingFragment<FragmentCoroutinesdemoBinding>() {
    override fun initParams() {

    }

    override fun initViews() = R.layout.fragment_coroutinesdemo

    override fun loadData() {

    }

    fun updateValue(value: String) {
        viewDataBinding.setVariable(BR.CoroutinesDemoValue, value)
    }
}