package com.renyu.commonlibrary.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class GrayFrameLayout extends FrameLayout {
    private final Paint mPaint = new Paint();

    public GrayFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }
}