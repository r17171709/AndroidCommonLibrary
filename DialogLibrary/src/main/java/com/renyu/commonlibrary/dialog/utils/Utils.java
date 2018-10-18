package com.renyu.commonlibrary.dialog.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/10/18.
 */
public class Utils {
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
