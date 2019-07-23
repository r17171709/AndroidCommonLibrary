package com.renyu.androidcommonlibrary.adapter.vlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.renyu.androidcommonlibrary.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 */

public class HeaderViewAdapter extends DelegateAdapter.Adapter<HeaderViewAdapter.HeaderViewHolder> {
    private Context context;
    private LayoutHelper layoutHelper;

    private ArrayList<String> beans;

    public HeaderViewAdapter(Context context, LayoutHelper layoutHelper, ArrayList<String> beans) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.beans = beans;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @NonNull
    @Override
    public HeaderViewAdapter.HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_friendlist, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewAdapter.HeaderViewHolder holder, int position) {
        holder.tv_friendlist_head.setText("a" + beans.get(position));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tv_friendlist_head;

        HeaderViewHolder(View itemView) {
            super(itemView);
            tv_friendlist_head = itemView.findViewById(R.id.tv_friendlist_head);
        }
    }
}
