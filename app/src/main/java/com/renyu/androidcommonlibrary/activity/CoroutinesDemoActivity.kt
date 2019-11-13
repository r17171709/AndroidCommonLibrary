package com.renyu.androidcommonlibrary.activity

import android.app.ProgressDialog
import android.graphics.Color
import com.blankj.utilcode.util.Utils
import com.renyu.androidcommonlibrary.BR
import com.renyu.androidcommonlibrary.ExampleApp
import com.renyu.androidcommonlibrary.R
import com.renyu.androidcommonlibrary.databinding.ActivityCoroutinesdemoBinding
import com.renyu.androidcommonlibrary.fragment.CoroutinesDemoFragment
import com.renyu.commonlibrary.baseact.BaseDataBindingActivity
import com.renyu.commonlibrary.network.OKHttpUtils
import kotlinx.coroutines.*
import javax.inject.Inject

class CoroutinesDemoActivity : BaseDataBindingActivity<ActivityCoroutinesdemoBinding>() {
    @JvmField
    @Inject
    var okHttpUtils: OKHttpUtils? = null

    private var job: Job? = null

    private var networkDialg: ProgressDialog? = null

    private val fragment by lazy {
        CoroutinesDemoFragment.getInstance()
    }

    override fun initParams() {
        (Utils.getApp() as ExampleApp).appComponent.plusAct().inject(this)
        supportFragmentManager.beginTransaction().replace(R.id.layout_coroutine, fragment).commitAllowingStateLoss()
        viewDataBinding.setVariable(BR.CoroutinesDemoTitle, "标题")
    }

    override fun initViews() = R.layout.activity_coroutinesdemo

    override fun loadData() {
        job = GlobalScope.launch(Dispatchers.Main) {
            showNetworkDialog("正在加载数据")
            try {
                fragment.updateValue(getRemoteData())
            } catch (e: Exception) {

            } finally {
                dismissNetworkDialog()
            }
        }
        job!!.invokeOnCompletion {
            // 被取消了
            println(it)
            if (it != null) {
                dismissNetworkDialog()
            }
        }
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    private suspend fun getRemoteData() = withContext(Dispatchers.Default) {
        val responseBody = okHttpUtils!!.syncGet("http://www.mocky.io/v2/5943e4dc1200000f08fcb4d4").body()
        if (responseBody == null) {
            throw Exception("出现异常")
        } else {
            responseBody.string()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun showNetworkDialog(content: String) {
        if (networkDialg == null || (networkDialg != null && !networkDialg!!.isShowing && !isFinishing))
            networkDialg = ProgressDialog.show(this, "", content)
    }

    private fun dismissNetworkDialog() {
        if (networkDialg != null && networkDialg!!.isShowing) {
            networkDialg!!.dismiss()
            networkDialg = null
        }
    }
}