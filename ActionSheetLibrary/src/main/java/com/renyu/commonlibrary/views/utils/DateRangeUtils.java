package com.renyu.commonlibrary.views.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.commonlibrary.views.wheelview.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateRangeUtils {

    public static String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public void showDateRange(ActionSheetFragment actionSheetFragment, View view,
                              ActionSheetFragment.OnOKListener onOKListener,
                              ActionSheetFragment.OnCancelListener onCancelListener) throws Exception {
        if (actionSheetFragment.getArguments() == null) {
            throw new Exception("ActionSheetFragment参数不完整");
        }
        LinearLayout pop_wheel_datarangelayout = view.findViewById(R.id.pop_wheel_datarangelayout);
        pop_wheel_datarangelayout.setVisibility(View.VISIBLE);
        LoopView pop_wheel_datarangelayout_year = view.findViewById(R.id.pop_wheel_datarangelayout_year);
        LoopView pop_wheel_datarangelayout_month = view.findViewById(R.id.pop_wheel_datarangelayout_month);
        LoopView pop_wheel_datarangelayout_day = view.findViewById(R.id.pop_wheel_datarangelayout_day);
        LoopView pop_wheel_datarangelayout_hour = view.findViewById(R.id.pop_wheel_datarangelayout_hour);
        LoopView pop_wheel_datarangelayout_minute = view.findViewById(R.id.pop_wheel_datarangelayout_minute);

        final ArrayList<String> years = new ArrayList<>();
        final ArrayList<String> months = new ArrayList<>();
        final LinkedHashMap<Integer, String> days = new LinkedHashMap<>();

        // 当前时间
        Calendar calendar_today = Calendar.getInstance();
        calendar_today.setTime(new Date());
        // 周期起始时间
        Calendar calendar_start = Calendar.getInstance();
        Calendar calendar_end = Calendar.getInstance();
        SimpleDateFormat formatDateRange = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            calendar_start.setTime(formatDateRange.parse(actionSheetFragment.getArguments().getInt("startYear") + "-01-01 00:00"));
            calendar_end.setTime(formatDateRange.parse(actionSheetFragment.getArguments().getInt("endYear") + "-01-01 00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year_start = calendar_start.get(Calendar.YEAR);
        int month_start = calendar_start.get(Calendar.MONTH);
        int month_day = calendar_start.get(Calendar.DATE);

        // 得到年份数据
        for (int i = year_start ; i <= calendar_end.get(Calendar.YEAR) ; i++) {
            years.add("" + i);
        }
        // 得到月份数据
        for (int i = month_start + 1 ; i <= 12 ; i++) {
            months.add(i < 10 ? "0" + i : "" + i);
        }
        // 得到选中月份天数
        Calendar calendar_temp = Calendar.getInstance();
        // 设置为上个月的月历
        calendar_temp.add(Calendar.MONTH, -1);
        calendar_temp.set(Calendar.DATE, calendar_temp.getActualMaximum(Calendar.DATE));
        for (int i = 1; i <= calendar_today.getActualMaximum(Calendar.DATE); i++) {
            calendar_temp.add(Calendar.DATE, 1);
            days.put(i, "" + (i < 10 ? "0" + i : i) + " " + weeks[calendar_temp.get(Calendar.DAY_OF_WEEK) - 1]);
        }

        // 年份滚轮参数设置
        pop_wheel_datarangelayout_year.setListener(index -> {
            months.clear();
            days.clear();
            for (int i = 1; i <= 12; i++) {
                months.add(i < 10 ? "0" + i : "" + i);
            }

            Calendar cl = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                cl.setTime(format.parse(years.get(index) + "-" + months.get(pop_wheel_datarangelayout_month.getSelectedItem()) + "-01"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            pop_wheel_datarangelayout_month.setItems(months);

            // 得到当前月份天数

            int dayCount = cl.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= dayCount; i++) {
                // 第一天不用加了
                if (i != 1) {
                    cl.add(Calendar.DATE, 1);
                }
                days.put(i, "" + (i < 10 ? "0" + i : i) + " " + weeks[cl.get(Calendar.DAY_OF_WEEK) - 1]);
            }

            if (pop_wheel_datarangelayout_day.getSelectedItem() + 1 > dayCount) {
                pop_wheel_datarangelayout_day.setInitPosition(0);
                pop_wheel_datarangelayout_day.setTotalScrollYPosition(dayCount - 1);
            } else {
                pop_wheel_datarangelayout_day.setInitPosition(0);
                pop_wheel_datarangelayout_day.setTotalScrollYPosition(pop_wheel_datarangelayout_day.getSelectedItem());
            }
            ArrayList<String> tempDays = new ArrayList<>();
            for (Object o : days.values().toArray()) {
                tempDays.add(o.toString());
            }
            pop_wheel_datarangelayout_day.setItems(tempDays);
        });
        pop_wheel_datarangelayout_year.setNotLoop();
        pop_wheel_datarangelayout_year.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_year.setItems(years);
        pop_wheel_datarangelayout_year.setTextSize(18);
        for (int i = 0; i < years.size(); i++) {
            if (calendar_today.get(Calendar.YEAR) == Integer.parseInt(years.get(i))) {
                pop_wheel_datarangelayout_year.setInitPosition(i);
                break;
            }
        }

        // 月份滚轮参数设置
        pop_wheel_datarangelayout_month.setNotLoop();
        pop_wheel_datarangelayout_month.setListener(index -> {
            days.clear();
            // 得到选中月份天数
            Calendar cl = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                cl.setTime(format.parse(years.get(pop_wheel_datarangelayout_year.getSelectedItem()) + "-" + months.get(index) + "-01"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int dayCount = cl.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= dayCount; i++) {
                // 第一天不用加了
                if (i != 1) {
                    cl.add(Calendar.DATE, 1);
                }
                days.put(i, "" + (i < 10 ? "0" + i : i) + " " + weeks[cl.get(Calendar.DAY_OF_WEEK) - 1]);
            }

            if (pop_wheel_datarangelayout_day.getSelectedItem() + 1 > dayCount) {
                pop_wheel_datarangelayout_day.setInitPosition(0);
                pop_wheel_datarangelayout_day.setTotalScrollYPosition(dayCount - 1);
            } else {
                pop_wheel_datarangelayout_day.setInitPosition(0);
                pop_wheel_datarangelayout_day.setTotalScrollYPosition(pop_wheel_datarangelayout_day.getSelectedItem());
            }
            ArrayList<String> tempDays = new ArrayList<>();
            for (Object o : days.values().toArray()) {
                tempDays.add(o.toString());
            }
            pop_wheel_datarangelayout_day.setItems(tempDays);
        });
        pop_wheel_datarangelayout_month.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_month.setItems(months);
        pop_wheel_datarangelayout_month.setTextSize(18);
        for (int i = 0; i < months.size(); i++) {
            if (calendar_today.get(Calendar.MONTH) + 1 == Integer.parseInt(months.get(i))) {
                pop_wheel_datarangelayout_month.setInitPosition(i);
                break;
            }
        }

        // 天数滚轮参数设置
        pop_wheel_datarangelayout_day.setNotLoop();
        pop_wheel_datarangelayout_day.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        ArrayList<String> tempDays = new ArrayList<>();
        for (Object o : days.values().toArray()) {
            tempDays.add(o.toString());
        }
        pop_wheel_datarangelayout_day.setItems(tempDays);
        pop_wheel_datarangelayout_day.setTextSize(18);
        Iterator<Map.Entry<Integer, String>> iterator = days.entrySet().iterator();
        int tempCount = 1;
        while (iterator.hasNext()) {
            if (calendar_today.get(Calendar.DAY_OF_MONTH) == tempCount) {
                pop_wheel_datarangelayout_day.setInitPosition(tempCount - 1);
                break;
            }
            tempCount++;
        }

        ArrayList<String> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i < 10 ? "0" + i : "" + i);
        }
        ArrayList<String> minutes = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            minutes.add(i < 10 ? "0" + i : "" + i);
        }

        pop_wheel_datarangelayout_hour.setNotLoop();
        pop_wheel_datarangelayout_hour.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_hour.setItems(hours);
        pop_wheel_datarangelayout_hour.setTextSize(18);
        pop_wheel_datarangelayout_minute.setNotLoop();
        pop_wheel_datarangelayout_minute.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_minute.setItems(minutes);
        pop_wheel_datarangelayout_minute.setTextSize(18);

        LinearLayout pop_morechoice = view.findViewById(R.id.pop_morechoice);
        pop_morechoice.setVisibility(View.VISIBLE);
        TextView pop_ok1 = view.findViewById(R.id.pop_ok1);
        pop_ok1.setText(actionSheetFragment.getArguments().getString("okTitle"));
        pop_ok1.setOnClickListener(v -> {
            if (onOKListener != null) {
                onOKListener.onOKClick(years.get(pop_wheel_datarangelayout_year.getSelectedItem()) + "-"
                        + (Integer.parseInt(months.get(pop_wheel_datarangelayout_month.getSelectedItem())) < 10 ? "0" + months.get(pop_wheel_datarangelayout_month.getSelectedItem()) : months.get(pop_wheel_datarangelayout_month.getSelectedItem())) + "-"
                        + (Integer.parseInt(days.get(pop_wheel_datarangelayout_day.getSelectedItem())) < 10 ? "0" + days.get(pop_wheel_datarangelayout_day.getSelectedItem()) : days.get(pop_wheel_datarangelayout_day.getSelectedItem())) + " "
                        + (Integer.parseInt(hours.get(pop_wheel_datarangelayout_hour.getSelectedItem())) < 10 ? "0" + hours.get(pop_wheel_datarangelayout_hour.getSelectedItem()) : hours.get(pop_wheel_datarangelayout_hour.getSelectedItem())) + ":"
                        + minutes.get(pop_wheel_datarangelayout_minute.getSelectedItem()));
            }
            actionSheetFragment.dismiss();
        });
        TextView pop_cancel1 = view.findViewById(R.id.pop_cancel1);
        pop_cancel1.setText(actionSheetFragment.getArguments().getString("cancelTitle"));
        pop_cancel1.setOnClickListener(v -> {
            if (onCancelListener != null) {
                onCancelListener.onCancelClick();
            }
            actionSheetFragment.dismiss();
        });
    }
}
