package com.renyu.androidcommonlibrary.activity

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.provider.Settings
import android.support.annotation.RequiresApi
import com.renyu.androidcommonlibrary.R
import com.renyu.commonlibrary.baseact.BaseActivity
import com.renyu.commonlibrary.bean.UpdateModel
import com.renyu.commonlibrary.commonutils.Utils
import com.renyu.commonlibrary.views.AppUpdateDialogFragment
import com.renyu.commonlibrary.views.dialog.LoadingDialog


/**
 * Created by Administrator on 2017/12/7.
 */
class OKHttpActivity : BaseActivity() {
    val pics = arrayOf("/storage/emulated/0/Pictures/dongqiudi/1512634878367.jpg",
            "/storage/emulated/0/Pictures/dongqiudi/1512635214549.jpg",
            "/storage/emulated/0/Pictures/dongqiudi/1512635218258.jpg",
            "/storage/emulated/0/Pictures/dongqiudi/1512635223683.jpg",
            "/storage/emulated/0/Pictures/dongqiudi/1512635228373.jpg")

    override fun initParams() {

    }

    override fun initViews() = R.layout.activity_main

    override fun loadData() {
        var haveInstallPermission: Boolean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = packageManager.canRequestPackageInstalls()
            if (!haveInstallPermission) {//没有权限
                val loadingDialog = LoadingDialog.getInstance_TextCommit("安装应用需要打开未知来源权限，请去设置中开启权限", "确定")
                loadingDialog.setOnDialogPosListener {
                    startInstallPermissionSettingActivity()
                }
                loadingDialog.show(this)
                return
            }
        }
        update()

        // 普通请求
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


        // 下载
//        httpHelper.okHttpUtils.asyncDownload("http://oss.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_209269_web_inner_referral_binded.apk",
//                InitParams.FILE_PATH,
//                object : OKHttpUtils.RequestListener {
//                    override fun onStart() {
//
//                    }
//
//                    override fun onSuccess(string: String?) {
//                        println("onSuccess:$string")
//                    }
//
//                    override fun onError() {
//
//                    }
//                }) { progress, _, _ -> println("1:$progress")}
//
//        httpHelper.okHttpUtils.asyncDownload("https://bos.pgzs.com/cloudsto/weitest/official_website6.0.3.268.apk",
//                InitParams.FILE_PATH,
//                object : OKHttpUtils.RequestListener {
//                    override fun onStart() {
//
//                    }
//
//                    override fun onSuccess(string: String?) {
//                        println("onSuccess:$string")
//                    }
//
//                    override fun onError() {
//
//                    }
//                }) { progress, _, _ -> println("2:$progress")}

        // 上传
//                for (i in 0 until pics.size) {
//                    Thread(Runnable {
//                        val fileHashMap = HashMap<String, File>()
//                        fileHashMap.put("fileData", File(pics[i]))
//                        httpHelper.okHttpUtils.syncUpload(
//                                "http://www.zksell.com/index.php?s=Api/Base/uploadpic",
//                                HashMap<String, String>(), fileHashMap) { currentBytesCount, totalBytesCount ->
//                            println("OKHttpActivity: $currentBytesCount   $totalBytesCount")
//                        }
//                    }).start()
//                }
    }

    override fun setStatusBarColor() = Color.BLACK

    override fun setStatusBarTranslucent() = 0

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
        startActivityForResult(intent, 10000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10000 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager.canRequestPackageInstalls()) {
            update()
        }
    }

    private fun update() {
        var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        checkPermission(permissions, "请授予SD卡读写权限", object : OnPermissionCheckedListener {
            override fun checked(flag: Boolean) {

            }

            override fun grant() {
                // 升级测试
                Handler().post {
                    val temp = UpdateModel()
                    temp.notificationTitle = "Demo"
                    temp.content = "测试"
                    temp.title = "标题"
                    temp.version = "3"
                    temp.forced = 0
                    temp.url = "http://oss.ucdl.pp.uc.cn/fs01/union_pack/Wandoujia_209269_web_inner_referral_binded.apk"
                    AppUpdateDialogFragment.getInstance(temp, 1, R.mipmap.ic_launcher, R.mipmap.ic_launcher).show(this@OKHttpActivity)
                }
            }

            override fun denied() {

            }
        })
    }
}