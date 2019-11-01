package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.ComponentName
import android.content.Context
import android.content.Intent

class VIVOSettingsIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        val intent = Intent()
        intent.putExtra("packagename", context.packageName)

        // vivo x7 Y67 Y85
        intent.setClassName(
            "com.iqoo.secure",
            "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"
        )
        if (SettingIntentGenerator.checkIntentAvailable(context, intent)) {
            return intent
        }

        // vivo Y66 x20 x9
        intent.setClassName(
            "com.vivo.permissionmanager",
            "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"
        )
        if (SettingIntentGenerator.checkIntentAvailable(context, intent)) {
            return intent
        }

        // Y85
        intent.setClassName(
            "com.vivo.permissionmanager",
            "com.vivo.permissionmanager.activity.PurviewTabActivity"
        )
        if (SettingIntentGenerator.checkIntentAvailable(context, intent)) {
            return intent
        }

        // 跳转会报 java.lang.SecurityException: Permission Denial
        intent.setClassName(
            "com.android.packageinstaller",
            "com.android.packageinstaller.permission.ui.ManagePermissionsActivity"
        )
        if (SettingIntentGenerator.checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.component = ComponentName(
            "com.iqoo.secure",
            "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"
        )
        return if (checkIntentAvailable(context, intent)) {
            intent
        } else null
    }
}