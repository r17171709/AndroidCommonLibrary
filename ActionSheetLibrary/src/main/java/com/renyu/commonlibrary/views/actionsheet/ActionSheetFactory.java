package com.renyu.commonlibrary.views.actionsheet;

import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.renyu.commonlibrary.views.actionsheet.fragment.*;

public class ActionSheetFactory {
    public static ListActionSheetFragment createListActionSheetFragment(FragmentActivity activity, String tag,
                                                                        String title, int titleColor,
                                                                        String cancelTitle, int cancelTitleColor,
                                                                        String[] items, String[] subItems, int choiceIndex,
                                                                        ListActionSheetFragment.OnItemClickListener onItemClickListener,
                                                                        ActionSheetFragment.OnCancelListener onCancelListener) {
        return ListActionSheetFragment.newListActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, subItems, choiceIndex, onItemClickListener, onCancelListener);
    }

    public static CenterListActionSheetFragment createCenterListActionSheetFragment(FragmentActivity activity, String tag,
                                                                                    String title, int titleColor,
                                                                                    String cancelTitle, int cancelTitleColor,
                                                                                    String[] items,
                                                                                    CenterListActionSheetFragment.OnItemClickListener onItemClickListener,
                                                                                    ActionSheetFragment.OnCancelListener onCancelListener) {
        return CenterListActionSheetFragment.newCenterListActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, onItemClickListener, onCancelListener);
    }

    public static GridActionSheetFragment createGridActionSheetFragment(FragmentActivity activity, String tag,
                                                                        String title, int titleColor,
                                                                        String cancelTitle, int cancelTitleColor,
                                                                        String[] items, int[] images, int columnCount,
                                                                        GridActionSheetFragment.OnItemClickListener onItemClickListener,
                                                                        ActionSheetFragment.OnCancelListener onCancelListener) {
        return GridActionSheetFragment.newGridActionSheetFragmentInstance(activity, tag, title, titleColor, cancelTitle, cancelTitleColor, items, images, columnCount, onItemClickListener, onCancelListener);
    }

    public static TouTiaoActionSheetFragment createTouTiaoActionSheetFragment(FragmentActivity activity, String tag,
                                                                              String cancelTitle, int cancelTitleColor,
                                                                              String[] topTitles, int[] topImages,
                                                                              String[] bottomTitles, int[] bottomImages,
                                                                              TouTiaoActionSheetFragment.OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener,
                                                                              ActionSheetFragment.OnCancelListener onCancelListener) {
        return TouTiaoActionSheetFragment.newTouTiaoActionSheetFragmentInstance(activity, tag, cancelTitle, cancelTitleColor, topTitles, topImages, bottomTitles, bottomImages, onToutiaoChoiceItemClickListener, onCancelListener);
    }


    public static TimeActionSheetFragment createTimeActionSheetFragment(FragmentActivity activity, String tag,
                                                                        String title, int titleColor,
                                                                        String okTitle, int okColor,
                                                                        String cancelTitle, int cancelColor,
                                                                        int hour, int minute,
                                                                        ActionSheetFragment.OnOKListener onOKListener,
                                                                        ActionSheetFragment.OnCancelListener onCancelListener) {
        return TimeActionSheetFragment.newTimeActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okColor, cancelTitle, cancelColor, hour, minute, onOKListener, onCancelListener);
    }

    public static DateRangeActionSheetFragment createDateRangeActionSheetFragment(FragmentActivity activity, String tag,
                                                                                  String title, int titleColor,
                                                                                  String okTitle, int okTitleColor,
                                                                                  String cancelTitle, int cancelTitleColor,
                                                                                  long startTime, long endTime, boolean isNeedHM,
                                                                                  ActionSheetFragment.OnOKListener onOKListener,
                                                                                  ActionSheetFragment.OnCancelListener onCancelListener) {
        return DateRangeActionSheetFragment.newDateRangeActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okTitleColor, cancelTitle, cancelTitleColor, startTime, endTime, isNeedHM, onOKListener, onCancelListener);
    }

    public static CustomActionSheetFragment createCustomActionSheetFragment(FragmentActivity activity, String tag,
                                                                            String title, int titleColor,
                                                                            String okTitle, int okTitleColor,
                                                                            String cancelTitle, int cancelTitleColor,
                                                                            boolean canDismiss, View customView,
                                                                            ActionSheetFragment.OnOKListener onOKListener,
                                                                            ActionSheetFragment.OnCancelListener onCancelListener) {
        return CustomActionSheetFragment.newCustomActionSheetFragmentInstance(activity, tag, title, titleColor, okTitle, okTitleColor, cancelTitle, cancelTitleColor, canDismiss, customView, onOKListener, onCancelListener);
    }
}
