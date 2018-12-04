package com.renyu.androidcommonlibrary.activity

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.renyu.androidcommonlibrary.contentobserver.SMSContentObserver
import com.renyu.commonlibrary.permission.annotation.NeedPermission
import com.renyu.commonlibrary.permission.annotation.PermissionDenied
import java.lang.ref.WeakReference


class SMSActivity : AppCompatActivity() {
    private val mHandler = MyHandler(this)

    class MyHandler(activity: SMSActivity) : Handler() {
        private val softReference: WeakReference<SMSActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            if (softReference.get() != null) {

            }
        }
    }

    private val smsContentObserver: SMSContentObserver by lazy {
        SMSContentObserver(this, mHandler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        check()
        contentResolver.registerContentObserver(
            Uri.parse("content://sms/"), true, smsContentObserver
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(smsContentObserver)
        mHandler.removeCallbacksAndMessages(null)
    }

    @NeedPermission(
        permissions = [Manifest.permission.READ_SMS],
        deniedDesp = "请授予短信读取权限"
    )
    fun check() {

    }

    @PermissionDenied(permissions = [Manifest.permission.READ_SMS])
    fun permissionDenied() {

    }
}