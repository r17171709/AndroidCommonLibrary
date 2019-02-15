package com.renyu.commonlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.commonlibrary.R;

import java.util.ArrayList;

/**
 * Created by renyu on 16/2/21.
 */
public class LineIndicatorView extends LinearLayout {
    int choiceColor;
    int noneChoiceColor;
    int height;
    int width;
    int margin;
    // 形状
    TYPE type;
    public enum TYPE {
        CIRCLE, LINE
    }

    Context context;

    ArrayList<View> views;

    public LineIndicatorView(Context context) {
        this(context, null);
    }

    public LineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineIndicatorViewAttr);
        height = array.getDimensionPixelOffset(R.styleable.LineIndicatorViewAttr_height, SizeUtils.dp2px(3));
        width = array.getDimensionPixelOffset(R.styleable.LineIndicatorViewAttr_width, SizeUtils.dp2px(3));
        int temp = array.getInteger(R.styleable.LineIndicatorViewAttr_type, 1);
        if (temp == 1) {
            type = TYPE.CIRCLE;
        } else {
            type = TYPE.LINE;
        }
        choiceColor = array.getResourceId(R.styleable.LineIndicatorViewAttr_choiceColor, Color.BLACK);
        noneChoiceColor = array.getResourceId(R.styleable.LineIndicatorViewAttr_noneChoiceColor, Color.WHITE);
        margin = array.getDimensionPixelOffset(R.styleable.LineIndicatorViewAttr_margin, SizeUtils.dp2px(5));
        array.recycle();

        views = new ArrayList<>();
    }

    public void setIndicatorNums(int nums) {
        removeAllViews();
        views.clear();
        for (int i = 0; i < nums; i++) {
            View view = new View(context);
            view.setBackgroundResource(noneChoiceColor);
            LayoutParams params = new LayoutParams(SizeUtils.dp2px(width), SizeUtils.dp2px(height));
            params.setMargins(margin, 0, margin, 0);
            addView(view, params);
            views.add(view);
        }
    }

    public void setCurrentPosition(int currentNum) {
        for (View view : views) {
            view.setBackgroundResource(noneChoiceColor);
        }
        if (views.size() > 0) {
            views.get(currentNum).setBackgroundResource(choiceColor);
        }
    }
}
