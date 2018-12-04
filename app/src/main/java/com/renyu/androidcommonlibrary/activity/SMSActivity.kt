package com.renyu.androidcommonlibrary.activity

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.renyu.androidcommonlibrary.contentobserver.SMSContentObserver
import com.renyu.commonlibrary.permission.annotation.NeedPermission
import com.renyu.commonlibrary.permission.annotation.PermissionDenied

class SMSActivity : AppCompatActivity() {
    private val mHandler = object : Handler() {}

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