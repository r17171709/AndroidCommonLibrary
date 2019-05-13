package com.renyu.androidcommonlibrary.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.AndroidBug5497Workaround;
import com.renyu.commonlibrary.commonutils.BarUtils;

/**
 * Created by renyu on 2017/7/2.
 */

public class MoreEditActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_moreedit;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BarUtils.setDark(this);
        super.onCreate(savedInstanceState);
        // 全屏模式下添加，或者在rootview加fitsSystemWindows，需要包裹ScrollView
        AndroidBug5497Workaround.assistActivity(this);
    }
}
