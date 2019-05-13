package com.renyu.commonlibrary.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by renyu on 16/2/23.
 */
public class PermissionsUtils {

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
