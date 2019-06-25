package com.renyu.commonlibrary.views.actionsheet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.renyu.commonlibrary.views.actionsheet.fragment.TouTiaoActionSheetFragment;
import com.renyu.commonlibrary.views.wheelview.R;

public class ToutiaochoiceActionSheetAdapter extends RecyclerView.Adapter<ToutiaochoiceActionSheetAdapter.ToutiaochoiceViewHolder> {
    private String[] title;
    private int[] image;
    private int row;
    private TouTiaoActionSheetFragment.OnToutiaoChoiceItemClickListener listener;

    public ToutiaochoiceActionSheetAdapter(String[] title, int[] image, int row, TouTiaoActionSheetFragment.OnToutiaoChoiceItemClickListener listener) {
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
