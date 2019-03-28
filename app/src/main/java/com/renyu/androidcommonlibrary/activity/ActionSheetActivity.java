package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFragment;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetUtils;
import com.renyu.commonlibrary.views.wheelview.LoopView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ActionSheetActivity extends BaseActivity {
    @Override
    public void initParams() {
        findViewById(R.id.btn_click).setOnClickListener(v -> {
//            View view_threeloopertitle_floor = addAction();
//            ActionSheetFragment.build()
//                    .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
//                    .setTitle("title", Color.BLUE)
//                    .setCancelTitle("取消", Color.YELLOW)
//                    .setOkTitle("确定", Color.RED)
//                    .setCanDismiss(false)
//                    .setOnOKListener(value -> {
//
//                    })
//                    .setCustomerView(view_threeloopertitle_floor)
//                    .show(ActionSheetActivity.this);

//            ActionSheetUtils.showGrid(this, "Hello", Color.BLUE, "cancel", Color.RED,
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    3, position -> {
//
//                    }, () -> {
//
//                    });

//            ActionSheetUtils.showList(this, "Hello", Color.BLUE, "cancel", Color.RED,
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"}, new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    2, position -> {
//
//                    }, this::finish);

            ActionSheetUtils.showCenterList(this, "", Color.BLUE, "cancel", Color.RED,
                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"}, position -> {

                    }, this::finish);


//            ActionSheetUtils.showTime(this, "Title", Color.BLUE, "Cancel", Color.YELLOW, "OK", Color.RED,
//                    10, 12, value -> {
//                Toast.makeText(this, "value:" + value, Toast.LENGTH_SHORT).show();
//            }, () -> {
//
//            });

//            ActionSheetUtils.showToutiaoChoice(this, "取消", Color.BLACK, new String[]{"微信好友", "朋友圈", "QQ好友", "微博",
//                            "微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    (row, column) -> Toast.makeText(ActionSheetActivity.this, "row:" + row + " column:" + column, Toast.LENGTH_SHORT).show(), () -> {
//
//                    });

//            ActionSheetUtils.showDateRange(this, "设置日期", Color.BLUE, "取消", Color.GRAY, "确定", Color.RED, 1545689754000L, System.currentTimeMillis(), false,
//                    value -> Toast.makeText(ActionSheetActivity.this, value.toString(), Toast.LENGTH_SHORT).show(), () -> {
//
//                    });
        });
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

    private void initLooper(LoopView loopview, ArrayList<String> strings) {
        loopview.setNotLoop();
        loopview.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
        loopview.setItems(strings);
        loopview.setTextSize(18);
        loopview.setInitPosition(0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (getSupportFragmentManager().getFragments().size() > 0) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    if (fragment instanceof ActionSheetFragment) {
                        View view_threeloopertitle_floor = addAction();
                        ((ActionSheetFragment) fragment).restoreCustomerView(view_threeloopertitle_floor);
                    }
                }
            }
        }
    }

    @NonNull
    private View addAction() {
        ArrayList<String> floor1 = new ArrayList<>();
        floor1.add("单层");
        floor1.add("跃层");
        ArrayList<String> floor2 = new ArrayList<>();
        ArrayList<String> floor3 = new ArrayList<>();
        ArrayList<String> floor4 = new ArrayList<>();
        for (int i = 1; i <= 99; i++) {
            floor2.add("" + i);
            floor3.add("" + i);
            floor4.add("" + i);
        }
        View view_threeloopertitle_floor = LayoutInflater.from(this).inflate(R.layout.view_releaserental_threeloopertitle, null, false);
        final LoopView view_releaserental_threelooptitleview1 = view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview1);
        final LoopView view_releaserental_threelooptitleview2 = view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview2);
        final LoopView view_releaserental_threelooptitleview3 = view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview3);
        final LoopView view_releaserental_threelooptitleview4 = view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview4);
        view_releaserental_threelooptitleview4.setVisibility(View.GONE);
        final TextView tv_releaserental_threelooptitleview4 = view_threeloopertitle_floor.findViewById(R.id.tv_releaserental_threelooptitleview4);
        tv_releaserental_threelooptitleview4.setVisibility(View.GONE);
        view_releaserental_threelooptitleview1.setListener(index -> {
            if (index == 1) {
                view_releaserental_threelooptitleview4.setVisibility(View.VISIBLE);
                tv_releaserental_threelooptitleview4.setVisibility(View.VISIBLE);
            } else if (index == 0) {
                view_releaserental_threelooptitleview4.setVisibility(View.GONE);
                tv_releaserental_threelooptitleview4.setVisibility(View.GONE);
            }
        });
        initLooper(view_releaserental_threelooptitleview1, floor1);
        initLooper(view_releaserental_threelooptitleview2, floor2);
        initLooper(view_releaserental_threelooptitleview3, floor3);
        initLooper(view_releaserental_threelooptitleview4, floor4);
        return view_threeloopertitle_floor;
    }
}
