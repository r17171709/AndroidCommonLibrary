package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.renyu.commonlibrary.views.wheelview.R;

public class CustomActionSheetFragment extends ActionSheetFragment {
    // 自定义视图
    private View customerView;

    @Override
    View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_customactionsheet, container, false);
    }

    @Override
    void initParams() {
        LinearLayout pop_morechoice = realView.findViewById(R.id.pop_morechoice);
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(okTitle) && TextUtils.isEmpty(cancelTitle)) {
            pop_morechoice.setVisibility(View.GONE);
        } else {
            pop_ok1.setOnClickListener(v -> {
                if (onOKListener != null) {
                    onOKListener.onOKClick("");
                }
                if (getArguments().getBoolean("canDismiss")) {
                    dismiss();
                }
            });
            pop_cancel1.setOnClickListener(v -> {
                if (onCancelListener != null) {
                    onCancelListener.onCancelClick();
                }
                dismiss();
            });
        }
        LinearLayout pop_customer_layout = realView.findViewById(R.id.pop_customer_layout);
        pop_customer_layout.setVisibility(View.VISIBLE);
        if (customerView != null) {
            pop_customer_layout.removeAllViews();
            if (customerView.getParent() != null) {
                // 上一个引用customerView的地方会出现留白
                // 这不是解决问题的正确方法，应该避免传入正在使用的同一个对象
                ((ViewGroup) customerView.getParent()).removeAllViews();
            }
            pop_customer_layout.addView(customerView);
        }
    }

    private void setCustomView(View customerView) {
        this.customerView = customerView;
    }

    public static void newCustomActionSheetFragmentInstance(FragmentActivity activity, String tag,
                                                            String title, int titleColor,
                                                            String okTitle, int okTitleColor,
                                                            String cancelTitle, int cancelTitleColor,
                                                            boolean canDismiss, View customView) {
        CustomActionSheetFragment fragment = new CustomActionSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("titleColor", titleColor);
        bundle.putString("okTitle", okTitle);
        bundle.putInt("okTitleColor", okTitleColor);
        bundle.putString("cancelTitle", cancelTitle);
        bundle.putInt("cancelTitleColor", cancelTitleColor);
        bundle.putBoolean("canDismiss", canDismiss);
        fragment.setArguments(bundle);
        fragment.setCustomView(customView);
        fragment.show(activity, tag);
    }
}
