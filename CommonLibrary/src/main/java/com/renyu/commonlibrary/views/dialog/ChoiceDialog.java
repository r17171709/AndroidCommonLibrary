package com.renyu.commonlibrary.views.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.renyu.commonlibrary.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class ChoiceDialog extends DialogFragment {

    TextView choice_container_content;
    TextView choice_container_title;
    Button choice_container_positive;
    Button choice_container_negative;
    View choice_container_line;

    boolean isDismiss = true;
    FragmentManager manager = null;

    private ChoiceDialog() {

    }

    public interface OnDialogDismiss {
        void onDismiss();
    }

    public interface OnDialogPos {
        void onPos();
    }

    public interface OnDialogNeg {
        void onNeg();
    }

    OnDialogDismiss onDialogDismissListener;
    OnDialogPos onDialogPosListener;
    OnDialogNeg onDialogNegListener;

    public void setOnDialogDismissListener(OnDialogDismiss onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }

    public void setOnDialogPosListener(OnDialogPos onDialogPosListener) {
        this.onDialogPosListener = onDialogPosListener;
    }

    public void setOnDialogNegListener(OnDialogNeg onDialogNegListener) {
        this.onDialogNegListener = onDialogNegListener;
    }

    /**
     * 選擇弹出框
     * @param content
     * @param pos
     * @param neg
     * @return
     */
    public static ChoiceDialog getInstanceByChoice(String content, String pos, String neg) {
        ChoiceDialog dialog = new ChoiceDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putString("content", content);
        bundle.putString("pos", pos);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 選擇弹出框 title+content
     * @param title
     * @param content
     * @param pos
     * @param neg
     * @return
     */
    public static ChoiceDialog getInstanceByChoiceWithTitle(String title, String content, String pos, String neg) {
        ChoiceDialog dialog = new ChoiceDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 6);
        bundle.putString("content", content);
        bundle.putString("title", title);
        bundle.putString("pos", pos);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 确定弹出框
     * @param content
     * @param pos
     * @return
     */
    public static ChoiceDialog getInstanceByTextCommit(String content, String pos) {
        ChoiceDialog dialog=new ChoiceDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("type", 12);
        bundle.putString("content", content);
        bundle.putString("pos", pos);
        dialog.setArguments(bundle);
        return dialog;
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

        View view = inflater.inflate(R.layout.dialog_choice, container, false);
        choice_container_content = (TextView) view.findViewById(R.id.choice_container_content);
        choice_container_title = (TextView) view.findViewById(R.id.choice_container_title);
        choice_container_positive = (Button) view.findViewById(R.id.choice_container_positive);
        choice_container_positive.setOnClickListener(v -> {
            if (onDialogPosListener != null) {
                onDialogPosListener.onPos();
            }
            dismissDialog();
        });
        choice_container_negative = (Button) view.findViewById(R.id.choice_container_negative);
        choice_container_negative.setOnClickListener(v -> {
            if (onDialogNegListener != null) {
                onDialogNegListener.onNeg();
            }
            dismissDialog();
        });
        choice_container_line = view.findViewById(R.id.choice_container_line);
        // 设置选择对话框
        if (getArguments().getInt("type") == 4) {
            choice_container_title.setVisibility(View.GONE);
            choice_container_content.setText(getArguments().getString("content"));
            choice_container_positive.setText(getArguments().getString("pos"));
            choice_container_negative.setText(getArguments().getString("neg"));
        }
        // 设置带标题的选择对话框
        else if (getArguments().getInt("type") == 6) {
            choice_container_title.setText(getArguments().getString("title"));
            choice_container_content.setText(getArguments().getString("content"));
            choice_container_positive.setText(getArguments().getString("pos"));
            choice_container_negative.setText(getArguments().getString("neg"));
        }
        // 设置确认对话框
        else if (getArguments().getInt("type") == 12) {
            choice_container_title.setVisibility(View.GONE);
            choice_container_line.setVisibility(View.GONE);
            choice_container_negative.setVisibility(View.GONE);
            choice_container_content.setText(getArguments().getString("content"));
            choice_container_positive.setText(getArguments().getString("pos"));
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDialogDismissListener != null) {
            onDialogDismissListener.onDismiss();
        }
    }

    public void show(FragmentActivity fragmentActivity) {
        show(fragmentActivity, "loadingDialog");
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
