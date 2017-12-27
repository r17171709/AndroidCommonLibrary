package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.adapter.ItemViewAdapter;
import com.renyu.androidcommonlibrary.adapter.RecyclerViewAdapter;
import com.renyu.commonlibrary.baseact.BaseActivity;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by renyu on 2017/12/27.
 */

public class RecyclerVIewActivity extends BaseActivity {

    @BindView(R.id.swipe_vlayout)
    SwipyRefreshLayout swipe_vlayout;
    @BindView(R.id.rv_vlayout)
    RecyclerView rv_vlayout;

    ArrayList<Object> linearLayoutBeans1;

    RecyclerViewAdapter adapter;

    @Override
    public void initParams() {
        linearLayoutBeans1=new ArrayList<>();
        linearLayoutBeans1.addAll(getBeans());

        swipe_vlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                linearLayoutBeans1.addAll(getBeans());
                adapter.notifyDataSetChanged();
            }
        });
        VirtualLayoutManager manager=new VirtualLayoutManager(this);
        rv_vlayout.setLayoutManager(manager);
        adapter = new RecyclerViewAdapter(this, linearLayoutBeans1);
        rv_vlayout.setAdapter(adapter);
    }

    @Override
    public int initViews() {
        return R.layout.activity_vlayout;
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

    private ArrayList<Object> getBeans() {
        ArrayList<Object> linearLayoutBeans=new ArrayList<>();
        Random random=new Random();
        for (int i=0;i<10+random.nextInt(10);i++) {
            linearLayoutBeans.add(""+i);
        }
        return linearLayoutBeans;
    }
}
