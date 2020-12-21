package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.gridlayout.widget.GridLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jakewharton.rxbinding4.view.RxView;
import com.renyu.commonlibrary.views.wheelview.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.Disposable;

public class GridActionSheetFragment extends ActionSheetFragment {
    private OnItemClickListener onItemClickListener;

    private Disposable disposable;

    @Override
    View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_gridactionsheet, container, false);
    }

    @Override
    void initParams() {
        // 不需要标题栏的取消功能
        pop_cancel1.setVisibility(View.INVISIBLE);

        View view_space = realView.findViewById(R.id.view_space);
        view_space.setVisibility(View.VISIBLE);
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

        GridLayout pop_grid = realView.findViewById(R.id.pop_grid);
        int width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(20)) / getArguments().getInt("columnCount");
        for (int i = 0; i < getArguments().getStringArray("items").length; i++) {
            final int i_ = i;
            View viewChild = LayoutInflater.from(context).inflate(R.layout.adapter_share, null, false);
            LinearLayout adapter_share_layout = viewChild.findViewById(R.id.adapter_share_layout);
            disposable = RxView.clicks(adapter_share_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(i_);
                }
                dismiss();
            });
            ImageView adapter_share_image = viewChild.findViewById(R.id.adapter_share_image);
            TextView adapter_share_text = viewChild.findViewById(R.id.adapter_share_text);
            adapter_share_image.setImageResource(getArguments().getIntArray("images")[i]);
            adapter_share_text.setText(getArguments().getStringArray("items")[i]);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.setGravity(Gravity.CENTER);
            params.width = width;
            params.height = SizeUtils.dp2px(100);
            params.columnSpec = GridLayout.spec(i % getArguments().getInt("columnCount"));
            params.rowSpec = GridLayout.spec(i / getArguments().getInt("columnCount"));
            pop_grid.addView(viewChild, params);
        }
    }

    @Override
    public void onDestroyView() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }

        super.onDestroyView();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static GridActionSheetFragment newGridActionSheetFragmentInstance(FragmentActivity activity, String tag,
                                                                             String title, int titleColor,
                                                                             String cancelTitle, int cancelTitleColor,
                                                                             String[] items, int[] images, int columnCount,
                                                                             OnItemClickListener onItemClickListener,
                                                                             ActionSheetFragment.OnCancelListener onCancelListener) {
        GridActionSheetFragment fragment = new GridActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("titleColor", titleColor);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putStringArray("items", items);
        bundle.putIntArray("images", images);
        bundle.putInt("columnCount", columnCount);
        fragment.setArguments(bundle);
        fragment.setOnItemClickListener(onItemClickListener);
        fragment.setOnCancelListener(onCancelListener);
        fragment.show(activity, tag);
        return fragment;
    }
}
