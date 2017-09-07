package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatImageView;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SVGActivity extends BaseActivity {

    @BindView(R.id.iv_svg)
    AppCompatImageView iv_svg;

    @Override
    public void initParams() {
        VectorDrawableCompat a= VectorDrawableCompat.create(getResources(), R.drawable.ic_white_bottom_arrow, getTheme());
        a.setTint(Color.RED);
        iv_svg.setImageDrawable(a);
    }

    @Override
    public int initViews() {
        return R.layout.activity_svg;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
