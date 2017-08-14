package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.actionsheet.ActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.LoopView;
import com.renyu.commonlibrary.views.wheelview.OnItemSelectedListener;

import java.util.ArrayList;

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
                ArrayList<String> floor1=new ArrayList<>();
                floor1.add("单层");
                floor1.add("跃层");
                ArrayList<String> floor2=new ArrayList<>();
                ArrayList<String> floor3=new ArrayList<>();
                ArrayList<String> floor4=new ArrayList<>();
                for (int i=1;i<=99;i++) {
                    floor2.add(""+i);
                    floor3.add(""+i);
                    floor4.add(""+i);
                }
                View view_threeloopertitle_floor= LayoutInflater.from(this).inflate(R.layout.view_releaserental_threeloopertitle, null, false);
                final LoopView view_releaserental_threelooptitleview1= (LoopView) view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview1);
                final LoopView view_releaserental_threelooptitleview2= (LoopView) view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview2);
                final LoopView view_releaserental_threelooptitleview3= (LoopView) view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview3);
                final LoopView view_releaserental_threelooptitleview4= (LoopView) view_threeloopertitle_floor.findViewById(R.id.view_releaserental_threelooptitleview4);
                view_releaserental_threelooptitleview4.setVisibility(View.GONE);
                final TextView tv_releaserental_threelooptitleview4= (TextView) view_threeloopertitle_floor.findViewById(R.id.tv_releaserental_threelooptitleview4);
                tv_releaserental_threelooptitleview4.setVisibility(View.GONE);
                view_releaserental_threelooptitleview1.setListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        if (index==1) {
                            view_releaserental_threelooptitleview4.setVisibility(View.VISIBLE);
                            tv_releaserental_threelooptitleview4.setVisibility(View.VISIBLE);
                        }
                        else if (index==0) {
                            view_releaserental_threelooptitleview4.setVisibility(View.GONE);
                            tv_releaserental_threelooptitleview4.setVisibility(View.GONE);
                        }
                    }
                });
                initLooper(view_releaserental_threelooptitleview1, floor1);
                initLooper(view_releaserental_threelooptitleview2, floor2);
                initLooper(view_releaserental_threelooptitleview3, floor3);
                initLooper(view_releaserental_threelooptitleview4, floor4);
                ActionSheetFragment.build(getSupportFragmentManager())
                        .setChoice(ActionSheetFragment.CHOICE.CUSTOMER)
                        .setCanDismiss(false)
                        .setCancelTitle("取消")
                        .setOkTitle("确定")
                        .setOnOKListener(new ActionSheetFragment.OnOKListener() {
                            @Override
                            public void onOKClick(Object value) {

                            }
                        })
                        .setCustomerView(view_threeloopertitle_floor)
                        .show();
                break;
        }
    }

    private void initLooper(LoopView loopview, ArrayList<String> strings) {
        loopview.setNotLoop();
        loopview.setViewPadding(SizeUtils.dp2px(20), SizeUtils.dp2px(15), SizeUtils.dp2px(20), SizeUtils.dp2px(15));
        loopview.setItems(strings);
        loopview.setTextSize(18);
        loopview.setInitPosition(0);
    }
}
