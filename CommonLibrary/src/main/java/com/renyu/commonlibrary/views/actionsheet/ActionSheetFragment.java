package com.renyu.commonlibrary.views.actionsheet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.commonutils.BarUtils;
import com.renyu.commonlibrary.views.wheelview.LoopView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Clevo on 2016/6/6.
 */
public class ActionSheetFragment extends Fragment {

    //是否已经关闭
    boolean isDismiss=true;

    View decorView;
    //添加进入的view
    View realView;
    //添加进入的第一个view
    View pop_child_layout;
    //待添加的view
    View customerView;

    FragmentManager fragmentManager;

    //提供类型
    public enum CHOICE {
        ITEM, BEFOREDATE, AFTERDATE, TIME, GRID, CUSTOMER
    }

    //是否自动关闭
    boolean canDismiss=true;

    OnItemClickListener onItemClickListener;
    OnCancelListener onCancelListener;
    OnOKListener onOKListener;

    public interface OnCancelListener {
        void onCancelClick();
    }

    public interface OnOKListener {
        void onOKClick(Object value);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void setOnOKListener(OnOKListener onOKListener) {
        this.onOKListener = onOKListener;
    }

    public void setCustomerView(View customerView) {
        this.customerView=customerView;
    }

    public void setCanDismiss(boolean canDismiss) {
        this.canDismiss = canDismiss;
    }

