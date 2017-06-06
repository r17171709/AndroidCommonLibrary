package com.renyu.commonlibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by renyu on 2017/3/7.
 */

public class FlowLayout extends ViewGroup {

    Context context;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        this.context=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i=0;i<getChildCount();i++) {
            View view=getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        int allWidth= MeasureSpec.getSize(widthMeasureSpec);
        int allHeight=0;

        // 上一次测量完的宽度
        int lastWidth=0;
        // 上一次测量完的高度
        int lastHeight=0;

        for (int i=0;i<getChildCount();i++) {
            View view=getChildAt(i);
            int childWidth=view.getMeasuredWidth();
            int childHeight=view.getMeasuredHeight();
            MarginLayoutParams params= (MarginLayoutParams) view.getLayoutParams();
            // 只有一行
            if (allHeight==0) {
                allHeight=lastHeight+params.topMargin+childHeight+params.bottomMargin;
            }
            if (lastWidth+params.leftMargin+params.rightMargin+childWidth>allWidth) {
                lastHeight+=params.topMargin+childHeight+params.bottomMargin;
                allHeight=lastHeight+params.topMargin+childHeight+params.bottomMargin;
                lastWidth=0;
            }
            lastWidth+=params.leftMargin+childWidth+params.rightMargin;
        }

        setMeasuredDimension(allWidth, allHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int screenWidth=getWidth();

        // 上一次测量完的宽度
        int lastWidth=0;
        // 上一次测量完的高度
        int lastHeight=0;

        for (int i=0;i<getChildCount();i++) {
            View view=getChildAt(i);
            int childWidth=view.getMeasuredWidth();
            int childHeight=view.getMeasuredHeight();
            MarginLayoutParams params= (MarginLayoutParams) view.getLayoutParams();
            if (lastWidth+params.leftMargin+params.rightMargin+childWidth>screenWidth) {
                lastHeight+=params.topMargin+childHeight+params.bottomMargin;
                lastWidth=0;
            }
            view.layout(lastWidth+params.leftMargin, lastHeight+params.topMargin,
                    lastWidth+params.leftMargin+childWidth, lastHeight+params.topMargin+childHeight);
            lastWidth+=params.leftMargin+childWidth+params.rightMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(context, attrs);
    }
}
