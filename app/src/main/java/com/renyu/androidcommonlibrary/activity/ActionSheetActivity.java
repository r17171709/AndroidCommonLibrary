package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.view.View;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFragment;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetUtils;

import butterknife.OnClick;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ActionSheetActivity extends BaseActivity {
    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_actionsheet;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return Color.RED;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @OnClick({R.id.btn_click})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click:
                ActionSheetUtils.showList(getSupportFragmentManager(), "title", new String[]{"1", "2"}, new ActionSheetFragment.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }
                }, new ActionSheetFragment.OnCancelListener() {
                    @Override
                    public void onCancelClick() {

                    }
                });
                break;
        }
    }
}
