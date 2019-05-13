package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;

/**
 * Created by Administrator on 2017/9/7.
 */

public class SVGActivity extends BaseActivity {

    AppCompatImageView iv_svg;

    @Override
    public void initParams() {
        iv_svg = findViewById(R.id.iv_svg);
        VectorDrawableCompat a = VectorDrawableCompat.create(getResources(), R.drawable.ic_white_bottom_arrow, getTheme());
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
