package com.renyu.androidcommonlibrary.adapter.vlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.androidcommonlibrary.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ItemViewAdapter extends DelegateAdapter.Adapter<ItemViewAdapter.ItemViewHolder> {
    private Context context;
    private LayoutHelper layoutHelper;

    private ArrayList<String> beans;

    public ItemViewAdapter(Context context, LayoutHelper layoutHelper, ArrayList<String> beans) {
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
    public ItemViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_friendlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewAdapter.ItemViewHolder holder, int position) {
        holder.tv_adapter_friendlist.setText("" + position);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_adapter_friendlist;
        SimpleDraweeView iv_adapter_friendlist;
        TextView tv_adapter_friendlist;

        ItemViewHolder(View itemView) {
            super(itemView);
            layout_adapter_friendlist = itemView.findViewById(R.id.layout_adapter_friendlist);
            iv_adapter_friendlist = itemView.findViewById(R.id.iv_adapter_friendlist);
            tv_adapter_friendlist = itemView.findViewById(R.id.tv_adapter_friendlist);
        }
    }
}
