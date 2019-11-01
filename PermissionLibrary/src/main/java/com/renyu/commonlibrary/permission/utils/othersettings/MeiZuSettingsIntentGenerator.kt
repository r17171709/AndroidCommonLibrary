package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.Context
import android.content.Intent

class MeiZuSettingsIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        return try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.putExtra("packageName", context.packageName)
            if (checkIntentAvailable(context, intent)) {
                intent
            } else null
        } catch (e: Exception) {
            null
        }
    }
}