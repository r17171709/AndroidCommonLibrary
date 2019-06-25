package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.renyu.commonlibrary.views.utils.DateRangeUtils;
import com.renyu.commonlibrary.views.wheelview.R;

public class DateRangeActionSheetFragment extends ActionSheetFragment {
    @Override
    View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_datarangeactionsheet, container, false);
    }

    @Override
    void initParams() {
        if (TextUtils.isEmpty(title)) {
            LinearLayout pop_morechoice = realView.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.GONE);
        }

        DateRangeUtils dateRangeUtils = new DateRangeUtils(getArguments().getLong("startTime"), getArguments().getLong("endTime"), getArguments().getBoolean("isNeedHM"));
        dateRangeUtils.showDateRange(this, realView, onOKListener, onCancelListener);
    }


    public static void newDateRangeActionSheetFragmentInstance(FragmentActivity activity, String tag,
                                                               String title, int titleColor,
                                                               String okTitle, int okTitleColor,
                                                               String cancelTitle, int cancelTitleColor,
                                                               long startTime, long endTime, boolean isNeedHM,
                                                               ActionSheetFragment.OnOKListener onOKListener,
                                                               ActionSheetFragment.OnCancelListener onCancelListener) {
        DateRangeActionSheetFragment fragment = new DateRangeActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("titleColor", titleColor);
        bundle.putString("okTitle", okTitle);
        bundle.putInt("okTitleColor", okTitleColor);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putLong("startTime", startTime);
        bundle.putLong("endTime", endTime);
        bundle.putBoolean("isNeedHM", isNeedHM);
        fragment.setArguments(bundle);
        fragment.setOnOKListener(onOKListener);
        fragment.setOnCancelListener(onCancelListener);
        fragment.show(activity, tag);
    }
}
