package com.renyu.androidcommonlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
 * Created by renyu on 2017/12/27.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    Context context;

    ArrayList<Object> beans;

    public RecyclerViewAdapter(Context context, ArrayList<Object> beans) {
        this.context=context;
        this.beans=beans;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.adapter_friendlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tv_adapter_friendlist.setText(""+position);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_adapter_friendlist)
        LinearLayout layout_adapter_friendlist;
        @BindView(R.id.iv_adapter_friendlist)
        SimpleDraweeView iv_adapter_friendlist;
        @BindView(R.id.tv_adapter_friendlist)
        TextView tv_adapter_friendlist;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
