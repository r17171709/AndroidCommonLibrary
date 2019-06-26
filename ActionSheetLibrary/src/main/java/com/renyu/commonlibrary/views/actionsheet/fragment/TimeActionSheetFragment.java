package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.commonlibrary.views.wheelview.R;

import java.util.ArrayList;

public class TimeActionSheetFragment extends ActionSheetFragment {
    @Override
    View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_timeactionsheet, container, false);
    }

    @Override
    void initParams() {
        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i < 10 ? "0" + i : "" + i);
        }
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }
        final int[] hourSelectedItem = {getArguments().getInt("hour")};
        final int[] minuteSelectedItem = {getArguments().getInt("minute")};

        pop_ok1.setOnClickListener(v -> {
            if (onOKListener != null) {
                onOKListener.onOKClick(hours.get(hourSelectedItem[0]) + ":" + (minutes.get(minuteSelectedItem[0])));
            }
            dismiss();
        });
        pop_cancel1.setOnClickListener(v -> {
            if (onCancelListener != null) {
                onCancelListener.onCancelClick();
            }
            dismiss();
        });

        LinearLayout pop_wheel_timelayout = realView.findViewById(R.id.pop_wheel_timelayout);
        LoopView pop_wheel_timelayout_hour = realView.findViewById(R.id.pop_wheel_timelayout_hour);
        LoopView pop_wheel_timelayout_minute = realView.findViewById(R.id.pop_wheel_timelayout_minute);
        pop_wheel_timelayout.setVisibility(View.VISIBLE);
        pop_wheel_timelayout_hour.setNotLoop();
        pop_wheel_timelayout_hour.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
        pop_wheel_timelayout_hour.setItems(hours);
        pop_wheel_timelayout_hour.setTextSize(18);
        pop_wheel_timelayout_hour.setInitPosition(hourSelectedItem[0]);
        pop_wheel_timelayout_hour.setListener(index -> hourSelectedItem[0] = index);
        pop_wheel_timelayout_minute.setNotLoop();
        pop_wheel_timelayout_minute.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
        pop_wheel_timelayout_minute.setItems(minutes);
        pop_wheel_timelayout_minute.setTextSize(18);
        pop_wheel_timelayout_minute.setInitPosition(minuteSelectedItem[0]);
        pop_wheel_timelayout_minute.setListener(index -> minuteSelectedItem[0] = index);
    }

    public static TimeActionSheetFragment newTimeActionSheetFragmentInstance(FragmentActivity activity, String tag,
                                                                             String title, int titleColor,
                                                                             String okTitle, int okTitleColor,
                                                                             String cancelTitle, int cancelTitleColor,
                                                                             int hour, int minute,
                                                                             OnOKListener onOKListener,
                                                                             OnCancelListener onCancelListener) {
        TimeActionSheetFragment fragment = new TimeActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("titleColor", titleColor);
        bundle.putString("okTitle", okTitle);
        bundle.putInt("okTitleColor", okTitleColor);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putInt("hour", hour);
        bundle.putInt("minute", minute);
        fragment.setArguments(bundle);
        fragment.setOnOKListener(onOKListener);
        fragment.setOnCancelListener(onCancelListener);
        fragment.show(activity, tag);
        return fragment;
    }
}
