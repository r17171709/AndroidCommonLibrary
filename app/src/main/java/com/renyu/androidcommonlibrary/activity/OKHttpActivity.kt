package com.renyu.androidcommonlibrary.activity

import android.graphics.Color
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.commonutils.Utils
import com.renyu.commonlibrary.network.OKHttpUtils
import com.renyu.commonlibrary.params.InitParams

/**
 * Created by Administrator on 2017/12/7.
 */
class OKHttpActivity : BaseActivity() {
    override fun initParams() {

    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        Thread(Runnable {
            for (i in 0..10) {
                val timestamp = (System.currentTimeMillis() / 1000).toInt()
                val random = "abcdefghijklmn"
                val signature = "app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id=" +
                        Utils.getUniquePsuedoID() + "&rand_str=" + random + "&timestamp=" + timestamp
                val url = "https://aznapi.house365.com/api/58bf98c1dcb63?city=nj&timestamp=" + timestamp +
                        "&app_id=46877648&rand_str=" + random +
                        "&signature=" + Utils.getMD5(signature) +
                        "&device_id=" + Utils.getUniquePsuedoID()
                val headMaps = HashMap<String, String>()
                headMaps.put("version", "v1.0")
                headMaps.put("debug", "0")
                val tokenResponse = httpHelper.okHttpUtils.syncGet(url, headMaps)
                println(tokenResponse.body()!!.string())
            }
        }).start()

//        Handler().post {
//            val temp = UpdateModel()
//            temp.content = "测试"
//            temp.title = "标题"
//            temp.version = "3"
//            temp.forced = 0
//            temp.url = "http://oss.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_209269_web_inner_referral_binded.apk"
//            AppUpdateDialogFragment.getInstance(temp, 1).show(supportFragmentManager, "update")
//        }

        httpHelper.okHttpUtils.asyncDownload("http://oss.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_209269_web_inner_referral_binded.apk",
                InitParams.FILE_PATH,
                object : OKHttpUtils.RequestListener {
                    override fun onStart() {

                    }

                    override fun onSuccess(string: String?) {
                        println("onSuccess:$string")
                    }

                    override fun onError() {

                    }
                }) { progress, _, _ -> println("1:$progress")}

        httpHelper.okHttpUtils.asyncDownload("https://bos.pgzs.com/cloudsto/weitest/official_website6.0.3.268.apk",
                InitParams.FILE_PATH,
                object : OKHttpUtils.RequestListener {
                    override fun onStart() {

                    }

                    override fun onSuccess(string: String?) {
                        println("onSuccess:$string")
                    }

                    override fun onError() {

                    }
                }) { progress, _, _ -> println("2:$progress")}
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0
}