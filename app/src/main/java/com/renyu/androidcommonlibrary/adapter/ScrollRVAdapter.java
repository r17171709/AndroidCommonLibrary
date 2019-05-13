package com.renyu.androidcommonlibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.renyu.androidcommonlibrary.R;

import java.util.ArrayList;

/**
 * Created by renyu on 2017/9/1.
 */

public class ScrollRVAdapter extends RecyclerView.Adapter<ScrollRVAdapter.ScrollRVViewHolder> {

    private Context context;
    private ArrayList<Object> beans;

    public ScrollRVAdapter(Context context, ArrayList<Object> beans) {
        this.context = context;
        this.beans = beans;
    }

    @NonNull
    @Override
    public ScrollRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_friendlist, parent, false);
        return new ScrollRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollRVViewHolder holder, final int position) {
        holder.tv_adapter_friendlist.setText("" + position);
        holder.layout_adapter_friendlist.setOnClickListener(v -> Log.d("ScrollRVAdapter", "点击" + position));
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class ScrollRVViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_adapter_friendlist;
        SimpleDraweeView iv_adapter_friendlist;
        TextView tv_adapter_friendlist;

        ScrollRVViewHolder(View itemView) {
            super(itemView);
            layout_adapter_friendlist = itemView.findViewById(R.id.layout_adapter_friendlist);
            iv_adapter_friendlist = itemView.findViewById(R.id.iv_adapter_friendlist);
            tv_adapter_friendlist = itemView.findViewById(R.id.tv_adapter_friendlist);
        }
    }
}
