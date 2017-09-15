package com.renyu.commonlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;

/**
 * Created by renyu on 16/2/21.
 */
public class LineIndicatorView extends LinearLayout {

    int choiceColor;
    int noneChoiceColor;

    Context context;

    ArrayList<View> views;

    public LineIndicatorView(Context context) {
        this(context, null);
    }

    public LineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        this.context=context;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        views=new ArrayList<>();
    }

    public void setColor(int choiceColor, int noneChoiceColor) {
        this.choiceColor=choiceColor;
        this.noneChoiceColor=noneChoiceColor;
    }

    public void setIndicatorNums(int nums) {
        removeAllViews();
        views.clear();

        for (int i = 0; i < nums; i++) {
            View view=new View(context);
            view.setBackgroundColor(noneChoiceColor);
            LayoutParams params=new LayoutParams(SizeUtils.dp2px(15), SizeUtils.dp2px(2));
            params.setMargins(SizeUtils.dp2px(5), 0, SizeUtils.dp2px(5), 0);
            addView(view, params);
            views.add(view);
        }
    }

    public void setCurrentPosition(int currentNum) {
        for (View view : views) {
            view.setBackgroundColor(noneChoiceColor);
        }
        if (views.size()>0) {
            views.get(currentNum).setBackgroundColor(choiceColor);
        }
    }
}
