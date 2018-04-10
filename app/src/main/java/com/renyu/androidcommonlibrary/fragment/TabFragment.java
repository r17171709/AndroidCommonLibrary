package com.renyu.androidcommonlibrary.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.basefrag.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/10.
 */
public class TabFragment extends BaseFragment {

    @BindView(R.id.layout_tab)
    LinearLayout layout_tab;

    public static TabFragment getInstance(int color) {
        TabFragment tabFragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("color", color);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void initParams() {
        layout_tab.setBackgroundColor(getArguments().getInt("color"));
    }

    @Override
    public int initViews() {
        return R.layout.fragment_tab;
    }

    @Override
    public void loadData() {

    }
}
