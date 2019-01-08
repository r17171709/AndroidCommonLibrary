package com.renyu.commonlibrary.views.actionsheet;

import android.support.v4.app.FragmentActivity;

/**
 * Created by renyu on 2017/2/22.
 */

public class ActionSheetUtils {
    public static void showDateRange(FragmentActivity activity, String title, String cancelTitle, String okTitle,
                                     long startTime, long endTime, boolean isNeedHM,
                                     ActionSheetFragment.OnOKListener onOKListener,
                                     ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.DATERANGE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setTimeRange(startTime, endTime, isNeedHM)
                .setOnOKListener(onOKListener).setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showTime(FragmentActivity activity, String title, String cancelTitle, String okTitle,
                                int hour, int minute,
                                ActionSheetFragment.OnOKListener onOKListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.TIME)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle)
                .setTimeHour(hour).setTimeMinute(minute)
                .setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showList(FragmentActivity activity, String title,
                                String[] items, String[] subItems, int setChoiceIndex,
                                ActionSheetFragment.OnItemClickListener onItemClickListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.ITEM)
                .setTitle(title).setListItems(items, subItems, onItemClickListener)
                .setChoiceIndex(setChoiceIndex)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showGrid(FragmentActivity activity, String title, String cancelTitle,
                                String[] items, int[] imageItems, int columnCount,
                                ActionSheetFragment.OnItemClickListener onItemClickListener,
                                ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.GRID)
                .setTitle(title).setCancelTitle(cancelTitle)
                .setGridItems(items, imageItems, columnCount, onItemClickListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }
}
