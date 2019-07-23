package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.blankj.utilcode.util.Utils;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.adapter.ScrollRVAdapter;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.views.FullyLinearLayoutManager;
import com.renyu.commonlibrary.views.LocalImageHolderView;
import com.tencent.mars.xlog.Log;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * Created by renyu on 2017/9/1.
 */

public class ScrollRVActivity extends BaseActivity {
    SwipyRefreshLayout swipy_scrollrv;
    ConvenientBanner<Uri> cb_scrollrv;
    NestedScrollView ns_scrollrv;
    RecyclerView rv_scrollrv;
    ScrollRVAdapter adapter;
    HorizontalScrollView scroll_scrollrv_1;
    LinearLayout layout_scrollrv_1;
    HorizontalScrollView scroll_scrollrv_2;
    LinearLayout layout_scrollrv_2;
    GridLayout grid_scrollrv;
    MarqueeView<TextView, String> marquee_scrollrv;
    TextView tv_scrollrv_1;
    TextView tv_scrollrv_2;
    TextView tv_scrollrv_3;
    LinearLayout layout_scrollrv_3;

    ArrayList<Uri> linearLayoutBeans1;
    ArrayList<Object> linearLayoutBeans2;
    ArrayList<Object> linearLayoutBeans3;
    List<String> info;

    MarqueeFactory<TextView, String> marqueeFactory1;

    @Inject
    MMKV mmkv;

    @Override
    public void initParams() {
        ((ExampleApp) (Utils.getApp())).appComponent.plusAct().inject(this);
        mmkv.putBoolean("test", true);
        mmkv.getBoolean("test", false);

        swipy_scrollrv = findViewById(R.id.swipy_scrollrv);
        cb_scrollrv = findViewById(R.id.cb_scrollrv);
        ns_scrollrv = findViewById(R.id.ns_scrollrv);
        rv_scrollrv = findViewById(R.id.rv_scrollrv);
        scroll_scrollrv_1 = findViewById(R.id.scroll_scrollrv_1);
        layout_scrollrv_1 = findViewById(R.id.layout_scrollrv_1);
        scroll_scrollrv_2 = findViewById(R.id.scroll_scrollrv_2);
        layout_scrollrv_2 = findViewById(R.id.layout_scrollrv_2);
        grid_scrollrv = findViewById(R.id.grid_scrollrv);
        marquee_scrollrv = findViewById(R.id.marquee_scrollrv);
        tv_scrollrv_1 = findViewById(R.id.tv_scrollrv_1);
        tv_scrollrv_2 = findViewById(R.id.tv_scrollrv_2);
        tv_scrollrv_3 = findViewById(R.id.tv_scrollrv_3);
        layout_scrollrv_3 = findViewById(R.id.layout_scrollrv_3);

        linearLayoutBeans1 = new ArrayList<>();
        linearLayoutBeans1.add(Uri.parse("http://f.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a29f863534d9725bc315d607c8e.jpg"));
        linearLayoutBeans1.add(Uri.parse("http://a.hiphotos.baidu.com/image/h%3D300/sign=a62e824376d98d1069d40a31113eb807/838ba61ea8d3fd1fc9c7b6853a4e251f94ca5f46.jpg"));
        linearLayoutBeans1.add(Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1504161160133&di=bd17c2859efa779ddca3b8ab1e1acb68&imgtype=0&src=http%3A%2F%2Fguangdong.sinaimg.cn%2F2014%2F0508%2FU10729P693DT20140508144234.jpg"));

        linearLayoutBeans2 = new ArrayList<>();
        linearLayoutBeans2.addAll(getBeans(15));

        linearLayoutBeans3 = new ArrayList<>();
        linearLayoutBeans3.addAll(getBeans(10));

        refreshVP();

        grid_scrollrv.post(() -> {
            grid_scrollrv.removeAllViews();
            for (int i = 0; i < 8; i++) {
                View view = LayoutInflater.from(ScrollRVActivity.this).inflate(R.layout.adapter_grid, null, false);
                view.setOnClickListener(v -> Log.d("ScrollRVActivity", "点击4"));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = grid_scrollrv.getMeasuredWidth() / 4;
                params.height = grid_scrollrv.getMeasuredWidth() / 4;
                grid_scrollrv.addView(view, params);
            }
        });

        info = new ArrayList<>();
        info.add("1. 大家好，我是孙福生。\n2. 欢迎大家关注我哦！");
        info.add("3. GitHub帐号：sfsheng0322 \n4. 新浪微博：孙福生微博");
        info.add("5. 个人博客：sunfusheng.com \n6. 微信公众号：孙福生");
        marqueeFactory1 = new NoticeMF(this);
        marqueeFactory1.setData(info);
        marquee_scrollrv.setMarqueeFactory(marqueeFactory1);
        marquee_scrollrv.setOnItemClickListener((mView, mData, mPosition) -> Toast.makeText(ScrollRVActivity.this, mData.toString(), Toast.LENGTH_SHORT).show());

        refreshScrollView();

        swipy_scrollrv.setOnRefreshListener(direction -> new Handler().postDelayed(() -> {
            if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                linearLayoutBeans2.addAll(getBeans(15));
                adapter.notifyDataSetChanged();
            } else if (direction == SwipyRefreshLayoutDirection.TOP) {
                linearLayoutBeans1.add(Uri.parse("http://a.hiphotos.baidu.com/image/pic/item/0b7b02087bf40ad15a962c0b592c11dfa8ecceec.jpg"));
                refreshVP();

                info.clear();
                info.add("11. 大家好，我是孙福生。\n21. 欢迎大家关注我哦！");
                info.add("31. GitHub帐号：sfsheng0322 \n41. 新浪微博：孙福生微博");
                info.add("51. 个人博客：sunfusheng.com \n61. 微信公众号：孙福生");
                marqueeFactory1.setData(info);

                linearLayoutBeans3.clear();
                linearLayoutBeans3.addAll(getBeans(4));
                refreshScrollView();
                layout_scrollrv_1.post(() -> scroll_scrollrv_1.scrollTo(0, 0));
                layout_scrollrv_2.post(() -> scroll_scrollrv_2.scrollTo(0, 0));

                linearLayoutBeans2.clear();
                linearLayoutBeans2.addAll(getBeans(15));
                adapter.notifyDataSetChanged();
            }
            swipy_scrollrv.setRefreshing(false);
        }, 2000));

        tv_scrollrv_1.setOnClickListener((view) -> {
            update();
        });
        tv_scrollrv_2.setOnClickListener((view) -> {
            update();
        });
        tv_scrollrv_3.setOnClickListener((view) -> {
            update();
        });

        rv_scrollrv.setHasFixedSize(true);
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(this);
        rv_scrollrv.setNestedScrollingEnabled(false);
        rv_scrollrv.setLayoutManager(linearLayoutManager);
        adapter = new ScrollRVAdapter(this, linearLayoutBeans2);
        rv_scrollrv.setAdapter(adapter);
    }