    public static ActionSheetFragment newItemInstance(String title, String[] items) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putStringArray("items", items);
        bundle.putInt("type", 1);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ActionSheetFragment newGridInstance(String title, String[] items, int[] images) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putStringArray("items", items);
        bundle.putIntArray("images", images);
        bundle.putInt("type", 2);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ActionSheetFragment newBeforeDateInstance(String title, String okTitle, String cancelTitle) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putString("okTitle", okTitle);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("type", 3);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ActionSheetFragment newAfterDateInstance(String title, String okTitle, String cancelTitle) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putString("okTitle", okTitle);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("type", 5);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ActionSheetFragment newTimeInstance(String title, String okTitle, String cancelTitle) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putString("okTitle", okTitle);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("type", 4);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ActionSheetFragment newCustomerInstance(String title, String okTitle, String cancelTitle) {
        ActionSheetFragment fragment=new ActionSheetFragment();
        Bundle bundle=new Bundle();
        bundle.putString("title", title);
        bundle.putString("okTitle", okTitle);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("type", 7);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        InputMethodManager manager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            View focusView=getActivity().getCurrentFocus();
            if (focusView!=null) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        realView=inflater.inflate(R.layout.view_actionsheet, container, false);
        initViews(realView);
        decorView=getActivity().getWindow().getDecorView();
        ((ViewGroup) decorView).addView(realView);
        startPlay();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initViews(View view) {
        pop_child_layout=view.findViewById(R.id.pop_child_layout);
        pop_child_layout.setVisibility(View.INVISIBLE);
        pop_child_layout.setOnTouchListener((v, event) -> true);
        realView.setOnClickListener(v -> dismiss());
        String title=getArguments().getString("title");
        TextView pop_title= (TextView) view.findViewById(R.id.pop_title);
        pop_title.setText(title);
        if (TextUtils.isEmpty(title)) {
            pop_title.setVisibility(View.GONE);
        }
        if (getArguments().getInt("type")==1) {
            ListView pop_listview= (ListView) view.findViewById(R.id.pop_listview);
            pop_listview.setVisibility(View.VISIBLE);
            ActionSheetAdapter adapter=new ActionSheetAdapter(getActivity(), getArguments().getStringArray("items"));
            pop_listview.setAdapter(adapter);
            pop_listview.setOnItemClickListener((parent, view1, position, id) -> {
                if (onItemClickListener!=null) {
                    onItemClickListener.onItemClick(position);
                    dismiss();
                }
            });
            LinearLayout pop_morechoice= (LinearLayout) view.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.VISIBLE);
        }
        else if (getArguments().getInt("type")==2) {
            GridLayout pop_grid= (GridLayout) view.findViewById(R.id.pop_grid);
            pop_grid.setVisibility(View.VISIBLE);
            int width=(ScreenUtils.getScreenWidth()-SizeUtils.dp2px(20))/(getArguments().getStringArray("items").length<4?getArguments().getStringArray("items").length:4);
            for (int i=0;i<getArguments().getStringArray("items").length;i++) {
                final int i_=i;
                View viewChild=LayoutInflater.from(getActivity()).inflate(R.layout.adapter_share, null, false);
                LinearLayout adapter_share_layout= (LinearLayout) viewChild.findViewById(R.id.adapter_share_layout);
                RxView.clicks(adapter_share_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                    if (onItemClickListener!=null) {
                        onItemClickListener.onItemClick(i_);
                    }
                    dismiss();
                });
                ImageView adapter_share_image= (ImageView) viewChild.findViewById(R.id.adapter_share_image);
                TextView adapter_share_text= (TextView) viewChild.findViewById(R.id.adapter_share_text);
                adapter_share_image.setImageResource(getArguments().getIntArray("images")[i]);
                adapter_share_text.setText(getArguments().getStringArray("items")[i]);
                GridLayout.LayoutParams params=new GridLayout.LayoutParams();
                params.width=width;
                params.height=SizeUtils.dp2px(120);
                params.columnSpec = GridLayout.spec(i%4);
                params.rowSpec= GridLayout.spec(i/4);
                pop_grid.addView(viewChild, params);
            }
        }
        else if (getArguments().getInt("type")==3) {
            final ArrayList<String> years=new ArrayList<>();
            Calendar calendar_today=Calendar.getInstance();
            calendar_today.setTime(new Date());
            for (int i=1950;i<=calendar_today.get(Calendar.YEAR);i++) {
                years.add(""+i);
            }
            final ArrayList<String> months=new ArrayList<>();
            for (int i=1;i<=(calendar_today.get(Calendar.MONTH)+1);i++) {
                months.add(""+i);
            }
            final ArrayList<String> days=new ArrayList<>();
            for (int i=1;i<=calendar_today.get(Calendar.DAY_OF_MONTH);i++) {
                days.add(""+i);
            }

            LinearLayout pop_wheel_yearlayout= (LinearLayout) view.findViewById(R.id.pop_wheel_yearlayout);
            pop_wheel_yearlayout.setVisibility(View.VISIBLE);

            LoopView pop_wheel_yearlayout_year= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_year);
            LoopView pop_wheel_yearlayout_month= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_month);
            LoopView pop_wheel_yearlayout_day= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_day);

            pop_wheel_yearlayout_year.setListener(index -> {
                months.clear();
                days.clear();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                if (years.get(index).equals(""+calendar.get(Calendar.YEAR))) {
                    for (int i=1;i<=(calendar.get(Calendar.MONTH)+1);i++) {
                        months.add(""+i);
                    }
                    if (months.size()<pop_wheel_yearlayout_month.getSelectedItem()) {
                        pop_wheel_yearlayout_month.setInitPosition(0);
                        pop_wheel_yearlayout_month.setTotalScrollYPosition(months.size()-1);
                    }
                    else {
                        pop_wheel_yearlayout_month.setInitPosition(0);
                        pop_wheel_yearlayout_month.setTotalScrollYPosition(pop_wheel_yearlayout_month.getSelectedItem());
                    }
                    pop_wheel_yearlayout_month.setItems(months);

                }
                else {
                    for (int i=1;i<=12;i++) {
                        months.add(""+i);
                    }
                    pop_wheel_yearlayout_month.setItems(months);

                }

                //当前月份最大天数
                int dayCount=0;
                if (years.get(index).equals(""+calendar.get(Calendar.YEAR)) && months.size()<=1+pop_wheel_yearlayout_month.getSelectedItem()) {
                    for (int i=1;i<=calendar.get(Calendar.DAY_OF_MONTH);i++) {
                        days.add(""+i);
                    }
                    dayCount=calendar.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    Calendar cl=Calendar.getInstance();
                    cl.set(Calendar.YEAR, Integer.parseInt(years.get(index)));
                    cl.set(Calendar.MONTH, Integer.parseInt(months.get(pop_wheel_yearlayout_month.getSelectedItem()))-1);
                    dayCount=cl.getActualMaximum(Calendar.DATE);
                    for (int i=1;i<=dayCount;i++) {
                        days.add(""+i);
                    }
                }
                if (pop_wheel_yearlayout_day.getSelectedItem()+1>dayCount) {
                    pop_wheel_yearlayout_day.setInitPosition(0);
                    pop_wheel_yearlayout_day.setTotalScrollYPosition(dayCount-1);
                }
                else {
                    pop_wheel_yearlayout_day.setInitPosition(0);
                    pop_wheel_yearlayout_day.setTotalScrollYPosition(pop_wheel_yearlayout_day.getSelectedItem());
                }
                pop_wheel_yearlayout_day.setItems(days);
            });
            pop_wheel_yearlayout_year.setNotLoop();
            pop_wheel_yearlayout_year.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_year.setItems(years);
            pop_wheel_yearlayout_year.setTextSize(18);
            pop_wheel_yearlayout_year.setInitPosition(years.size()-1);

            pop_wheel_yearlayout_month.setNotLoop();
            pop_wheel_yearlayout_month.setListener(index -> {
                days.clear();
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                //当前月份最大天数
                int dayCount=0;
                if (index==months.size()-1 && years.get(pop_wheel_yearlayout_year.getSelectedItem()).equals(""+calendar.get(Calendar.YEAR))) {
                    for (int i=1;i<=calendar.get(Calendar.DAY_OF_MONTH);i++) {
                        days.add(""+i);
                    }
                    dayCount=calendar.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    Calendar cl=Calendar.getInstance();
                    cl.set(Calendar.YEAR, Integer.parseInt(years.get(pop_wheel_yearlayout_year.getSelectedItem())));
                    cl.set(Calendar.MONTH, Integer.parseInt(months.get(index))-1);
                    dayCount=cl.getActualMaximum(Calendar.DATE);
                    for (int i=1;i<=dayCount;i++) {
                        days.add(""+i);
                    }
                }
                if (pop_wheel_yearlayout_day.getSelectedItem()+1>dayCount) {
                    pop_wheel_yearlayout_day.setInitPosition(0);
                    pop_wheel_yearlayout_day.setTotalScrollYPosition(dayCount-1);
                }
                else {
                    pop_wheel_yearlayout_day.setInitPosition(0);
                    pop_wheel_yearlayout_day.setTotalScrollYPosition(pop_wheel_yearlayout_day.getSelectedItem());
                }
                pop_wheel_yearlayout_day.setItems(days);
            });
            pop_wheel_yearlayout_month.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_month.setItems(months);
            pop_wheel_yearlayout_month.setTextSize(18);
            pop_wheel_yearlayout_month.setInitPosition(months.size()-1);

            pop_wheel_yearlayout_day.setNotLoop();
            pop_wheel_yearlayout_day.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_day.setItems(days);
            pop_wheel_yearlayout_day.setTextSize(18);
            pop_wheel_yearlayout_day.setInitPosition(days.size()-1);

            LinearLayout pop_morechoice= (LinearLayout) view.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.VISIBLE);
            TextView pop_ok1= (TextView) view.findViewById(R.id.pop_ok1);
            pop_ok1.setText(getArguments().getString("okTitle"));
            pop_ok1.setOnClickListener(v -> {
                if (onOKListener!=null) {
                    onOKListener.onOKClick(years.get(pop_wheel_yearlayout_year.getSelectedItem())+"-"
                            +months.get(pop_wheel_yearlayout_month.getSelectedItem())+"-"
                            +days.get(pop_wheel_yearlayout_day.getSelectedItem()));
                }
                dismiss();
            });
            TextView pop_cancel1= (TextView) view.findViewById(R.id.pop_cancel1);
            pop_cancel1.setText(getArguments().getString("cancelTitle"));
            pop_cancel1.setOnClickListener(v -> {
                if (onCancelListener!=null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
        }
        else if (getArguments().getInt("type")==5) {
            final ArrayList<String> years=new ArrayList<>();
            Calendar calendar_today=Calendar.getInstance();
            calendar_today.setTime(new Date());
            int currentYear=calendar_today.get(Calendar.YEAR);
            for (int i=currentYear; i<currentYear+5; i++) {
                years.add(""+i);
            }
            final ArrayList<String> months=new ArrayList<>();
            for (int i=(calendar_today.get(Calendar.MONTH)+1); i<=12; i++) {
                months.add(""+i);
            }
            final ArrayList<String> days=new ArrayList<>();
            for (int i=calendar_today.get(Calendar.DAY_OF_MONTH); i<=calendar_today.getActualMaximum(Calendar.DATE); i++) {
                days.add(""+i);
            }

            LinearLayout pop_wheel_yearlayout= (LinearLayout) view.findViewById(R.id.pop_wheel_yearlayout);
            pop_wheel_yearlayout.setVisibility(View.VISIBLE);

            LoopView pop_wheel_yearlayout_year= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_year);
            LoopView pop_wheel_yearlayout_month= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_month);
            LoopView pop_wheel_yearlayout_day= (LoopView) view.findViewById(R.id.pop_wheel_yearlayout_day);

            pop_wheel_yearlayout_year.setListener(index -> {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                // month起始位置
                int initMonthPosition=0;
                if (years.get(index).equals(""+calendar.get(Calendar.YEAR))) {
                    // 当月不是从1月份开始，年份切换导致month大小变化
                    initMonthPosition=pop_wheel_yearlayout_month.getSelectedItem()-calendar.get(Calendar.MONTH);
                    // 年份切换不足部分补充
                    if (initMonthPosition<0) {
                        initMonthPosition=0;
                    }
                    months.clear();
                    for (int i=(calendar.get(Calendar.MONTH)+1); i<=12; i++) {
                        months.add(""+i);
                    }
                }
                else {
                    // 年份切换导致month大小变化
                    if (months.size()!=12) {
                        initMonthPosition=Integer.parseInt(months.get(pop_wheel_yearlayout_month.getSelectedItem()))-1;
                    }
                    else {
                        initMonthPosition=pop_wheel_yearlayout_month.getSelectedItem();
                    }
                    months.clear();
                    for (int i=1;i<=12;i++) {
                        months.add(""+i);
                    }
                }
                pop_wheel_yearlayout_month.setInitPosition(0);
                pop_wheel_yearlayout_month.setTotalScrollYPosition(initMonthPosition);
                pop_wheel_yearlayout_month.setItems(months);

                // day起始日期
                int initDay=Integer.parseInt(days.get(pop_wheel_yearlayout_day.getSelectedItem()));
                // day初始化位置
                int initDayPosition=0;
                days.clear();
                if (years.get(index).equals(""+calendar.get(Calendar.YEAR)) &&
                        (""+(calendar.get(Calendar.MONTH)+1)).equals(months.get(initMonthPosition))) {
                    for (int i=calendar.get(Calendar.DAY_OF_MONTH); i<=calendar.getActualMaximum(Calendar.DATE); i++) {
                        days.add(""+i);
                    }
                    initDayPosition=initDay-calendar.get(Calendar.DAY_OF_MONTH);
                    // 月份切换不足部分补充
                    if (initDayPosition<0) {
                        initDayPosition=0;
                    }
                }
                else {
                    Calendar cl=Calendar.getInstance();
                    cl.set(Calendar.YEAR, Integer.parseInt(years.get(index)));
                    cl.set(Calendar.MONTH, Integer.parseInt(months.get(pop_wheel_yearlayout_month.getSelectedItem()))-1);
                    for (int i=1;i<=cl.getActualMaximum(Calendar.DATE);i++) {
                        days.add(""+i);
                    }
                    boolean isFind=false;
                    for (int i = 0; i < days.size(); i++) {
                        if (days.get(i).equals(initDay+"")) {
                            initDayPosition=i;
                            isFind=true;
                            break;
                        }
                    }
                    if (!isFind) {
                        initDayPosition=days.size()-1;
                    }
                }
                pop_wheel_yearlayout_day.setInitPosition(0);
                pop_wheel_yearlayout_day.setTotalScrollYPosition(initDayPosition);
                pop_wheel_yearlayout_day.setItems(days);
            });
            pop_wheel_yearlayout_year.setNotLoop();
            pop_wheel_yearlayout_year.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_year.setItems(years);
            pop_wheel_yearlayout_year.setTextSize(18);
            pop_wheel_yearlayout_year.setInitPosition(0);

            pop_wheel_yearlayout_month.setNotLoop();
            pop_wheel_yearlayout_month.setListener(index -> {
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(new Date());
                // day起始日期
                int initDay=Integer.parseInt(days.get(pop_wheel_yearlayout_day.getSelectedItem()));
                // day初始化位置
                int initDayPosition=0;
                days.clear();
                if (years.get(pop_wheel_yearlayout_year.getSelectedItem()).equals(""+calendar.get(Calendar.YEAR)) &&
                        (""+(calendar.get(Calendar.MONTH)+1)).equals(months.get(index))) {
                    for (int i=calendar.get(Calendar.DAY_OF_MONTH); i<=calendar.getActualMaximum(Calendar.DATE); i++) {
                        days.add(""+i);
                    }
                    initDayPosition=initDay-calendar.get(Calendar.DAY_OF_MONTH);
                    // 月份切换不足部分补充
                    if (initDayPosition<0) {
                        initDayPosition=0;
                    }
                }
                else {
                    Calendar cl=Calendar.getInstance();
                    cl.set(Calendar.YEAR, Integer.parseInt(years.get(pop_wheel_yearlayout_year.getSelectedItem())));
                    cl.set(Calendar.MONTH, Integer.parseInt(months.get(index))-1);
                    for (int i=1;i<=cl.getActualMaximum(Calendar.DATE);i++) {
                        days.add(""+i);
                    }
                    boolean isFind=false;
                    for (int i = 0; i < days.size(); i++) {
                        if (days.get(i).equals(initDay+"")) {
                            initDayPosition=i;
                            isFind=true;
                            break;
                        }
                    }
                    if (!isFind) {
                        initDayPosition=days.size()-1;
                    }
                }
                pop_wheel_yearlayout_day.setInitPosition(0);
                pop_wheel_yearlayout_day.setTotalScrollYPosition(initDayPosition);
                pop_wheel_yearlayout_day.setItems(days);

            });
            pop_wheel_yearlayout_month.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_month.setItems(months);
            pop_wheel_yearlayout_month.setTextSize(18);
            pop_wheel_yearlayout_month.setInitPosition(0);

            pop_wheel_yearlayout_day.setNotLoop();
            pop_wheel_yearlayout_day.setViewPadding(SizeUtils.dp2px(30), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_yearlayout_day.setItems(days);
            pop_wheel_yearlayout_day.setTextSize(18);
            pop_wheel_yearlayout_day.setInitPosition(0);

            LinearLayout pop_morechoice= (LinearLayout) view.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.VISIBLE);
            TextView pop_ok1= (TextView) view.findViewById(R.id.pop_ok1);
            pop_ok1.setText(getArguments().getString("okTitle"));
            pop_ok1.setOnClickListener(v -> {
                if (onOKListener!=null) {
                    onOKListener.onOKClick(years.get(pop_wheel_yearlayout_year.getSelectedItem())+"-"
                            +months.get(pop_wheel_yearlayout_month.getSelectedItem())+"-"
                            +days.get(pop_wheel_yearlayout_day.getSelectedItem()));
                }
                if (canDismiss) {
                    dismiss();
                }
            });
            TextView pop_cancel1= (TextView) view.findViewById(R.id.pop_cancel1);
            pop_cancel1.setText(getArguments().getString("cancelTitle"));
            pop_cancel1.setOnClickListener(v -> {
                if (onCancelListener!=null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
        }
        else if (getArguments().getInt("type")==4) {
            ArrayList<String> hours=new ArrayList<>();
            for (int i=0;i<24;i++) {
                hours.add(""+i);
            }
            ArrayList<String> minutes=new ArrayList<>();
            for (int i=0;i<60;i++) {
                minutes.add(i<10?"0"+i:""+i);
            }

            LinearLayout pop_wheel_timelayout= (LinearLayout) view.findViewById(R.id.pop_wheel_timelayout);
            LoopView pop_wheel_timelayout_hour= (LoopView) view.findViewById(R.id.pop_wheel_timelayout_hour);
            LoopView pop_wheel_timelayout_minute= (LoopView) view.findViewById(R.id.pop_wheel_timelayout_minute);
            pop_wheel_timelayout.setVisibility(View.VISIBLE);
            pop_wheel_timelayout_hour.setNotLoop();
            pop_wheel_timelayout_hour.setViewPadding(SizeUtils.dp2px(60), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_timelayout_hour.setItems(hours);
            pop_wheel_timelayout_hour.setTextSize(18);
            pop_wheel_timelayout_minute.setNotLoop();
            pop_wheel_timelayout_minute.setViewPadding(SizeUtils.dp2px(60), SizeUtils.dp2px(15), SizeUtils.dp2px(30), SizeUtils.dp2px(15));
            pop_wheel_timelayout_minute.setItems(minutes);
            pop_wheel_timelayout_minute.setTextSize(18);

            LinearLayout pop_morechoice= (LinearLayout) view.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.VISIBLE);
            TextView pop_ok1= (TextView) view.findViewById(R.id.pop_ok1);
            pop_ok1.setText(getArguments().getString("okTitle"));
            pop_ok1.setOnClickListener(v -> {
                if (onOKListener!=null) {
                    onOKListener.onOKClick(hours.get(pop_wheel_timelayout_hour.getSelectedItem())+":"+minutes.get(pop_wheel_timelayout_minute.getSelectedItem()));
                }
                dismiss();
            });
            TextView pop_cancel1= (TextView) view.findViewById(R.id.pop_cancel1);
            pop_cancel1.setText(getArguments().getString("cancelTitle"));
            pop_cancel1.setOnClickListener(v -> {
                if (onCancelListener!=null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
        }
        else if (getArguments().getInt("type")==7) {
            LinearLayout pop_morechoice= (LinearLayout) view.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.VISIBLE);
            TextView pop_ok1= (TextView) view.findViewById(R.id.pop_ok1);
            pop_ok1.setText(getArguments().getString("okTitle"));
            pop_ok1.setOnClickListener(v -> {
                if (onOKListener!=null) {
                    onOKListener.onOKClick("");
                }
                if (canDismiss) {
                    dismiss();
                }
            });
            TextView pop_cancel1= (TextView) view.findViewById(R.id.pop_cancel1);
            pop_cancel1.setText(getArguments().getString("cancelTitle"));
            pop_cancel1.setOnClickListener(v -> {
                if (onCancelListener!=null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
            LinearLayout pop_customer_layout= (LinearLayout) view.findViewById(R.id.pop_customer_layout);
            pop_customer_layout.setVisibility(View.VISIBLE);
            if (customerView!=null) {
                pop_customer_layout.addView(customerView);
            }
        }
    }

    private void startPlay() {
        pop_child_layout.post(new Runnable() {
            @Override
            public void run() {
                final int moveHeight=pop_child_layout.getMeasuredHeight();
                ValueAnimator valueAnimator=ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        pop_child_layout.setVisibility(View.VISIBLE);
                    }
                });
                valueAnimator.addUpdateListener(animation -> {
                    ArgbEvaluator argbEvaluator=new ArgbEvaluator();
                    realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#00000000"), Color.parseColor("#70000000")));
                    //当底部存在导航栏并且decorView获取的高度不包含底部状态栏的时候，需要去掉这个高度差
                    if (BarUtils.getNavBarHeight(pop_child_layout.getContext())>0 && decorView.getMeasuredHeight()!=ScreenUtils.getScreenHeight()) {
                        pop_child_layout.setTranslationY((moveHeight+BarUtils.getNavBarHeight(pop_child_layout.getContext()))*(1-animation.getAnimatedFraction())-BarUtils.getNavBarHeight(pop_child_layout.getContext()));
                    }
                    else {
                        pop_child_layout.setTranslationY(moveHeight*(1-animation.getAnimatedFraction()));
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void stopPlay() {
        pop_child_layout.post(() -> {
            final int moveHeight=pop_child_layout.getMeasuredHeight();
            ValueAnimator valueAnimator=ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(animation -> {
                ArgbEvaluator argbEvaluator=new ArgbEvaluator();
                realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#70000000"), Color.parseColor("#00000000")));
                if (BarUtils.getNavBarHeight(pop_child_layout.getContext())>0 && decorView.getMeasuredHeight()!= ScreenUtils.getScreenHeight()) {
                    pop_child_layout.setTranslationY((moveHeight+BarUtils.getNavBarHeight(pop_child_layout.getContext()))*animation.getAnimatedFraction()-BarUtils.getNavBarHeight(pop_child_layout.getContext()));
                }
                else {
                    pop_child_layout.setTranslationY(moveHeight*animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
        });
    }

    private void show(FragmentManager manager, final String tag) {
        this.fragmentManager=manager;
        if (manager.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss=false;
        new Handler().post(() -> {
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.add(ActionSheetFragment.this, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        });
    }

    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss=true;
        new Handler().post(() -> {
            fragmentManager.popBackStack();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.remove(ActionSheetFragment.this);
            transaction.commitAllowingStateLoss();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            isDismiss=savedInstanceState.getBoolean("isDismiss");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPlay();
        new Handler().postDelayed(() -> ((ViewGroup) decorView).removeView(realView), 500);
    }

    public static Builder build(FragmentManager fragmentManager) {
        Builder builder=new Builder(fragmentManager);
        return builder;
    }

    public static class Builder {

        FragmentManager fragmentManager;

        String tag="ActionSheetFragment";
        String title="";
        String okTitle="";
        String cancelTitle="";
        CHOICE choice;
        String[] items;
        int[] images;
        boolean canDismiss=true;
        OnItemClickListener onItemClickListener;
        OnCancelListener onCancelListener;
        OnOKListener onOKListener;
        View customerView;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager=fragmentManager;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setOkTitle(String okTitle) {
            this.okTitle = okTitle;
            return this;
        }

        public Builder setCancelTitle(String cancelTitle) {
            this.cancelTitle = cancelTitle;
            return this;
        }

        public Builder setListItems(String[] items, OnItemClickListener onItemClickListener) {
            this.items=items;
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setGridItems(String[] items, int[] images, OnItemClickListener onItemClickListener) {
            this.items=items;
            this.images=images;
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setChoice(CHOICE choice) {
            this.choice = choice;
            return this;
        }

        public Builder setCanDismiss(boolean canDismiss) {
            this.canDismiss = canDismiss;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnOKListener(OnOKListener onOKListener) {
            this.onOKListener = onOKListener;
            return this;
        }

        public Builder setCustomerView(View customerView) {
            this.customerView = customerView;
            return this;
        }

        public ActionSheetFragment show() {
            ActionSheetFragment fragment=null;
            if (choice== CHOICE.ITEM) {
                fragment=ActionSheetFragment.newItemInstance(title, items);
                fragment.setOnItemClickListener(onItemClickListener);
                fragment.setOnCancelListener(onCancelListener);
                fragment.show(fragmentManager, tag);
            }
            if (choice== CHOICE.GRID) {
                fragment=ActionSheetFragment.newGridInstance(title, items, images);
                fragment.setOnItemClickListener(onItemClickListener);
                fragment.show(fragmentManager, tag);
            }
            if (choice== CHOICE.BEFOREDATE) {
                fragment=ActionSheetFragment.newBeforeDateInstance(title, okTitle, cancelTitle);
                fragment.setOnOKListener(onOKListener);
                fragment.setOnCancelListener(onCancelListener);
                fragment.show(fragmentManager, tag);
            }
            if (choice== CHOICE.AFTERDATE) {
                fragment=ActionSheetFragment.newAfterDateInstance(title, okTitle, cancelTitle);
                fragment.setOnOKListener(onOKListener);
                fragment.setOnCancelListener(onCancelListener);
                fragment.show(fragmentManager, tag);
            }
            if (choice== CHOICE.TIME) {
                fragment=ActionSheetFragment.newTimeInstance(title, okTitle, cancelTitle);
                fragment.setOnOKListener(onOKListener);
                fragment.setOnCancelListener(onCancelListener);
                fragment.show(fragmentManager, tag);
            }
            if (choice== CHOICE.CUSTOMER) {
                fragment=ActionSheetFragment.newCustomerInstance(title, okTitle, cancelTitle);
                fragment.setOnOKListener(onOKListener);
                fragment.setOnCancelListener(onCancelListener);
                fragment.setCustomerView(customerView);
                fragment.show(fragmentManager, tag);
            }
            fragment.setCanDismiss(canDismiss);
            return fragment;
        }
    }
}
