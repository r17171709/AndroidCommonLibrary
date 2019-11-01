package com.renyu.commonlibrary.permission.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.renyu.commonlibrary.permission.utils.othersettings.AppDetailIntentGenerator;
import com.renyu.commonlibrary.permission.utils.othersettings.EMUISettingsIntentGenerator;
import com.renyu.commonlibrary.permission.utils.othersettings.MIUISettingsIntentGenerator;
import com.renyu.commonlibrary.permission.utils.othersettings.MeiZuSettingsIntentGenerator;
import com.renyu.commonlibrary.permission.utils.othersettings.SamSungSettingsIntentGenerator;
import com.renyu.commonlibrary.permission.utils.othersettings.VIVOSettingsIntentGenerator;

/**
 * Created by renyu on 16/2/23.
 */
public class PermissionsUtils {
    private static Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.powercenter.PowerSettings")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity"))};

    /**
     * 选取合适的电源管理跳转页面
     *
     * @param context
     * @return
     */
    public static Intent presentPowerManagerIntent(Context context) {
        for (Intent powermanager_intent : POWERMANAGER_INTENTS) {
            if (context.getPackageManager().resolveActivity(powermanager_intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                return powermanager_intent;
            }
        }
        return null;
    }

    /**
     * 选取合适的设置跳转页面
     *
     * @param context
     * @return
     */
    public static Intent presentSettingIntent(Context context) {
        if (Build.BRAND.toLowerCase().contains("huawei")) {
            return new EMUISettingsIntentGenerator().onGeneratorDangerousIntent(context);
        } else if (Build.BRAND.toLowerCase().contains("xiaomi") || Build.BRAND.toLowerCase().contains("redmi")) {
            return new MIUISettingsIntentGenerator().onGeneratorDangerousIntent(context);
        } else if (Build.BRAND.toLowerCase().contains("vivo")) {
            return new VIVOSettingsIntentGenerator().onGeneratorDangerousIntent(context);
        } else if (Build.BRAND.toLowerCase().contains("meizu")) {
            return new MeiZuSettingsIntentGenerator().onGeneratorDangerousIntent(context);
        } else if (Build.BRAND.toLowerCase().contains("samsung")) {
            return new SamSungSettingsIntentGenerator().onGeneratorDangerousIntent(context);
        } else {
            return new AppDetailIntentGenerator().onGeneratorDangerousIntent(context);
        }
    }

    /**
     * 判断是否缺少权限
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户关闭并不再提醒所有权限提示
     * <p>
     * 华为手机第一次请求权限和勾选了Don't ask again，shouldShowRequestPermissionRationale都会返回FALSE
     *
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean hasDelayAllPermissions(Activity activity, String... permissions) {
        int count = 0;
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                count++;
            }
        }
        if (count != 0) {
            return true;
        }
        return false;
    }

    /**
     * 申请权限
     *
     * @param context
     * @param permissions
     */
    public static void requestPermissions(Context context, String... permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 1000);
    }
}