    private void refreshScrollView() {
        layout_scrollrv_1.removeAllViews();
        for (int i = 0; i < linearLayoutBeans3.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.adapter_item, null, false);
            v.setOnClickListener(v12 -> Log.d("ScrollRVActivity", "点击2"));
            TextView tv_releaserentalsuccess_address = v.findViewById(R.id.tv_releaserentalsuccess_address);
            tv_releaserentalsuccess_address.setText(linearLayoutBeans3.get(i).toString());
            layout_scrollrv_1.addView(v);
        }
        layout_scrollrv_2.removeAllViews();
        for (int i = 0; i < linearLayoutBeans3.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.adapter_item, null, false);
            v.setOnClickListener(v1 -> Log.d("ScrollRVActivity", "点击3"));
            TextView tv_releaserentalsuccess_address = v.findViewById(R.id.tv_releaserentalsuccess_address);
            tv_releaserentalsuccess_address.setText(linearLayoutBeans3.get(i).toString());
            layout_scrollrv_2.addView(v);
        }
    }

    private void refreshVP() {
        cb_scrollrv.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new LocalImageHolderView(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.adapter_convenientbanner;
            }
        }, linearLayoutBeans1).setOnItemClickListener(position -> {

        }).setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }

            @Override
            public void onPageSelected(int index) {
                Log.d("ScrollRVActivity", "position:" + index);
            }
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_scrollrv;
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

    private ArrayList<Object> getBeans(int num) {
        ArrayList<Object> linearLayoutBeans = new ArrayList<>();
        if (num == -1) {
            Random random = new Random();
            num = 10 + random.nextInt(20);
        }
        for (int i = 0; i < num; i++) {
            Random random = new Random();
            linearLayoutBeans.add("" + i + "-" + random.nextInt(20));
        }
        return linearLayoutBeans;
    }

    private void update() {
        swipy_scrollrv.getChildAt(0).scrollTo(0, layout_scrollrv_3.getTop());
        new Handler().postDelayed(() -> {
            linearLayoutBeans2.clear();
            linearLayoutBeans2.addAll(getBeans(-1));
            adapter.notifyDataSetChanged();

        }, 2000);
    }

    @Override
    public void onStart() {
        super.onStart();
        cb_scrollrv.startTurning(4000);
        marquee_scrollrv.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        cb_scrollrv.stopTurning();
        marquee_scrollrv.stopFlipping();
    }
}
