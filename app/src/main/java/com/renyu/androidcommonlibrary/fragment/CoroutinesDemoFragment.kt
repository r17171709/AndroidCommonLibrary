package com.renyu.androidcommonlibrary.fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.bean.AccessTokenResponse
import com.renyu.androidcommonlibrary.databinding.FragmentCoroutinesdemoBinding
import com.renyu.androidcommonlibrary.utils.BaseObserver2
import com.renyu.androidcommonlibrary.utils.bindArgument
import com.renyu.androidcommonlibrary.viewmodel.CoroutinesViewModel
import com.renyu.commonlibrary.basefrag.BaseDataBindingFragment
import com.renyu.commonlibrary.network.other.Resource

class CoroutinesDemoFragment : BaseDataBindingFragment<FragmentCoroutinesdemoBinding>() {
    private val value: Int by bindArgument("key")

    private val vm by lazy { getFragmentScopeViewModel(CoroutinesViewModel::class.java) }

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
        vm.getHttpRequest().observe(this, object : BaseObserver2<AccessTokenResponse>(context as AppCompatActivity) {
            override fun onError(tResource: Resource<AccessTokenResponse>?) {

            }

            override fun onSucess(tResource: Resource<AccessTokenResponse>?) {

            }
        })
    }

    fun updateValue(value: String) {
        viewDataBinding.setVariable(BR.CoroutinesDemoValue, value)
    }
}