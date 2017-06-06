package com.renyu.commonlibrary.views.actionsheet;

import android.support.v4.app.FragmentManager;

/**
 * Created by renyu on 2017/2/22.
 */

public class ActionSheetUtils {

    public static void showBeforeDate(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.BEFOREDATE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static void showAfterDate(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.AFTERDATE)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static ActionSheetFragment showAfterDateWithoutDismiss(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        return ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.AFTERDATE).setCanDismiss(false)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static void showTime(FragmentManager manager, String title, String cancelTitle, String okTitle, ActionSheetFragment.OnOKListener onOKListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.TIME)
                .setTitle(title).setOkTitle(okTitle).setCancelTitle(cancelTitle).setOnOKListener(onOKListener)
                .setOnCancelListener(onCancelListener).show();
    }

    public static void showList(FragmentManager manager, String title, String[] items, ActionSheetFragment.OnItemClickListener onItemClickListener, ActionSheetFragment.OnCancelListener onCancelListener) {
        ActionSheetFragment.build(manager).setChoice(ActionSheetFragment.CHOICE.ITEM)
                .setTitle(title).setListItems(items, onItemClickListener)
                .setOnCancelListener(onCancelListener).show();
    }
}
