package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.Context
import android.content.Intent

class AppDetailIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        return generatorAppDetailIntent(context)
    }
}