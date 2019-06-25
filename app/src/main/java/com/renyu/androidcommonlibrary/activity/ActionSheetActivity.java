package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.blankj.utilcode.util.SizeUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFactory;
import com.renyu.commonlibrary.views.wheelview.LoopView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ActionSheetActivity extends BaseActivity {
    @Override
    public void initParams() {
        findViewById(R.id.btn_click).setOnClickListener(v -> {
            View view_threeloopertitle_floor = addAction();
            ActionSheetFactory.createCustomActionSheetFragment(this, "", "自定义视图", Color.BLUE,
                    "确定", Color.RED,
                    "取消", Color.GRAY,
                    false,
                    view_threeloopertitle_floor);

//            ActionSheetFactory.createGridActionSheetFragment(this, "", "Hello", Color.BLUE, "cancel", Color.RED,
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    4, position -> {
//
//                    }, () -> {
//
//                    });

//            ActionSheetFactory.createListActionSheetFragment(this, "", "Hello", Color.BLUE, "cancel", Color.RED,
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"}, new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    2, position -> {
//
//                    }, this::finish);

//            ActionSheetFactory.createCenterListActionSheetFragment(this, "", "", Color.BLUE, "cancel", Color.RED,
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"}, position -> {
//
//                    }, this::finish);

//            ActionSheetFactory.createTimeActionSheetFragment(this, "", "Title", Color.BLUE, "OK", Color.RED, "Cancel", Color.YELLOW,
//                    10, 12, value -> Toast.makeText(this, "value:" + value, Toast.LENGTH_SHORT).show(), () -> {
//
//                    });

//            ActionSheetFactory.createTouTiaoActionSheetFragment(this, "", "取消", Color.BLACK, new String[]{"微信好友", "朋友圈", "QQ好友", "微博",
//                            "微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
//                            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    new String[]{"微信好友", "朋友圈", "QQ好友", "微博"},
//                    new int[]{R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                    (row, column) -> Toast.makeText(ActionSheetActivity.this, "row:" + row + " column:" + column, Toast.LENGTH_SHORT).show(), () -> {
//
//                    });

//            ActionSheetFactory.createDateRangeActionSheetFragment(this, "", "设置日期", Color.BLUE, "确定", Color.RED, "取消", Color.GRAY,
//                    1545689754000L, System.currentTimeMillis(), false,
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
