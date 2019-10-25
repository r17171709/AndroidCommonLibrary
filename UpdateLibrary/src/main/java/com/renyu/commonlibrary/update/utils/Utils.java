package com.renyu.commonlibrary.update.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.WindowManager;

import androidx.core.content.FileProvider;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/10/18.
 */
public class Utils {
    /**
     * 安装app
     *
     * @param context
     * @param path
     */
    public static Intent install(Context context, String path) {
        if (path == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri downloadURI = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", new File(path));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(downloadURI, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 判断下载文件是否完整
     *
     * @param context
     * @param path
     * @return
     */
    public static boolean checkAPKState(Context context, String path) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            if (pi == null) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
            return pi != null;
        } catch (Exception e) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            return false;
        }
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

    public static String bytes2mb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        return ((int) returnValue + "MB");
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }
}
