package com.renyu.commonlibrary.views.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.commonlibrary.views.actionsheet.fragment.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.commonlibrary.views.wheelview.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateRangeUtils {
    private static String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    // 周期起始时间
    private Calendar calendar_start;
    private Calendar calendar_end;
    private int year_start;
    private int month_start;
    private int day_start;
    private int year_end;
    private int month_end;
    private int day_end;
    // 当前时间
    private Calendar calendar_today;

    // 是否需要显示时分
    private boolean isNeedHM;

    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> months = new ArrayList<>();
    private LinkedHashMap<Integer, String> days = new LinkedHashMap<>();


    public DateRangeUtils(long startTime, long endTime, boolean isNeedHM) {
        calendar_today = Calendar.getInstance();
        calendar_today.setTime(new Date());

        calendar_start = Calendar.getInstance();
        calendar_end = Calendar.getInstance();
        Date dateStart = new Date();
        dateStart.setTime(startTime);
        calendar_start.setTime(dateStart);
        Date dateEnd = new Date();
        dateEnd.setTime(endTime);
        calendar_end.setTime(dateEnd);
        year_start = calendar_start.get(Calendar.YEAR);
        month_start = calendar_start.get(Calendar.MONTH);
        day_start = calendar_start.get(Calendar.DATE);
        year_end = calendar_end.get(Calendar.YEAR);
        month_end = calendar_end.get(Calendar.MONTH);
        day_end = calendar_end.get(Calendar.DATE);

        this.isNeedHM = isNeedHM;
    }

    public void showDateRange(ActionSheetFragment actionSheetFragment, View view,
                              ActionSheetFragment.OnOKListener onOKListener,
                              ActionSheetFragment.OnCancelListener onCancelListener) {
        LinearLayout pop_wheel_datarangelayout = view.findViewById(R.id.pop_wheel_datarangelayout);
        pop_wheel_datarangelayout.setVisibility(View.VISIBLE);
        LoopView pop_wheel_datarangelayout_year = view.findViewById(R.id.pop_wheel_datarangelayout_year);
        LoopView pop_wheel_datarangelayout_month = view.findViewById(R.id.pop_wheel_datarangelayout_month);
        LoopView pop_wheel_datarangelayout_day = view.findViewById(R.id.pop_wheel_datarangelayout_day);
        LoopView pop_wheel_datarangelayout_hour = view.findViewById(R.id.pop_wheel_datarangelayout_hour);
        LoopView pop_wheel_datarangelayout_minute = view.findViewById(R.id.pop_wheel_datarangelayout_minute);
        if (!isNeedHM) {
            pop_wheel_datarangelayout_hour.setVisibility(View.GONE);
            pop_wheel_datarangelayout_minute.setVisibility(View.GONE);
        }

        // 初始化数据
        // 得到年份数据
        for (int i = year_start; i <= calendar_end.get(Calendar.YEAR); i++) {
            years.add("" + i);
        }
        // 分别处理时间点前后的情况
        boolean special = isBeforeStart() || isAfterEnd();
        // 通过当前月份判断是否为开始时间与结束时间年份
        int begin = special ? (year_start == calendar_start.get(Calendar.YEAR) ? (month_start + 1) : 1) : (year_start == calendar_today.get(Calendar.YEAR) ? (month_start + 1) : 1);
        int end = special ? (year_start == calendar_end.get(Calendar.YEAR) ? (month_end + 1) : 12) : (year_end == calendar_today.get(Calendar.YEAR) ? (month_end + 1) : 12);
        // 得到月份数据
        for (int i = begin; i <= end; i++) {
            months.add(i < 10 ? "0" + i : "" + i);
        }
        // 得到选中月份天数据
        Calendar cl = Calendar.getInstance();
        cl.setTime(special ? calendar_start.getTime() : calendar_today.getTime());
        cl.add(Calendar.MONTH, 0);
        if (special) {
            cl.set(Calendar.DAY_OF_MONTH, day_start);
            int dayCount = isSameEnd(cl) ? day_end : cl.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= dayCount - day_start + 1; i++) {
                if (i != 1) {
                    cl.add(Calendar.DATE, 1);
                }
                int value = day_start + (i - 1);
                days.put(i, "" + (value < 10 ? "0" + value : value) + " " + weeks[cl.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        } else {
            cl.set(Calendar.DAY_OF_MONTH, 1);
            int dayCount = isSameEnd(cl) ? day_end : cl.getActualMaximum(Calendar.DATE);
            for (int i = 1; i <= dayCount; i++) {
                if (i != 1) {
                    cl.add(Calendar.DATE, 1);
                }
                days.put(i, "" + (i < 10 ? "0" + i : i) + " " + weeks[cl.get(Calendar.DAY_OF_WEEK) - 1]);
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
                    // 通过当前月份判断是否为开始时间与结束时间年份
                    int beginTmp = special ? (year_start == calendar_start.get(Calendar.YEAR) ? (month_start + 1) : 1) : (year_start == calendar_today.get(Calendar.YEAR) ? (month_start + 1) : 1);
                    int endTmp = special ? (year_start == calendar_end.get(Calendar.YEAR) ? (month_end + 1) : 12) : (year_end == calendar_today.get(Calendar.YEAR) ? (month_end + 1) : 12);
                    for (int i = beginTmp; i <= endTmp; i++) {
                        months.add(i < 10 ? "0" + i : "" + i);
                        if (lastSelectedMonth == i) {
                            currentSelectedMonth = i;
                        }
                    }
                    if (currentSelectedMonth == -1) {
                        currentSelectedMonth = month_start + 1;
                    }
                } else if (Integer.parseInt(years.get(index)) == year_end) {
                    for (int i = 1; i <= (month_end + 1); i++) {
                        months.add(i < 10 ? "0" + i : "" + i);
                        if (lastSelectedMonth == i) {
                            currentSelectedMonth = i;
                        }
                    }
                    if (currentSelectedMonth == -1) {
                        currentSelectedMonth = month_end + 1;
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
                operDay(pop_wheel_datarangelayout_day, index, currentSelectedIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pop_wheel_datarangelayout_year.setNotLoop();
        pop_wheel_datarangelayout_year.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_year.setItems(years);
        pop_wheel_datarangelayout_year.setTextSize(18);
        // 比较起始时间与当前时间
        if (whereIsTodayPosition() == TodayPosition.AfterEndTime ||
                whereIsTodayPosition() == TodayPosition.BeforeStartTime) {
            pop_wheel_datarangelayout_year.setInitPosition(0);
        } else {
            for (int i = 0; i < years.size(); i++) {
                if (calendar_today.get(Calendar.YEAR) == Integer.parseInt(years.get(i))) {
                    pop_wheel_datarangelayout_year.setInitPosition(i);
                    break;
                }
            }
        }

        // 月份滚轮参数设置
        pop_wheel_datarangelayout_month.setNotLoop();
        pop_wheel_datarangelayout_month.setListener(index -> operDay(pop_wheel_datarangelayout_day, pop_wheel_datarangelayout_year.getSelectedItem(), index));
        pop_wheel_datarangelayout_month.setViewPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15), SizeUtils.dp2px(15));
        pop_wheel_datarangelayout_month.setItems(months);
        pop_wheel_datarangelayout_month.setTextSize(18);
        // 比较起始时间与当前时间
        if (whereIsTodayPosition() == TodayPosition.AfterEndTime ||
                whereIsTodayPosition() == TodayPosition.BeforeStartTime) {
            pop_wheel_datarangelayout_month.setInitPosition(0);
        } else {
            for (int i = 0; i < months.size(); i++) {
                if (calendar_today.get(Calendar.MONTH) + 1 == Integer.parseInt(months.get(i))) {
                    pop_wheel_datarangelayout_month.setInitPosition(i);
                    break;
                }
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
        if (whereIsTodayPosition() == TodayPosition.AfterEndTime ||
                whereIsTodayPosition() == TodayPosition.BeforeStartTime) {
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
                        + months.get(pop_wheel_datarangelayout_month.getSelectedItem()) + "-"
                        + day.split("周")[0].trim() + " "
                        + hours.get(pop_wheel_datarangelayout_hour.getSelectedItem()) + ":"
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

    // 当前时间所在位置
    enum TodayPosition {
        AfterEndTime,
        Inside,
        BeforeStartTime
    }

    private TodayPosition whereIsTodayPosition() {
        if (isBeforeStart()) {
            return TodayPosition.BeforeStartTime;
        } else if (isAfterEnd()) {
            return TodayPosition.AfterEndTime;
        } else {
            return TodayPosition.Inside;
        }
    }

    /**
     * 当前时间是否在开始时间之前
     *
     * @return
     */
    private boolean isBeforeStart() {
        return (year_start > calendar_today.get(Calendar.YEAR) ||
                (year_start == calendar_today.get(Calendar.YEAR) && month_start > calendar_today.get(Calendar.MONTH)) ||
                (year_start == calendar_today.get(Calendar.YEAR) && month_start == calendar_today.get(Calendar.MONTH) && day_start >= calendar_today.get(Calendar.DATE)));
    }

    /**
     * 当前时间是否在结束时间之后
     *
     * @return
     */
    private boolean isAfterEnd() {
        return (year_end < calendar_today.get(Calendar.YEAR) ||
                (year_end == calendar_today.get(Calendar.YEAR) && month_end < calendar_today.get(Calendar.MONTH)) ||
                (year_end == calendar_today.get(Calendar.YEAR) && month_end == calendar_today.get(Calendar.MONTH) && day_end < calendar_today.get(Calendar.DATE)));
    }

    /**
     * 当前选择的时间跟起始时间是否在同一个月
     *
     * @param cl
     * @return
     */
    private boolean isSameStart(Calendar cl) {
        return year_start == cl.get(Calendar.YEAR) && month_start == cl.get(Calendar.MONTH);
    }

    /**
     * 当前选择的时间跟结束时间是否在同一个月
     *
     * @param cl
     * @return
     */
    private boolean isSameEnd(Calendar cl) {
        return year_end == cl.get(Calendar.YEAR) && month_end == cl.get(Calendar.MONTH);
    }

    /**
     * 对天的操作
     *
     * @param pop_wheel_datarangelayout_day
     * @param yearIndex
     * @param monthIndex
     */
    private void operDay(LoopView pop_wheel_datarangelayout_day, int yearIndex, int monthIndex) {
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
        int dayCount = isSameEnd(cl) ? day_end : cl.getActualMaximum(Calendar.DATE);
        // 如果当前月为起始月，则将Calendar移动到相应位置
        boolean isSameStart = isSameStart(cl);
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
            // 如果之前选择的月份的日期数值比当前选择的月份的日期大，选择当前可用日期数组的最后一个值
            pop_wheel_datarangelayout_day.setInitPosition(0);
            pop_wheel_datarangelayout_day.setTotalScrollYPosition(days.size() - 1);
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
