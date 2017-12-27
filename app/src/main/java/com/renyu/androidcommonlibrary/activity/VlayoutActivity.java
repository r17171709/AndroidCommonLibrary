package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.adapter.HeaderViewAdapter;
import com.renyu.androidcommonlibrary.adapter.ItemViewAdapter;
import com.renyu.commonlibrary.baseact.BaseActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/4.
 */

public class VlayoutActivity extends BaseActivity {

    @BindView(R.id.swipe_vlayout)
    SwipyRefreshLayout swipe_vlayout;
    @BindView(R.id.rv_vlayout)
    RecyclerView rv_vlayout;

    List<DelegateAdapter.Adapter> adapters;
    ArrayList<Object> linearLayoutBeansHeader1;
    ArrayList<Object> linearLayoutBeans1;
    ArrayList<Object> linearLayoutBeansHeader2;
    ArrayList<Object> linearLayoutBeans2;

    @Override
    public void initParams() {
        linearLayoutBeansHeader1=new ArrayList<>();
        linearLayoutBeansHeader1.add("cccc1");
        linearLayoutBeans1=new ArrayList<>();
        linearLayoutBeans1.addAll(getBeans());
        linearLayoutBeansHeader2=new ArrayList<>();
        linearLayoutBeansHeader2.add("cccc2");
        linearLayoutBeans2=new ArrayList<>();
        linearLayoutBeans2.addAll(getBeans());
        adapters=new LinkedList<>();

        VirtualLayoutManager manager=new VirtualLayoutManager(this);
        rv_vlayout.setLayoutManager(manager);
        RecyclerView.RecycledViewPool pool=new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 20);
        rv_vlayout.setRecycledViewPool(pool);
        DelegateAdapter delegateAdapter=new DelegateAdapter(manager, false);
        rv_vlayout.setAdapter(delegateAdapter);
        adapters.add(new HeaderViewAdapter(this, new StickyLayoutHelper(), linearLayoutBeansHeader1));
        adapters.add(new ItemViewAdapter(this, new LinearLayoutHelper(), linearLayoutBeans1));
        adapters.add(new HeaderViewAdapter(this, new StickyLayoutHelper(), linearLayoutBeansHeader2));
        adapters.add(new ItemViewAdapter(this, new LinearLayoutHelper(), linearLayoutBeans2));
        delegateAdapter.setAdapters(adapters);

        swipe_vlayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                linearLayoutBeans1.addAll(getBeans());
//                adapters.get(1).notifyDataSetChanged();

                ArrayList<Object> linearLayoutBeansHeader1=new ArrayList<>();
                linearLayoutBeansHeader1.add("cccc3");
                ArrayList<Object> linearLayoutBeans1=new ArrayList<>();
                linearLayoutBeans1.addAll(getBeans());
                Log.d("VlayoutActivity", "linearLayoutBeans2.size():" + linearLayoutBeans1.size());
                ArrayList<Object> linearLayoutBeansHeader2=new ArrayList<>();
                linearLayoutBeansHeader2.add("cccc4");
                ArrayList<Object> linearLayoutBeans2=new ArrayList<>();
                linearLayoutBeans2.addAll(getBeans());
                Log.d("VlayoutActivity", "linearLayoutBeans2.size():" + linearLayoutBeans2.size());
                delegateAdapter.addAdapter(new HeaderViewAdapter(VlayoutActivity.this, new StickyLayoutHelper(), linearLayoutBeansHeader1));
                delegateAdapter.addAdapter(new ItemViewAdapter(VlayoutActivity.this, new LinearLayoutHelper(), linearLayoutBeans1));
                delegateAdapter.addAdapter(new HeaderViewAdapter(VlayoutActivity.this, new StickyLayoutHelper(), linearLayoutBeansHeader2));
                delegateAdapter.addAdapter(new ItemViewAdapter(VlayoutActivity.this, new LinearLayoutHelper(), linearLayoutBeans2));
                delegateAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe_vlayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private ArrayList<Object> getBeans() {
        ArrayList<Object> linearLayoutBeans=new ArrayList<>();
        Random random=new Random();
        for (int i=0;i<5+random.nextInt(5);i++) {
            linearLayoutBeans.add(""+i);
        }
        return linearLayoutBeans;
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
}
