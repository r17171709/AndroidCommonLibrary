package com.renyu.commonlibrary.commonutils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by renyu on 16/2/23.
 */
public class PermissionsUtils {

    /**
     * 判断是否缺少权限
     * @param context
     * @param permissions
     * @return
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)== PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户关闭并不再提醒所有权限提示
     * @param activity
     * @param permissions
     * @return
     */
    public static boolean hasDelayAllPermissions(Activity activity, String... permissions) {
        int count=0;
        for (String permission : permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) && ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                count++;
            }
        }
        if (count!=0) {
            return false;
        }
        return true;
    }

    /**
     * 申请权限
     * @param context
     * @param permissions
     */
    public static void requestPermissions(Context context, String... permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 1000);
    }
}
