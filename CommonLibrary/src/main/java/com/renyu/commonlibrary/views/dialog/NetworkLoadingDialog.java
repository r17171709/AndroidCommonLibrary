package com.renyu.commonlibrary.views.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.renyu.commonlibrary.R;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public class NetworkLoadingDialog extends DialogFragment {

    private boolean isDismiss = true;
    private FragmentManager manager = null;
    private OnDialogDismiss onDialogDismissListener = null;

    public static NetworkLoadingDialog getInstance() {
        return getInstance("");
    }

    public static NetworkLoadingDialog getInstance(String loadingText) {
        NetworkLoadingDialog dialog = new NetworkLoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putString("loadingText", loadingText);
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnDialogDismiss {
        void onDismiss();
    }

    public void setDialogDismissListener(OnDialogDismiss onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //透明状态栏
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams mWindowAttributes = getDialog().getWindow().getAttributes();
        mWindowAttributes.width = ScreenUtils.getScreenWidth();
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });

        View view = inflater.inflate(R.layout.dialog_networkloading, container, false);
        if (!TextUtils.isEmpty(getArguments().getString("loadingText"))) {
            ((TextView) view.findViewById(R.id.tv_networkloading)).setText(getArguments().getString("loadingText"));
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            isDismiss=savedInstanceState.getBoolean("isDismiss");
            FragmentActivity activity = getActivity();
            if (activity != null) {
                manager = activity.getSupportFragmentManager();
            }
            dismissDialog();
        }

        InputMethodManager manager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            View focusView=getActivity().getCurrentFocus();
            if (focusView!=null) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;
        window.setAttributes(windowParams);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    public void show(FragmentActivity fragmentActivity) {
        show(fragmentActivity, "loadingDialog");
    }

    public void show(FragmentActivity fragmentActivity, final String tag) {
        if (fragmentActivity.isDestroyed() || !isDismiss) {
            return;
        }
        manager = fragmentActivity.getSupportFragmentManager();
        new Handler().post(() -> {
            super.show(manager, tag);

            isDismiss=false;
        });
    }

    public void close() {
        dismissDialog();
        if (onDialogDismissListener != null) {
            onDialogDismissListener.onDismiss();
        }
    }

    /**
     * 带有文字提示
     */
    public void closeWithText(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        close();
    }

    /**
     * 带有图文提示
     */
    public void closeWithTextAndImage(String text, int imageRes) {
        Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) (toast.getView());
        ImageView image = new ImageView(getActivity().getApplicationContext());
        image.setImageResource(imageRes);
        toastView.addView(image, 0);
        toast.show();
        close();
    }

    private void dismissDialog() {
        new Handler().post(() -> {
            if (isDismiss) {
                return;
            }
            isDismiss=true;
            try {
                dismissAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
