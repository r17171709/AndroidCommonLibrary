package com.renyu.commonlibrary.commonutils;

import android.content.Context;

/**
 * Created by renyu on 2017/6/12.
 */

public class ResourceUtils {
    public static int getLayoutId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "layout", context.getPackageName());
    }

    public static int getStringId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    public static int getDrawableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
    }

    public static int getMipmapId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "mipmap", context.getPackageName());
    }

    public static int getStyleId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    public static int getId(Context context, String resName) {

        return context.getResources().getIdentifier(resName, "id", context.getPackageName());
    }

    public static int getColorId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "color", context.getPackageName());
    }

    public static int getValuesId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "style", context.getPackageName());
    }

    public static int getStyleableId(Context context, String resName) {
        return context.getResources().getIdentifier(resName, "styleable", context.getPackageName());
    }
}
