package com.renyu.commonlibrary.commonutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by renyu on 2016/12/26.
 */

public class Utils {

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获得独一无二的Psuedo ID
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        return Settings.System.getString(com.blankj.utilcode.util.Utils.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * @param fraction   进度比例（0-1）
     * @param startValue 开始色值
     * @param endValue   结束色值
     * @return 当前进度的色值
     */
    public static int rgbEvaluate(float fraction, int startValue, int endValue) {
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * 设置tablayout线条的宽度
     *
     * @param context
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (tabStrip != null) {
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = null;
            try {
                ll_tab = (LinearLayout) tabStrip.get(tabs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            int left = (int) (getDisplayMetrics(context).density * leftDip);
            int right = (int) (getDisplayMetrics(context).density * rightDip);

            if (ll_tab != null) {
                for (int i = 0; i < ll_tab.getChildCount(); i++) {
                    View child = ll_tab.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = left;
                    params.rightMargin = right;
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }
        }
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 通过getRunningTasks判断App是否位于前台
     * getRunningTask方法在Android5.0以上已经被废弃，只会返回自己和系统的一些不敏感的task，不再返回其他应用的task，用此方法来判断自身App是否处于后台，仍然是有效的，但是无法判断其他应用是否位于前台，因为不再能获取信息
     *
     * @param context     上下文参数
     * @param packageName 需要检查是否位于栈顶的App的包名
     * @return
     */
    public static boolean getRunningTask(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return !TextUtils.isEmpty(packageName) && packageName.equals(cn.getPackageName());
    }

    /**
     * 获取view截图
     *
     * @param view
     * @return
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap getViewBitmap2(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(),
                0, 0,
                view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }
}
