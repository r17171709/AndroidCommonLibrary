package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.ComponentName
import android.content.Context
import android.content.Intent

class EMUISettingsIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        val intent = Intent()
        intent.component = ComponentName(
            "com.huawei.systemmanager",
            "com.huawei.permissionmanager.ui.MainActivity"
        )
        if (checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.component = ComponentName(
            "com.huawei.systemmanager",
            "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
        )
        if (checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.component = ComponentName(
            "com.android.packageinstaller",
            "com.android.packageinstaller.permission.ui.ManagePermissionsActivity"
        )
        return if (checkIntentAvailable(context, intent)) {
            intent
        } else null
    }
}