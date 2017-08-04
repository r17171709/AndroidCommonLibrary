package com.renyu.androidcommonlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.renyu.androidcommonlibrary.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/4.
 */

public class HeaderViewAdapter extends DelegateAdapter.Adapter<HeaderViewAdapter.HeaderViewHolder> {

    Context context;
    LayoutHelper layoutHelper;

    ArrayList<Object> beans;

    public HeaderViewAdapter(Context context, LayoutHelper layoutHelper, ArrayList<Object> beans) {
        this.context=context;
        this.layoutHelper=layoutHelper;
        this.beans=beans;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public HeaderViewAdapter.HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.header_friendlist, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeaderViewAdapter.HeaderViewHolder holder, int position) {
        holder.tv_friendlist_head.setText("a"+beans.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_friendlist_head)
        TextView tv_friendlist_head;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
