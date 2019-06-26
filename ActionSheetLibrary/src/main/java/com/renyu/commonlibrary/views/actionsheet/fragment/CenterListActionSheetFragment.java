package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.renyu.commonlibrary.views.actionsheet.adapter.CenterListActionSheetAdapter;
import com.renyu.commonlibrary.views.wheelview.R;

public class CenterListActionSheetFragment extends ActionSheetFragment {
    private OnItemClickListener onItemClickListener;

    @Override
    public View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_listactionsheet, container, false);
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

        ListView pop_listview = realView.findViewById(R.id.pop_listview);
        CenterListActionSheetAdapter adapter = new CenterListActionSheetAdapter(context, getArguments().getStringArray("items"));
        pop_listview.setAdapter(adapter);
        pop_listview.setOnItemClickListener((parent, view1, position, id) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
                dismiss();
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static CenterListActionSheetFragment newCenterListActionSheetFragmentInstance(FragmentActivity activity, String tag, String title, int titleColor,
                                                                                         String cancelTitle, int cancelTitleColor,
                                                                                         String[] items,
                                                                                         OnItemClickListener onItemClickListener,
                                                                                         ActionSheetFragment.OnCancelListener onCancelListener) {
        CenterListActionSheetFragment fragment = new CenterListActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("titleColor", titleColor);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putStringArray("items", items);
        fragment.setArguments(bundle);
        fragment.setOnItemClickListener(onItemClickListener);
        fragment.setOnCancelListener(onCancelListener);
        fragment.show(activity, tag);
        return fragment;
    }
}
