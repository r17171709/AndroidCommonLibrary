package com.renyu.androidcommonlibrary.activity;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.adapter.ShopAdapter;
import com.renyu.androidcommonlibrary.bean.DiscreteBean;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.Orientation;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by renyu on 2017/7/9.
 */

public class DiscreteScrollviewActivity extends BaseActivity {

    @BindView(R.id.scroll_discrete)
    DiscreteScrollView scroll_discrete;

    List<DiscreteBean> data;

    @Override
    public void initParams() {
        data=new ArrayList<>();
        data.add(new DiscreteBean(1, "name1", "price1", R.mipmap.shop1));
        data.add(new DiscreteBean(2, "name2", "price2", R.mipmap.shop2));
        data.add(new DiscreteBean(3, "name3", "price3", R.mipmap.shop3));
        data.add(new DiscreteBean(4, "name4", "price4", R.mipmap.shop4));
        data.add(new DiscreteBean(5, "name5", "price5", R.mipmap.shop5));
        data.add(new DiscreteBean(6, "name6", "price6", R.mipmap.shop6));
    }

    @Override
    public int initViews() {
        return R.layout.activity_discretescrollview;
    }

    @Override
    public void loadData() {
        scroll_discrete.setOrientation(Orientation.HORIZONTAL);
        scroll_discrete.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                onItemChanged(data.get(adapterPosition));
            }
        });
        scroll_discrete.setAdapter(new ShopAdapter(data));
        scroll_discrete.setItemTransitionTimeMillis(150);

        onItemChanged(data.get(0));
    }

    private void onItemChanged(DiscreteBean item) {
        Log.d("DiscreteScrollviewActiv", "item.getId():" + item.getId());
    }

    @Override
    public int setStatusBarColor() {
        return ContextCompat.getColor(this, R.color.colorPrimary);
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
