package com.renyu.commonlibrary.permission.utils.othersettings

import android.content.Context
import android.content.Intent

class MIUISettingsIntentGenerator : SettingIntentGenerator() {
    override fun onGeneratorDangerousIntent(context: Context): Intent? {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", context.packageName)
        if (checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.setPackage("com.miui.securitycenter")
        if (checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        if (checkIntentAvailable(context, intent)) {
            return intent
        }

        intent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
        )
        return if (checkIntentAvailable(context, intent)) {
            intent
        } else null
    }
}