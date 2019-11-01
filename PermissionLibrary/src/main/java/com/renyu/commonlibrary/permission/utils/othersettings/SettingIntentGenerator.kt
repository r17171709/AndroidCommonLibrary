package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings

abstract class SettingIntentGenerator {
    fun generatorAppDetailIntent(context: Context): Intent? {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context.packageName)
        )
    }

    abstract fun onGeneratorDangerousIntent(context: Context): Intent?

    companion object {
        fun checkIntentAvailable(context: Context, intent: Intent): Boolean {
            return context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            ).isNotEmpty()
        }
    }
}