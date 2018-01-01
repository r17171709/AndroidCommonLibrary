package com.renyu.commonlibrary.views.actionsheet;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by renyu on 2017/2/22.
 */

public class ActionSheetUtils {

    public static void showBeforeDate(FragmentActivity activity, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.BEFOREDATE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showAfterDate(FragmentActivity activity, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.AFTERDATE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static ActionSheetFragment showAfterDateWithoutDismiss(FragmentActivity activity, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        return ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.AFTERDATE).setCanDismiss(false)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showDateRange(FragmentActivity activity, String title, String cancelTitle, String okTitle, long startRangeTime, long endRangeTime, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.DATERANGE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setTimeRange(startRangeTime, endRangeTime)
                .setOnOKListener(onOKListener).setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showTime(FragmentActivity activity, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.TIME)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }

    public static void showList(FragmentActivity activity, String title, String[] items, ActionSheetFragment.OnItemClickListener onItemClickListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build().setChoice(ActionSheetFragment.CHOICE.ITEM)
                .setTitle(title).setListItems(items, onItemClickListener)
                .setOnCancelListener(onCancelListener).show(activity);
    }
}
