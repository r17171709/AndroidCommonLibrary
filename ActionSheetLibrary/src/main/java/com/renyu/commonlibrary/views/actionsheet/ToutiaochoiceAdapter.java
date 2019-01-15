package com.renyu.commonlibrary.views.actionsheet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.renyu.commonlibrary.views.wheelview.R;

public class ToutiaochoiceAdapter extends RecyclerView.Adapter<ToutiaochoiceAdapter.ToutiaochoiceViewHolder> {

    private String[] title;
    private int[] image;
    private int row;
    private ActionSheetFragment.OnToutiaoChoiceItemClickListener listener;

    ToutiaochoiceAdapter(String[] title, int[] image, int row, ActionSheetFragment.OnToutiaoChoiceItemClickListener listener) {
        this.title = title;
        this.image = image;
        this.row = row;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToutiaochoiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_toutiaochoice, viewGroup, false);
        return new ToutiaochoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToutiaochoiceViewHolder toutiaochoiceViewHolder, int i) {
        toutiaochoiceViewHolder.iv_toutiaochoice.setImageResource(image[toutiaochoiceViewHolder.getLayoutPosition()]);
        toutiaochoiceViewHolder.tv_toutiaochoice.setText(title[toutiaochoiceViewHolder.getLayoutPosition()]);
        toutiaochoiceViewHolder.itemView.setOnClickListener(v -> listener.onItemClick(row, toutiaochoiceViewHolder.getLayoutPosition()));
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    class ToutiaochoiceViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_toutiaochoice;
        TextView tv_toutiaochoice;

        ToutiaochoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_toutiaochoice = itemView.findViewById(R.id.iv_toutiaochoice);
            tv_toutiaochoice = itemView.findViewById(R.id.tv_toutiaochoice);
        }
    }
}
