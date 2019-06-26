package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.renyu.commonlibrary.views.actionsheet.adapter.ToutiaochoiceActionSheetAdapter;
import com.renyu.commonlibrary.views.wheelview.R;

public class TouTiaoActionSheetFragment extends ActionSheetFragment {
    private OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener;

    @Override
    View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_toutiaoactionsheet, container, false);
    }

    @Override
    void initParams() {
        TextView pop_cancel = realView.findViewById(R.id.pop_cancel);
        if (getArguments().getInt("cancelTitleColor", -1) != -1) {
            pop_cancel.setTextColor(getArguments().getInt("cancelTitleColor"));
        }
        if (!TextUtils.isEmpty(cancelTitle)) {
            pop_cancel.setText(cancelTitle);
            pop_cancel.setVisibility(View.VISIBLE);
            pop_cancel.setOnClickListener(v -> {
                if (onCancelListener != null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
        }
        if (TextUtils.isEmpty(title)) {
            LinearLayout pop_morechoice = realView.findViewById(R.id.pop_morechoice);
            pop_morechoice.setVisibility(View.GONE);
        }

        String[] topTitles = getArguments().getStringArray("topTitles");
        int[] topImages = getArguments().getIntArray("topImages");
        RecyclerView rv_toutiaochoice_top = realView.findViewById(R.id.rv_toutiaochoice_top);
        rv_toutiaochoice_top.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_toutiaochoice_top.setHasFixedSize(true);
        ToutiaochoiceActionSheetAdapter adapterTop = new ToutiaochoiceActionSheetAdapter(topTitles, topImages, 1, onToutiaoChoiceItemClickListener);
        rv_toutiaochoice_top.setAdapter(adapterTop);

        String[] bottomTitles = getArguments().getStringArray("bottomTitles");
        int[] bottomImages = getArguments().getIntArray("bottomImages");
        RecyclerView rv_toutiaochoice_bottom = realView.findViewById(R.id.rv_toutiaochoice_bottom);
        rv_toutiaochoice_bottom.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rv_toutiaochoice_bottom.setHasFixedSize(true);
        ToutiaochoiceActionSheetAdapter adapterBottom = new ToutiaochoiceActionSheetAdapter(bottomTitles, bottomImages, 2, onToutiaoChoiceItemClickListener);
        rv_toutiaochoice_bottom.setAdapter(adapterBottom);
    }

    public interface OnToutiaoChoiceItemClickListener {
        void onItemClick(int row, int column);
    }

    private void setOnToutiaoChoiceItemClickListener(OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener) {
        this.onToutiaoChoiceItemClickListener = onToutiaoChoiceItemClickListener;
    }


    public static TouTiaoActionSheetFragment newTouTiaoActionSheetFragmentInstance(FragmentActivity activity, String tag,
                                                                                   String cancelTitle, int cancelTitleColor,
                                                                                   String[] topTitles, int[] topImages,
                                                                                   String[] bottomTitles, int[] bottomImages,
                                                                                   OnToutiaoChoiceItemClickListener onToutiaoChoiceItemClickListener,
                                                                                   ActionSheetFragment.OnCancelListener onCancelListener) {
        TouTiaoActionSheetFragment fragment = new TouTiaoActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putStringArray("topTitles", topTitles);
        bundle.putIntArray("topImages", topImages);
        bundle.putStringArray("bottomTitles", bottomTitles);
        bundle.putIntArray("bottomImages", bottomImages);
        fragment.setArguments(bundle);
        fragment.setOnToutiaoChoiceItemClickListener(onToutiaoChoiceItemClickListener);
        fragment.setOnCancelListener(onCancelListener);
        fragment.show(activity, tag);
        return fragment;
    }
}
