package com.renyu.commonlibrary.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.KeyboardUtils;
import com.renyu.commonlibrary.dialog.utils.Utils;
import com.renyu.commonlibrary.dialog.view.VerificationCodeInput;

import java.lang.reflect.Field;

public class CodeDialog extends DialogFragment {

    // 不需要再getActivity()了
    public Context context;

    // 自定义视图
    View customerView;
    // 自定义EditText输入类型
    VerificationCodeInput.VerificationCodeInputType inputType;
    // 距离顶部的边距
    int marginTop;
    // box数量
    int boxNum = -1;

    boolean isDismiss = true;
    FragmentManager manager = null;

    public interface OnCodeListener {
        void getCode(String code);
    }

    OnCodeListener onCodeListener;

    public CodeDialog setOnCodeListener(OnCodeListener onCodeListener) {
        this.onCodeListener = onCodeListener;
        return this;
    }

    public CodeDialog setCustomerView(View customerView) {
        this.customerView = customerView;
        return this;
    }

    public CodeDialog setVarificationInputType(VerificationCodeInput.VerificationCodeInputType inputType) {
        this.inputType = inputType;
        return this;
    }

    public CodeDialog setMarginTop(int marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    public CodeDialog setBoxNum(int boxNum) {
        this.boxNum = boxNum;
        return this;
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onAttachToContext(context);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        this.context = context;
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
        mWindowAttributes.width = Utils.getScreenWidth(context);
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowAttributes.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });

        View view = inflater.inflate(R.layout.dialog_code, container, false);
        VerificationCodeInput code_vertication_input = view.findViewById(R.id.code_vertication_input);
        if (boxNum > 0) {
            code_vertication_input.setBox(boxNum);
        }
        if (customerView != null) {
            LinearLayout code_vertication_customerview = view.findViewById(R.id.code_vertication_customerview);
            code_vertication_customerview.setVisibility(View.VISIBLE);
            code_vertication_customerview.removeAllViews();
            code_vertication_customerview.addView(customerView);

        }
        code_vertication_input.setVarificationInputType(inputType);
        code_vertication_input.setOnCompleteListener(content -> new Handler().postDelayed(() -> {
            KeyboardUtils.hideSoftInput((Activity) context);

            dismissDialog();
            onCodeListener.getCode(content);
        }, 500));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VerificationCodeInput code_vertication_input = view.findViewById(R.id.code_vertication_input);
        code_vertication_input.post(() -> KeyboardUtils.showSoftInput(code_vertication_input.getChildAt(0)));
        View code_vertication_space = view.findViewById(R.id.code_vertication_space);
        code_vertication_space.post(() -> {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) code_vertication_space.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, marginTop);
            }
            else {
                params.height = marginTop;
            }
            code_vertication_space.setLayoutParams(params);
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null) {
            isDismiss=savedInstanceState.getBoolean("isDismiss");
            FragmentActivity activity = (FragmentActivity) context;
            if (activity != null) {
                manager = activity.getSupportFragmentManager();
            }
            dismissDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    public void show(FragmentActivity fragmentActivity) {
        show(fragmentActivity, "codeDialog");
    }

    public void show(FragmentActivity fragmentActivity, final String tag) {
        if (fragmentActivity.isDestroyed() || !isDismiss) {
            return;
        }
        manager = fragmentActivity.getSupportFragmentManager();
        try {
            Field fieldDismissed = DialogFragment.class.getDeclaredField("mDismissed");
            fieldDismissed.setAccessible(true);
            fieldDismissed.setBoolean(this, false);

            Field fieldShownByMe = DialogFragment.class.getDeclaredField("mShownByMe");
            fieldShownByMe.setAccessible(true);
            fieldShownByMe.setBoolean(this, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(this, tag);
        transaction.commitAllowingStateLoss();

        isDismiss=false;
    }

    private void dismissDialog() {
        if (isDismiss) {
            return;
        }
        isDismiss=true;
        try {
            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
