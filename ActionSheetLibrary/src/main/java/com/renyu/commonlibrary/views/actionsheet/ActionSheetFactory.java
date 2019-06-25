package com.renyu.commonlibrary.views.actionsheet;

import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.renyu.commonlibrary.views.actionsheet.fragment.*;

public class ActionSheetFactory {
    public static void createListActionSheetFragment(FragmentActivity activity, String tag,
                                                     String title, int titleColor,
                                                     String cancelTitle, int cancelTitleColor,
                                                     String[] items, String[] subItems, int choiceIndex,
                                                     ListActionSheetFragment.OnItemClickListener onItemClickListener,
                                                     ActionSheetFragment.OnCancelListener onCancelListener) {
        ListActionSheetFragment.newListActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, subItems, choiceIndex, onItemClickListener, onCancelListener);
    }

    public static void createCenterListActionSheetFragment(FragmentActivity activity, String tag,
                                                           String title, int titleColor,
                                                           String cancelTitle, int cancelTitleColor,
                                                           String[] items,
                                                           CenterListActionSheetFragment.OnItemClickListener onItemClickListener,
                                                           ActionSheetFragment.OnCancelListener onCancelListener) {
        CenterListActionSheetFragment.newCenterListActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, onItemClickListener, onCancelListener);
    }

    public static void createGridActionSheetFragment(FragmentActivity activity, String tag,
                                                     String title, int titleColor,
                                                     String cancelTitle, int cancelTitleColor,
                                                     String[] items, int[] images, int columnCount,
                                                     GridActionSheetFragment.OnItemClickListener onItemClickListener,
                                                     ActionSheetFragment.OnCancelListener onCancelListener) {
        GridActionSheetFragment.newGridActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, images, columnCount, onItemClickListener, onCancelListener);
    }

    public static void createTouTiaoActionSheetFragment(FragmentActivity activity, String tag,
                                                        String cancelTitle, int cancelTitleColor,
                                                        String[] topTitles, int[] topImages,
                                                        String[] bottomTitles, int[] bottomImages,
                                                        TouTiaoActionSheetFragment.OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener,
                                                        ActionSheetFragment.OnCancelListener onCancelListener) {
        TouTiaoActionSheetFragment.newTouTiaoActionSheetFragmentInstance(activity, tag, cancelTitle, cancelTitleColor, topTitles, topImages, bottomTitles, bottomImages, onToutiaoChoiceItemClickListener, onCancelListener);
    }


    public static void createTimeActionSheetFragment(FragmentActivity activity, String tag,
                                                     String title, int titleColor,
                                                     String okTitle, int okColor,
                                                     String cancelTitle, int cancelColor,
                                                     int hour, int minute,
                                                     ActionSheetFragment.OnOKListener onOKListener,
                                                     ActionSheetFragment.OnCancelListener onCancelListener) {
        TimeActionSheetFragment.newTimeActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okColor, cancelTitle, cancelColor, hour, minute, onOKListener, onCancelListener);
    }

    public static void createDateRangeActionSheetFragment(FragmentActivity activity, String tag,
                                                          String title, int titleColor,
                                                          String okTitle, int okTitleColor,
                                                          String cancelTitle, int cancelTitleColor,
                                                          long startTime, long endTime, boolean isNeedHM,
                                                          ActionSheetFragment.OnOKListener onOKListener,
                                                          ActionSheetFragment.OnCancelListener onCancelListener) {
        DateRangeActionSheetFragment.newDateRangeActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okTitleColor, cancelTitle, cancelTitleColor, startTime, endTime, isNeedHM, onOKListener, onCancelListener);
    }

    public static void createCustomActionSheetFragment(FragmentActivity activity, String tag,
                                                       String title, int titleColor,
                                                       String okTitle, int okTitleColor,
                                                       String cancelTitle, int cancelTitleColor,
                                                       boolean canDismiss, View customView) {
        CustomActionSheetFragment.newCustomActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okTitleColor, cancelTitle, cancelTitleColor, canDismiss, customView);
    }
}
