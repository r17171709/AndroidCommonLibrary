package com.renyu.androidcommonlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

/**
 * Created by Administrator on 2017/6/21.
 */

public class InsideSwipyRefreshLayout extends SwipyRefreshLayout {

    private float startY;
    private float startX;
    private boolean mIsVpDragger;
    private final int mTouchSlop;

    public InsideSwipyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                startX = ev.getX();
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(mIsVpDragger) {
                    return false;
                }
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if(distanceX > mTouchSlop && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}