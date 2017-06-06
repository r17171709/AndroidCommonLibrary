package com.renyu.commonlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.renyu.commonlibrary.R;

/**
 * Created by renyu on 15/12/1.
 */
public class ProgressCircleView extends View {

    int background_color;
    int foreground_color;
    int second_color;
    int progress;
    int stoke_width;
    int width;
    int text_size;
    String text="0";
    int pdirection;
    int text_color;
    boolean showcircle;
    int start_direction;

    Bitmap circle;

    Paint paint_background=null;
    Paint paint_foreground=null;
    Paint paint_second=null;
    Paint paint_text=null;
    Paint paint_circle;

    public ProgressCircleView(Context context) {
        this(context, null);
    }

    public ProgressCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.ProgressCircleViewStyle);
        background_color=array.getColor(R.styleable.ProgressCircleViewStyle_background_color, Color.parseColor("#737373"));
        foreground_color=array.getColor(R.styleable.ProgressCircleViewStyle_foreground_color, Color.parseColor("#d0101b"));
        second_color=array.getColor(R.styleable.ProgressCircleViewStyle_second_color, Color.parseColor("#737373"));
        progress=array.getInt(R.styleable.ProgressCircleViewStyle_progress, 0);
        stoke_width= array.getDimensionPixelSize(R.styleable.ProgressCircleViewStyle_stoke_width, 10);
        width= array.getDimensionPixelSize(R.styleable.ProgressCircleViewStyle_width, 2);
        text_size=array.getDimensionPixelSize(R.styleable.ProgressCircleViewStyle_text_size, 10);
        pdirection=array.getInt(R.styleable.ProgressCircleViewStyle_pdirection, 1);
        text_color=array.getColor(R.styleable.ProgressCircleViewStyle_text_color, foreground_color);
        text=array.getString(R.styleable.ProgressCircleViewStyle_text);
        showcircle=array.getBoolean(R.styleable.ProgressCircleViewStyle_showcircle, false);
        start_direction=array.getInt(R.styleable.ProgressCircleViewStyle_start_direction, 2);
        array.recycle();

        paint_background=new Paint();
        paint_background.setAntiAlias(true);
        paint_background.setStrokeCap(Paint.Cap.ROUND);
        paint_background.setStrokeJoin(Paint.Join.ROUND);
        paint_background.setStrokeWidth(width);
        paint_background.setStyle(Paint.Style.FILL);
        paint_background.setColor(background_color);

        paint_foreground=new Paint();
        paint_foreground.setAntiAlias(true);
        paint_foreground.setStrokeCap(Paint.Cap.ROUND);
        paint_foreground.setStrokeJoin(Paint.Join.ROUND);
        paint_foreground.setStrokeWidth(stoke_width);
        paint_foreground.setStyle(Paint.Style.STROKE);
        paint_foreground.setColor(foreground_color);

        paint_second=new Paint();
        paint_second.setAntiAlias(true);
        paint_second.setStrokeCap(Paint.Cap.ROUND);
        paint_second.setStrokeJoin(Paint.Join.ROUND);
        paint_second.setStrokeWidth(width);
        paint_second.setStyle(Paint.Style.STROKE);
        paint_second.setColor(second_color);

        paint_text=new Paint();
        paint_text.setAntiAlias(true);
        paint_text.setColor(text_color);
        paint_text.setTextSize(text_size);

        paint_circle=new Paint();
        paint_circle.setAntiAlias(true);

        if (showcircle) {
            circle= BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_circleball);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius=getMeasuredHeight()<getMeasuredWidth()?getMeasuredHeight()/2-stoke_width:getMeasuredWidth()/2-stoke_width;
        //绘制里面的圆
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius, paint_background);
        canvas.drawCircle(getMeasuredWidth()/2, getMeasuredHeight()/2, radius, paint_second);
        //绘制圆弧
        RectF rectF=new RectF(getMeasuredWidth()/2-radius, getMeasuredHeight()/2-radius, getMeasuredWidth()/2+radius, getMeasuredHeight()/2+radius);
        if (pdirection==1) {
            canvas.drawArc(rectF, 90*(start_direction-3), (float) -progress/100*360, false, paint_foreground);

            if (showcircle) {
                //画圆球
                int x= (int) (getMeasuredWidth()/2-radius*Math.sin(Math.PI*((double) progress*360/100)/180.0));
                int y= (int) (getMeasuredHeight()/2-radius*Math.cos(Math.PI*((double) progress*360/100)/180.0));
                canvas.drawBitmap(circle, x-circle.getWidth()/2, y-circle.getHeight()/2, paint_circle);
            }
        }
        else {
            canvas.drawArc(rectF, 90*(start_direction-3), (float) progress/100*360, false, paint_foreground);

            if (showcircle) {
                //画圆球
                int x= (int) (getMeasuredWidth()/2+radius*Math.sin(Math.PI*((double) progress*360/100+90*(start_direction-2))/180.0));
                int y= (int) (getMeasuredHeight()/2-radius*Math.cos(Math.PI*((double) progress*360/100+90*(start_direction-2))/180.0));
                canvas.drawBitmap(circle, x-circle.getWidth()/2, y-circle.getHeight()/2, paint_circle);
            }
        }
        //绘制文字
        RectF rectText=new RectF(getMeasuredWidth()/2-radius, getMeasuredHeight()/2-radius, getMeasuredWidth()/2+radius, getMeasuredHeight()/2+radius);
        Paint.FontMetricsInt fontMetrics = paint_text.getFontMetricsInt();
        int baseline = ((int) (rectText.top + (rectText.bottom - rectText.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top));
        canvas.drawText(text, rectText.left+(rectText.width()-paint_text.measureText(text))/2, baseline, paint_text);
    }

    public void setText(String text, int progress) {
        this.text = text;
        this.progress=progress;
        invalidate();
    }
}
