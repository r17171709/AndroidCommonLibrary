package com.renyu.commonlibrary.views.actionsheet;

import android.support.v4.app.FragmentActivity;

/**
 * Created by renyu on 2017/2/22.
 */

public class ActionSheetUtils {
    public static void showList(FragmentActivity activity, String title, int titleColor, String cancelTitle, int cancelColor,
                                String[] items, String[] subItems, int setChoiceIndex,
                                ActionSheetFragment.OnItemClickListener onItemClickListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.ITEM)
                .setCancelTitle(cancelTitle, cancelColor).setTitle(title, titleColor)
                .setListItems(items, subItems, onItemClickListener)
                .setChoiceIndex(setChoiceIndex)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showCenterList(FragmentActivity activity, String title, int titleColor, String cancelTitle, int cancelColor,
                                      String[] items,
                                      ActionSheetFragment.OnItemClickListener onItemClickListener,
                                      ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.CENTERITEM)
                .setCancelTitle(cancelTitle, cancelColor).setTitle(title, titleColor)
                .setCenterListItems(items, onItemClickListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showGrid(FragmentActivity activity, String title, int titleColor, String cancelTitle, int cancelColor,
                                String[] items, int[] imageItems, int columnCount,
                                ActionSheetFragment.OnItemClickListener onItemClickListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.GRID)
                .setTitle(title, titleColor).setCancelTitle(cancelTitle, cancelColor)
                .setGridItems(items, imageItems, columnCount, onItemClickListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showToutiaoChoice(FragmentActivity activity, String cancelTitle, int cancelColor,
                                         String[] topTitles, int[] topImages,
                                         String[] bottomTitles, int[] bottomImages,
                                         ActionSheetFragment.OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener,
                                         ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.TOUTIAO).setCancelTitle(cancelTitle, cancelColor)
                .setToutiaochoice(topTitles, topImages, bottomTitles, bottomImages, onToutiaoChoiceItemClickListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showTime(FragmentActivity activity, String title, int titleColor,
                                String cancelTitle, int cancelColor,
                                String okTitle, int okColor,
                                int hour, int minute,
                                ActionSheetFragment.OnOKListener onOKListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.TIME)
                .setTitle(title, titleColor).setOkTitle(okTitle, okColor).setCancelTitle(cancelTitle, cancelColor)
                .setTimeHour(hour).setTimeMinute(minute)
                .setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showDateRange(FragmentActivity activity, String title, int titleColor,
                                     String cancelTitle, int cancelColor,
                                     String okTitle, int okColor,
                                     long startTime, long endTime, boolean isNeedHM,
                                     ActionSheetFragment.OnOKListener onOKListener,
                                     ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.DATERANGE)
                .setTitle(title, titleColor).setOkTitle(okTitle, okColor).setCancelTitle(cancelTitle, cancelColor)
                .setTimeRange(startTime, endTime, isNeedHM)
                .setOnOKListener(onOKListener).setOnCancelListener(onCancelListener).show(activity);
    }
}
