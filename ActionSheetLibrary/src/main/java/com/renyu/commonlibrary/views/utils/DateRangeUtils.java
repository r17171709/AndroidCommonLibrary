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
            calendar_start.setTime(formatDateRange.parse(actionSheetFragment.getArguments().getInt("startYear") + "-01-10 00:00"));
            calendar_end.setTime(formatDateRange.parse(actionSheetFragment.getArguments().getInt("endYear") + "-01-01 00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int year_start = calendar_start.get(Calendar.YEAR);
        int month_start = calendar_start.get(Calendar.MONTH);
        int day_start = calendar_start.get(Calendar.DATE);

        // 初始化数据
        // 得到年份数据
        for (int i = year_start; i <= calendar_end.get(Calendar.YEAR); i++) {
            years.add("" + i);
        }
        // 得到月份数据
        for (int i = (month_start + 1); i <= 12; i++) {
            months.add(i < 10 ? "0" + i : "" + i);
        }
        // 得到选中月份天数
        boolean isAfterStart = isAfterStart(year_start, month_start, day_start, calendar_today);
        Calendar calendar_temp = Calendar.getInstance();
        calendar_temp.setTime(isAfterStart ? calendar_start.getTime() : calendar_today.getTime());
        calendar_temp.add(Calendar.MONTH, 0);
        if (isAfterStart) {
            calendar_temp.set(Calendar.DAY_OF_MONTH, day_start);
            for (int i = 1; i <= calendar_start.getActualMaximum(Calendar.DATE) - day_start + 1; i++) {
                if (i != 1) {
                    calendar_temp.add(Calendar.DATE, 1);
                }
                int value = day_start + (i - 1);
                days.put(i, "" + (value < 10 ? "0" + value : value) + " " + weeks[calendar_temp.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        } else {
            calendar_temp.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 1; i <= calendar_today.getActualMaximum(Calendar.DATE); i++) {
                if (i != 1) {
                    calendar_temp.add(Calendar.DATE, 1);
                }
                days.put(i, "" + (i < 10 ? "0" + i : i) + " " + weeks[calendar_temp.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        }

        // 年份滚轮参数设置
        pop_wheel_datarangelayout_year.setListener(index -> {
            // 上一次选中的月份
            try {
                int lastSelectedMonth = Integer.parseInt(months.get(pop_wheel_datarangelayout_month.getSelectedItem()));
                // 当前需要展示的月份
                int currentSelectedMonth = -1;
                // 当前选中的月份索引
                int currentSelectedIndex = -1;
                months.clear();
                // 得到当前选中的月份索引
                if (Integer.parseInt(years.get(index)) == year_start) {
                    for (int i = (month_start + 1); i <= 12; i++) {
                        months.add(i < 10 ? "0" + i : "" + i);
                        if (lastSelectedMonth == i) {
                            currentSelectedMonth = i;
                        }
                    }
                    if (currentSelectedMonth == -1) {
                        currentSelectedMonth = month_start + 1;
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        months.add(i < 10 ? "0" + i : "" + i);
                        if (lastSelectedMonth == i) {
                            currentSelectedMonth = i;
                        }
                    }
                }
                for (int i = 0; i < months.size(); i++) {
                    if (Integer.parseInt(months.get(i)) == currentSelectedMonth) {
                        currentSelectedIndex = i;
                    }
                }
                // 调整滚轮位置
                pop_wheel_datarangelayout_month.setInitPosition(0);
                pop_wheel_datarangelayout_month.setTotalScrollYPosition(currentSelectedIndex);
                pop_wheel_datarangelayout_month.setItems(months);
                // 调整天
                operDay(years, months, days, year_start, month_start, day_start, pop_wheel_datarangelayout_day, index, currentSelectedIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        pop_wheel_datarangelayout_month.setListener(index -> operDay(years, months, days, year_start, month_start, day_start, pop_wheel_datarangelayout_day, pop_wheel_datarangelayout_year.getSelectedItem(), index));
        pop_wheel_datarangelayout_month.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_month.setItems(months);
        pop_wheel_datarangelayout_month.setTextSize(18);
        for (int i = 0; i < months.size(); i++) {
            if (calendar_today.get(Calendar.MONTH) + 1 == Integer.parseInt(months.get(i))) {
                pop_wheel_datarangelayout_month.setInitPosition(i);
                break;
            }
        }

        // 天滚轮参数设置
        pop_wheel_datarangelayout_day.setNotLoop();
        pop_wheel_datarangelayout_day.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        ArrayList<String> tempDays = new ArrayList<>();
        for (Object o : days.values().toArray()) {
            tempDays.add(o.toString());
        }
        pop_wheel_datarangelayout_day.setItems(tempDays);
        pop_wheel_datarangelayout_day.setTextSize(18);
        // 比较起始时间与当前时间
        if (isAfterStart && !isSameStart(year_start, month_start, calendar_today)) {
            pop_wheel_datarangelayout_day.setInitPosition(0);
        } else {
            Iterator<Map.Entry<Integer, String>> iterator = days.entrySet().iterator();
            int tempCount = 1;
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                if (calendar_today.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(entry.getValue().split("周")[0].trim())) {
                    pop_wheel_datarangelayout_day.setInitPosition(tempCount - 1);
                    break;
                }
                tempCount++;
            }
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
                String day = "0";
                Iterator<Map.Entry<Integer, String>> iterator = days.entrySet().iterator();
                int tempCount = 0;
                while (iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = iterator.next();
                    if (pop_wheel_datarangelayout_day.getSelectedItem() == tempCount) {
                        day = days.get(entry.getKey());
                        break;
                    }
                    tempCount++;
                }
                onOKListener.onOKClick(years.get(pop_wheel_datarangelayout_year.getSelectedItem()) + "-"
                        + (Integer.parseInt(months.get(pop_wheel_datarangelayout_month.getSelectedItem())) < 10 ? "0" + months.get(pop_wheel_datarangelayout_month.getSelectedItem()) : months.get(pop_wheel_datarangelayout_month.getSelectedItem())) + "-"
                        + day.split("周")[0].trim() + " "
                        + (Integer.parseInt(hours.get(pop_wheel_datarangelayout_hour.getSelectedItem())) < 10 ? hours.get(pop_wheel_datarangelayout_hour.getSelectedItem()) : hours.get(pop_wheel_datarangelayout_hour.getSelectedItem())) + ":"
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

    // 天数显示数量判断依据
    private boolean isAfterStart(int year_start, int month_start, int day_start, Calendar calendar_today) {
        return (year_start > calendar_today.get(Calendar.YEAR) ||
                (year_start == calendar_today.get(Calendar.YEAR) && month_start > calendar_today.get(Calendar.MONTH)) ||
                (year_start == calendar_today.get(Calendar.YEAR) && month_start == calendar_today.get(Calendar.MONTH) && calendar_today.get(Calendar.DATE) >= day_start));
    }

    // 是否起始时间跟当前时间在同一个月
    private boolean isSameStart(int year_start, int month_start, Calendar cl) {
        return year_start == cl.get(Calendar.YEAR) && month_start == cl.get(Calendar.MONTH);
    }

    /**
     * 对天的操作
     * @param years
     * @param months
     * @param days
     * @param year_start
     * @param month_start
     * @param day_start
     * @param pop_wheel_datarangelayout_day
     * @param yearIndex
     * @param monthIndex
     */
    private void operDay(ArrayList<String> years, ArrayList<String> months, LinkedHashMap<Integer, String> days,
                         int year_start, int month_start, int day_start,
                         LoopView pop_wheel_datarangelayout_day,
                         int yearIndex, int monthIndex) {
        // 上一次选中的天
        int lastSelectedDay = -1;
        Iterator<Map.Entry<Integer, String>> iterator = days.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            if (entry.getValue().equals(days.get(pop_wheel_datarangelayout_day.getSelectedItem() + 1))) {
                lastSelectedDay = Integer.parseInt(days.get(pop_wheel_datarangelayout_day.getSelectedItem() + 1).split("周")[0].trim());
            }
        }
        // 当前选中的天索引
        int currentSelectedIndex = -1;
        days.clear();
        // 得到选中月份天数
        Calendar cl = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cl.setTime(format.parse(years.get(yearIndex) + "-" + months.get(monthIndex) + "-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dayCount = cl.getActualMaximum(Calendar.DATE);
        // 如果当前月为起始月，则将Calendar移动到相应位置
        boolean isSameStart = isSameStart(year_start, month_start, cl);
        if (isSameStart) {
            cl.set(Calendar.DAY_OF_MONTH, day_start);
        }
        // 组合天数据
        for (int i = 1; i <= (isSameStart ? (dayCount - day_start + 1) : dayCount); i++) {
            // 第一天不用加了
            if (i != 1) {
                cl.add(Calendar.DATE, 1);
            }
            int value = (isSameStart ? day_start : 1) + (i - 1);
            days.put(i, "" + (value < 10 ? "0" + value : value) + " " + weeks[cl.get(Calendar.DAY_OF_WEEK) - 1]);
        }
        // 调整滚轮位置
        if (lastSelectedDay > dayCount) {
            pop_wheel_datarangelayout_day.setInitPosition(0);
            pop_wheel_datarangelayout_day.setTotalScrollYPosition(dayCount - 1);
        } else {
            int tempIndex = 0;
            iterator = days.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                if (Integer.parseInt(entry.getValue().split("周")[0].trim()) == lastSelectedDay) {
                    currentSelectedIndex = tempIndex;
                }
                tempIndex++;
            }
            pop_wheel_datarangelayout_day.setInitPosition(0);
            pop_wheel_datarangelayout_day.setTotalScrollYPosition(currentSelectedIndex);
        }
        ArrayList<String> tempDays = new ArrayList<>();
        for (Object o : days.values().toArray()) {
            tempDays.add(o.toString());
        }
        pop_wheel_datarangelayout_day.setItems(tempDays);
    }
}
