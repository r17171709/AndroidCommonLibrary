package com.renyu.commonlibrary.views.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/10/18.
 */
public class NavigationBarUtils {
    /**
     * 获取NavigationBar高度
     *
     * @return
     */
    public static int getNavBarHeight(Activity activity) {
        int navigationBarHeight = 0;
        Resources rs = Resources.getSystem();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && isNavigationBarShow(activity)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 是否支持NavigationBar
     *
     * @return
     */
    private static boolean checkDeviceHasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = Resources.getSystem();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        if (Build.VERSION.SDK_INT < 28) {
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hasNavigationBar;
    }

    /**
     * 判断NavigationBar是否显示
     *
     * @param activity
     * @return
     */
    private static boolean isNavigationBarShow(Activity activity) {
        View decorView = activity.getWindow().getDecorView();

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point realSize = new Point();
        display.getRealSize(realSize);

        Configuration conf = activity.getResources().getConfiguration();
        if (Configuration.ORIENTATION_LANDSCAPE == conf.orientation) {
            View contentView = decorView.findViewById(android.R.id.content);
            return realSize.x != contentView.getWidth();
        } else {
            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            return realSize.y != rect.bottom;
        }
    }
}
