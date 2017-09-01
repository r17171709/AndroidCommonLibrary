package com.renyu.androidcommonlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.androidcommonlibrary.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by renyu on 2017/9/1.
 */

public class ScrollRVAdapter extends RecyclerView.Adapter<ScrollRVAdapter.ScrollRVViewHolder> {

    Context context;
    ArrayList<Object> beans;

    public ScrollRVAdapter(Context context, ArrayList<Object> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public ScrollRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_friendlist, parent, false);
        return new ScrollRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScrollRVViewHolder holder, final int position) {
        holder.tv_adapter_friendlist.setText(""+position);
        holder.layout_adapter_friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ScrollRVAdapter", "点击"+position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ScrollRVViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_adapter_friendlist)
        LinearLayout layout_adapter_friendlist;
        @BindView(R.id.iv_adapter_friendlist)
        SimpleDraweeView iv_adapter_friendlist;
        @BindView(R.id.tv_adapter_friendlist)
        TextView tv_adapter_friendlist;

        public ScrollRVViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
