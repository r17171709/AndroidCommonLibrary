package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

class SamSungSettingsIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val intent = Intent().apply {
                data = Uri.parse("package:" + context.packageName)
                component =
                    ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings\$AppOpsDetailsActivity"
                    )
            }
            return if (checkIntentAvailable(context, intent)) {
                intent
            } else null
        } else {
            generatorAppDetailIntent(context)
        }
    }
}