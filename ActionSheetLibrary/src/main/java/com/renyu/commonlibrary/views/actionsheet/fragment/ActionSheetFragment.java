package com.renyu.commonlibrary.views.actionsheet.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.blankj.utilcode.util.ScreenUtils;
import com.renyu.commonlibrary.views.utils.NavigationBarUtils;
import com.renyu.commonlibrary.views.wheelview.R;

public abstract class ActionSheetFragment extends Fragment {
    OnCancelListener onCancelListener;
    OnOKListener onOKListener;

    abstract View initViews(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    abstract void initParams();

    private View decorView;
    // 添加进入的view
    View realView;
    private View pop_child_layout;
    private TextView pop_title;
    String title;
    TextView pop_cancel1;
    String cancelTitle;
    TextView pop_ok1;
    String okTitle;

    // 是否已经关闭
    private boolean isDismiss = true;
    private FragmentManager manager = null;

    // 不需要再getActivity()了
    Context context;

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

    private void onAttachToContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() == null)
            return super.onCreateView(inflater, container, savedInstanceState);

        realView = initViews(inflater, container, savedInstanceState);
        pop_child_layout = realView.findViewById(R.id.pop_child_layout);
        pop_child_layout.setVisibility(View.INVISIBLE);
        pop_child_layout.setOnTouchListener((v, event) -> true);
        realView.setOnClickListener(v -> dismiss());

        title = getArguments().getString("title");
        pop_title = realView.findViewById(R.id.pop_title);
        if (getArguments().getInt("titleColor", -1) != -1) {
            pop_title.setTextColor(getArguments().getInt("titleColor"));
        }
        if (TextUtils.isEmpty(title)) {
            pop_title.setVisibility(View.GONE);
        } else {
            pop_title.setText(title);
        }

        cancelTitle = getArguments().getString("cancelTitle");
        pop_cancel1 = realView.findViewById(R.id.pop_cancel1);
        if (getArguments().getInt("cancelTitleColor", -1) != -1) {
            pop_cancel1.setTextColor(getArguments().getInt("cancelTitleColor"));
        }
        if (!TextUtils.isEmpty(cancelTitle)) {
            pop_cancel1.setText(cancelTitle);
        }

        okTitle = getArguments().getString("okTitle");
        pop_ok1 = realView.findViewById(R.id.pop_ok1);
        if (getArguments().getInt("okTitleColor", -1) != -1) {
            pop_ok1.setTextColor(getArguments().getInt("okTitleColor"));
        }
        if (!TextUtils.isEmpty(okTitle)) {
            pop_ok1.setText(okTitle);
        }

        initParams();

        decorView = ((FragmentActivity) context).getWindow().getDecorView();
        ((ViewGroup) decorView).addView(realView);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 发生回收，即销毁弹出层
        if (savedInstanceState != null) {
            isDismiss = savedInstanceState.getBoolean("isDismiss");
            FragmentActivity activity = (FragmentActivity) context;
            if (activity != null) {
                manager = activity.getSupportFragmentManager();
            }
            try {
                dismiss();
            } catch (Exception e) {

            }
            return;
        }

        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            View focusView = ((FragmentActivity) context).getCurrentFocus();
            if (focusView != null) {
                manager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }

        startPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopPlay();
        new Handler().postDelayed(() -> ((ViewGroup) decorView).removeView(realView), 500);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    private void startPlay() {
        pop_child_layout.post(new Runnable() {
            @Override
            public void run() {
                final int moveHeight = pop_child_layout.getMeasuredHeight();
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(500);
                valueAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        pop_child_layout.setVisibility(View.VISIBLE);
                    }
                });
                valueAnimator.addUpdateListener(animation -> {
                    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
                    realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#00000000"), Color.parseColor("#70000000")));
                    //当底部存在导航栏并且decorView获取的高度不包含底部状态栏的时候，需要去掉这个高度差
                    if (NavigationBarUtils.getNavBarHeight((FragmentActivity) context) > 0) {
                        pop_child_layout.setTranslationY((moveHeight + NavigationBarUtils.getNavBarHeight((FragmentActivity) context)) * (1 - animation.getAnimatedFraction()) - NavigationBarUtils.getNavBarHeight((FragmentActivity) context));
                    } else {
                        pop_child_layout.setTranslationY(moveHeight * (1 - animation.getAnimatedFraction()));
                    }
                });
                valueAnimator.start();
            }
        });
    }

    private void stopPlay() {
        pop_child_layout.post(() -> {
            final int moveHeight = pop_child_layout.getMeasuredHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(animation -> {
                ArgbEvaluator argbEvaluator = new ArgbEvaluator();

                realView.setBackgroundColor((Integer) argbEvaluator.evaluate(animation.getAnimatedFraction(), Color.parseColor("#70000000"), Color.parseColor("#00000000")));
                if (NavigationBarUtils.getNavBarHeight((FragmentActivity) context) > 0 && decorView.getMeasuredHeight() != ScreenUtils.getScreenHeight()) {
                    pop_child_layout.setTranslationY((moveHeight + NavigationBarUtils.getNavBarHeight((FragmentActivity) context)) * animation.getAnimatedFraction() - NavigationBarUtils.getNavBarHeight((FragmentActivity) context));
                } else {
                    pop_child_layout.setTranslationY(moveHeight * animation.getAnimatedFraction());
                }
            });
            valueAnimator.start();
        });
    }

    void show(FragmentActivity fragmentActivity, final String tag) {
        if (fragmentActivity.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss = false;
        manager = fragmentActivity.getSupportFragmentManager();
        new Handler().post(() -> {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(this, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        });
    }

    public void dismiss() {
        try {
            if (isDismiss) {
                return;
            }
            isDismiss = true;
            if (context != null && ((FragmentActivity) context).isFinishing()) {
                return;
            }
            new Handler().post(() -> {
                if (manager != null) {
                    manager.popBackStack();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.remove(this);
                    transaction.commitAllowingStateLoss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    void setOnOKListener(OnOKListener onOKListener) {
        this.onOKListener = onOKListener;
    }

    public interface OnCancelListener {
        void onCancelClick();
    }

    public interface OnOKListener {
        void onOKClick(Object value);
    }
}
