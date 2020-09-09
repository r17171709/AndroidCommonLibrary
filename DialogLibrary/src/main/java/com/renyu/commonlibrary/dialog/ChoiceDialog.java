package com.renyu.commonlibrary.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.renyu.commonlibrary.dialog.utils.Utils;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/2/28 0028.
 */

public class ChoiceDialog extends DialogFragment {

    private TextView choice_container_content;
    private TextView choice_container_title;
    private Button choice_container_positive;
    private Button choice_container_negative;
    private ProgressBar choice_container_pb;
    private View choice_container_line;

    private boolean isDismiss = true;
    private FragmentManager manager = null;
    // 是否由手动触发关闭产生
    private boolean isHandlerDismiss = false;

    public interface OnDialogDismiss {
        void onDismiss();
    }

    public interface OnDialogPos {
        void onPos();
    }

    public interface OnDialogNeg {
        void onNeg();
    }

    private OnDialogDismiss onDialogDismissListener;
    private OnDialogPos onDialogPosListener;
    private OnDialogNeg onDialogNegListener;

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
     *
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
     * 选择弹出框 title+content
     *
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
     *
     * @param content
     * @param pos
     * @return
     */
    public static ChoiceDialog getInstanceByTextCommit(String content, String pos) {
        ChoiceDialog dialog = new ChoiceDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 12);
        bundle.putString("content", content);
        bundle.putString("pos", pos);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 进度条框
     *
     * @param title
     * @param neg
     * @return
     */
    public static ChoiceDialog getInstanceByPB(String title, String neg) {
        ChoiceDialog dialog = new ChoiceDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putString("title", title);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }

    // 不需要再getActivity()了
    public Context context;

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
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });

        View view = inflater.inflate(R.layout.dialog_choice, container, false);
        choice_container_content = view.findViewById(R.id.choice_container_content);
        choice_container_title = view.findViewById(R.id.choice_container_title);
        choice_container_positive = view.findViewById(R.id.choice_container_positive);
        choice_container_positive.setOnClickListener(v -> {
            if (onDialogPosListener != null) {
                onDialogPosListener.onPos();
            }
            isHandlerDismiss = true;
            dismissDialog();
        });
        choice_container_negative = view.findViewById(R.id.choice_container_negative);
        choice_container_negative.setOnClickListener(v -> {
            if (onDialogNegListener != null) {
                onDialogNegListener.onNeg();
            }
            isHandlerDismiss = true;
            dismissDialog();
        });
        choice_container_pb = view.findViewById(R.id.choice_container_pb);
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
        // 设置进度条框
        else if (getArguments().getInt("type") == 1) {
            choice_container_title.setText(getArguments().getString("title"));
            choice_container_content.setVisibility(View.GONE);
            choice_container_line.setVisibility(View.GONE);
            choice_container_positive.setVisibility(View.GONE);
            choice_container_negative.setText(getArguments().getString("neg"));
            choice_container_pb.setVisibility(View.VISIBLE);
        }
        choice_container_content.post(() -> {
            if (choice_container_content.getLineCount() > 1) {
                choice_container_content.setGravity(Gravity.LEFT);
            }
        });
        return view;
    }

    /**
     * 更新进度
     *
     * @param num
     */
    public void setPb(int num) {
        choice_container_pb.setProgress(num);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            isDismiss = savedInstanceState.getBoolean("isDismiss");
            FragmentActivity activity = (FragmentActivity) context;
            if (activity != null) {
                manager = activity.getSupportFragmentManager();
            }
            dismissDialog();
        }

        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            View focusView = ((FragmentActivity) context).getCurrentFocus();
            if (focusView != null) {
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
        if (onDialogDismissListener != null && isHandlerDismiss) {
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

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(this, tag);
        transaction.commitAllowingStateLoss();

        isDismiss = false;
    }

    private void dismissDialog() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        try {
            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
