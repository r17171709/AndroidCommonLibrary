package com.renyu.commonlibrary.views.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.renyu.commonlibrary.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by RG on 2016/4/11.
 */
public class LoadingDialog extends DialogFragment {

    FragmentManager fragmentManager;
    boolean isDismiss = true;

    RelativeLayout loading_container;
    TextView loading_container_cancel;
    ProgressBar loading_container_progressbar;
    TextView loading_container_tips;

    RelativeLayout choice_container;
    TextView choice_container_content;
    TextView choice_container_title;
    Button choice_container_positive;
    Button choice_container_negative;
    View choice_container_line;

    LinearLayout toast_textimage_container;
    ImageView toast_textimage_container_image;
    TextView toast_textimage_container_content;

    LinearLayout toast_text_container;
    TextView toast_text_container_content;

    LinearLayout toast_titletext_container;
    ImageView toast_titletext_container_image;
    TextView toast_titletext_container_title;
    TextView toast_titletext_container_content;

    RelativeLayout customer_container;

    // 自定义的View
    View customerView;

    //是否需要关闭
    boolean isChoiceNeedClose = false;

    public interface OnDialogCancel {
        void onCancel();
    }

    public interface OnDialogStart {
        void onStart();
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

    public interface OnDialogProceed {
        void proceed();
    }

    public interface OnDialogCreate {
        void create(View view);
    }

    OnDialogCancel onDialogCancelListener;
    OnDialogStart onDialogStartListener;
    OnDialogDismiss onDialogDismissListener;
    OnDialogPos onDialogPosListener;
    OnDialogNeg onDialogNegListener;
    OnDialogProceed onDialogProceedListener;
    OnDialogCreate onDialogCreate;

    public void setOnDialogCancelListener(OnDialogCancel onDialogCancelListener) {
        this.onDialogCancelListener = onDialogCancelListener;
    }

    public void setOnDialogStartListener(OnDialogStart onDialogStartListener) {
        this.onDialogStartListener = onDialogStartListener;
    }

    public void setOnDialogDismissListener(OnDialogDismiss onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }

    public void setOnDialogPosListener(OnDialogPos onDialogPosListener) {
        this.onDialogPosListener = onDialogPosListener;
    }

    public void setOnDialogNegListener(OnDialogNeg onDialogNegListener) {
        this.onDialogNegListener = onDialogNegListener;
    }

    public void setOnDialogProceedListener(OnDialogProceed onDialogProceedListener) {
        this.onDialogProceedListener = onDialogProceedListener;
    }

    public void setOnDialogCreate(OnDialogCreate onDialogCreate) {
        this.onDialogCreate = onDialogCreate;
    }

    public static LoadingDialog getInstance(int type) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 仿Toast弹出框
     * @param text
     * @return
     */
    public static LoadingDialog getInstance_TextSuccessWithOutClose(String text) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 7);
        bundle.putString("text", text);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 仿Toast弹出框
     * @param text
     * @return
     */
    public static LoadingDialog getInstance_TextSuccessWithClose(String text) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 8);
        bundle.putString("text", text);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static LoadingDialog getInstance_TextImageSuccessWithOutClose(String title, int image) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 9);
        bundle.putString("title", title);
        bundle.putInt("image", image);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static LoadingDialog getInstance_TextImageSuccessWithClose(String title, int image) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 10);
        bundle.putString("title", title);
        bundle.putInt("image", image);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 選擇弹出框
     * @param title
     * @param pos
     * @param neg
     * @return
     */
    public static LoadingDialog getInstance_NoCloseChoice(String title, String pos, String neg) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 3);
        bundle.putString("title", title);
        bundle.putString("pos", pos);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 選擇弹出框
     * @param title
     * @param pos
     * @param neg
     * @return
     */
    public static LoadingDialog getInstance_CloseChoice(String title, String pos, String neg) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 4);
        bundle.putString("title", title);
        bundle.putString("pos", pos);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 弹出框 title+content
     * @param title
     * @param content
     * @param pos
     * @param neg
     * @return
     */
    public static LoadingDialog getInstance_NoCloseChoiceWithTitle(String title, String content, String pos, String neg) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 5);
        bundle.putString("content", content);
        bundle.putString("title", title);
        bundle.putString("pos", pos);
        bundle.putString("neg", neg);
        dialog.setArguments(bundle);
        return dialog;
    }


    /**
     * 弹出框 title+content
     * @param title
     * @param content
     * @param pos
     * @param neg
     * @return
     */
    public static LoadingDialog getInstance_CloseChoiceWithTitle(String title, String content, String pos, String neg) {
        LoadingDialog dialog = new LoadingDialog();
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
     * 弹出框 title+content+img
     * @param title
     * @param content
     * @param image
     * @return
     */
    public static LoadingDialog getInstance_TitleImageContent(String title, String content, int image) {
        LoadingDialog dialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 11);
        bundle.putString("content", content);
        bundle.putString("title", title);
        bundle.putInt("image", image);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 确定弹出框
     * @param content
     * @param pos
     * @return
     */
    public static LoadingDialog getInstance_TextCommit(String content, String pos) {
        LoadingDialog dialog=new LoadingDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("type", 12);
        bundle.putString("content", content);
        bundle.putString("pos", pos);
        dialog.setArguments(bundle);
        return dialog;
    }

    /**
     * 添加自定义的view
     * @param viewId
     * @return
     */
    public static LoadingDialog getInstance_CustomerView(int viewId) {
        LoadingDialog dialog=new LoadingDialog();
        Bundle bundle=new Bundle();
        bundle.putInt("type", 13);
        bundle.putInt("customerViewId", viewId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
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

        View view = inflater.inflate(R.layout.dialog_loading, container, false);

        loading_container = (RelativeLayout) view.findViewById(R.id.loading_container);
        loading_container_cancel = (TextView) view.findViewById(R.id.loading_container_cancel);
        loading_container_cancel.setOnClickListener(v -> {
            if (onDialogCancelListener != null) {
                onDialogCancelListener.onCancel();
            }
            try {
                dismiss();
            } catch (Exception e) {

            }
        });
        loading_container_progressbar= (ProgressBar) view.findViewById(R.id.loading_container_progressbar);
        loading_container_tips= (TextView) view.findViewById(R.id.loading_container_tips);

        choice_container = (RelativeLayout) view.findViewById(R.id.choice_container);
        choice_container_content = (TextView) view.findViewById(R.id.choice_container_content);
        choice_container_title = (TextView) view.findViewById(R.id.choice_container_title);
        choice_container_positive = (Button) view.findViewById(R.id.choice_container_positive);
        choice_container_positive.setOnClickListener(v -> {
            if (onDialogPosListener != null) {
                onDialogPosListener.onPos();
            }
            if (isChoiceNeedClose) {
                try {
                    dismiss();
                } catch (Exception e) {

                }
            }
        });
        choice_container_negative = (Button) view.findViewById(R.id.choice_container_negative);
        choice_container_negative.setOnClickListener(v -> {
            if (onDialogNegListener != null) {
                onDialogNegListener.onNeg();
            }
            try {
                dismiss();
            } catch (Exception e) {

            }
        });
        choice_container_line = view.findViewById(R.id.choice_container_line);

        toast_textimage_container = (LinearLayout) view.findViewById(R.id.toast_textimage_container);
        toast_textimage_container_image = (ImageView) view.findViewById(R.id.toast_textimage_container_image);
        toast_textimage_container_content = (TextView) view.findViewById(R.id.toast_textimage_container_content);

        toast_text_container = (LinearLayout) view.findViewById(R.id.toast_text_container);
        toast_text_container_content = (TextView) view.findViewById(R.id.toast_text_container_content);

        toast_titletext_container = (LinearLayout) view.findViewById(R.id.toast_titletext_container);
        toast_titletext_container_image = (ImageView) view.findViewById(R.id.toast_titletext_container_image);
        toast_titletext_container_title = (TextView) view.findViewById(R.id.toast_titletext_container_title);
        toast_titletext_container_content = (TextView) view.findViewById(R.id.toast_titletext_container_content);

        customer_container= (RelativeLayout) view.findViewById(R.id.customer_container);

        if (getArguments().getInt("type") == 1) {
            setloading();
        }
        if (getArguments().getInt("type") == 2) {
            setNetWorkError();
        }
        if (getArguments().getInt("type") == 3) {
            setNoCloseChoice(getArguments().getString("title"), getArguments().getString("pos"), getArguments().getString("neg"));
        }
        if (getArguments().getInt("type") == 4) {
            setCloseChoice(getArguments().getString("title"), getArguments().getString("pos"), getArguments().getString("neg"));
        }
        if (getArguments().getInt("type") == 5) {
            setNoCloseChoiceWithTitle(getArguments().getString("title"), getArguments().getString("content"), getArguments().getString("pos"), getArguments().getString("neg"));
        }
        if (getArguments().getInt("type") == 6) {
            setCloseChoiceWithTitle(getArguments().getString("title"), getArguments().getString("content"), getArguments().getString("pos"), getArguments().getString("neg"));
        }
        if (getArguments().getInt("type") == 7) {
            setTextSuccessWithOutClose(getArguments().getString("title"));
        }
        if (getArguments().getInt("type") == 8) {
            setTextSuccessWithClose(getArguments().getString("title"));
        }
        if (getArguments().getInt("type") == 9) {
            setTextImageSuccessWithOutClose(getArguments().getString("title"), getArguments().getInt("image"));
        }
        if (getArguments().getInt("type") == 10) {
            setTextImageSuccessWithClose(getArguments().getString("title"), getArguments().getInt("image"));
        }
        if (getArguments().getInt("type") == 11) {
            setTitleImageContent(getArguments().getString("title"), getArguments().getString("content"), getArguments().getInt("image"));
        }
        if (getArguments().getInt("type")==12) {
            setTextCommit(getArguments().getString("content"), getArguments().getString("pos"));
        }
        if (getArguments().getInt("type")==13) {
            setCustomer(getArguments().getInt("customerViewId"));
        }

        if (onDialogCreate!=null) {
            onDialogCreate.create(view);
        }
        return view;
    }

    /**
     * 图片+文字+底部描述
     *
     * @param title
     * @param content
     * @param image
     */
    public void setTitleImageContent(String title, String content, int image) {
        new Handler().post(() -> {
            closeAll();
            if (toast_titletext_container_image == null || toast_titletext_container_title == null || toast_titletext_container_content == null || toast_titletext_container == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            toast_titletext_container.setVisibility(View.VISIBLE);
            if (image==0) {
                toast_titletext_container_image.setVisibility(View.GONE);
            }
            else {
                toast_titletext_container_image.setImageResource(image);
            }
            if (TextUtils.isEmpty(title)) {
                toast_titletext_container_title.setVisibility(View.GONE);
            } else {
                toast_titletext_container_title.setText(title);
            }
            if (TextUtils.isEmpty(content)) {
                toast_titletext_container_content.setVisibility(View.GONE);
            } else {
                toast_titletext_container_content.setText(content);
            }
            Observable.timer(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    if (getFragmentManager() != null) {
                        try {
                            dismiss();
                        } catch (Exception e) {

                        }
                        if (onDialogDismissListener != null) {
                            onDialogDismissListener.onDismiss();
                        }
                    }
                }
            });
        });
    }

    /**
     * 设置耗时加载中
     */
    public void setloading() {
        closeAll();
        if (loading_container == null || loading_container_cancel == null) {
            return;
        }
        loading_container.setVisibility(View.VISIBLE);
        loading_container_cancel.setText("取消");
    }

    /**
     * 设置加载成功单文字并关闭
     * @param text
     */
    public void setTextSuccessWithOutClose(String text) {
        new Handler().post(() -> {
            closeAll();
            if (toast_text_container == null || toast_text_container_content == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            toast_text_container.setVisibility(View.VISIBLE);
            toast_text_container_content.setText(text);
            Observable.timer(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                if (getFragmentManager() != null) {
                    if (onDialogProceedListener != null) {
                        onDialogProceedListener.proceed();
                    }
                }
            });
        });
    }

    /**
     * 设置加载成功单文字并关闭
     * @param text
     * @param text
     */
    public void setTextSuccessWithClose(String text) {
        new Handler().post(() -> {
            closeAll();
            if (toast_text_container == null || toast_text_container_content == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            toast_text_container.setVisibility(View.VISIBLE);
            toast_text_container_content.setText(text);
            Observable.timer(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                try {
                    dismiss();
                } catch (Exception e) {

                }
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }
            });
        });
    }

    /**
     * 设置加载成功文字图片不关闭
     * @param text
     * @param image
     */
    public void setTextImageSuccessWithOutClose(String text, int image) {
        new Handler().post(() -> {
            closeAll();
            if (toast_textimage_container == null || toast_textimage_container_image == null || toast_textimage_container_content == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            toast_textimage_container.setVisibility(View.VISIBLE);
            if (image==0) {
                toast_textimage_container_image.setVisibility(View.GONE);
            }
            else {
                toast_textimage_container_image.setImageResource(image);
            }
            toast_textimage_container_content.setText(text);
            Observable.timer(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                if (getFragmentManager() != null) {
                    if (onDialogProceedListener != null) {
                        onDialogProceedListener.proceed();
                    }
                }
            });
        });
    }

    /**
     * 设置加载成功文字图片并关闭
     * @param text
     * @param image
     */
    public void setTextImageSuccessWithClose(String text, int image) {
        new Handler().post(() -> {
            closeAll();
            if (toast_textimage_container == null || toast_textimage_container_image == null || toast_textimage_container_content == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            toast_textimage_container.setVisibility(View.VISIBLE);
            if (image==0) {
                toast_textimage_container_image.setVisibility(View.GONE);
            }
            else {
                toast_textimage_container_image.setImageResource(image);
            }
            toast_textimage_container_content.setText(text);
            Observable.timer(2000, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                try {
                    dismiss();
                } catch (Exception e) {

                }
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }
            });
        });
    }

    /**
     * 设置网络异常对话框
     */
    public void setNetWorkError() {
        setNoCloseChoice("网络连接异常", "重试", "取消");
    }

    /**
     * 设置可以关闭的选择对话框
     * @param title
     * @param pos
     * @param neg
     */
    public void setCloseChoice(String title, String pos, String neg) {
        new Handler().post(() -> {
            closeAll();
            if (choice_container == null || choice_container_title==null || choice_container_content==null ||
                    choice_container_negative == null || choice_container_positive == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            choice_container.setVisibility(View.VISIBLE);
            choice_container_title.setVisibility(View.GONE);
            choice_container_content.setText(title);
            choice_container_positive.setText(pos);
            choice_container_negative.setText(neg);
            isChoiceNeedClose = true;
        });
    }

    /**
     * 设置不可以关闭的选择对话框
     * @param title
     * @param pos
     * @param neg
     */
    public void setNoCloseChoice(String title, String pos, String neg) {
        new Handler().post(() -> {
            closeAll();
            if (choice_container == null || choice_container_title==null || choice_container_content==null ||
                    choice_container_negative == null || choice_container_positive == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            choice_container.setVisibility(View.VISIBLE);
            choice_container_title.setVisibility(View.GONE);
            choice_container_content.setText(title);
            choice_container_positive.setText(pos);
            choice_container_negative.setText(neg);
            isChoiceNeedClose = false;
        });
    }

    /**
     * 设置带标题的不可以关闭的选择对话框
     * @param title
     * @param content
     * @param pos
     * @param neg
     */
    public void setNoCloseChoiceWithTitle(String title, String content, String pos, String neg) {
        new Handler().post(() -> {
            closeAll();
            if (choice_container == null || choice_container_title==null || choice_container_content==null ||
                    choice_container_negative == null || choice_container_positive == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            choice_container.setVisibility(View.VISIBLE);
            choice_container_title.setText(title);
            choice_container_content.setText(content);
            choice_container_positive.setText(pos);
            choice_container_negative.setText(neg);
            isChoiceNeedClose = false;
        });
    }

    /**
     * 设置带标题的可以关闭的选择对话框
     * @param title
     * @param content
     * @param pos
     * @param neg
     */
    public void setCloseChoiceWithTitle(String title, String content, String pos, String neg) {
        new Handler().post(() -> {
            closeAll();
            if (choice_container == null || choice_container_title==null || choice_container_content==null ||
                    choice_container_negative == null || choice_container_positive == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            choice_container.setVisibility(View.VISIBLE);
            choice_container_title.setText(title);
            choice_container_content.setText(content);
            choice_container_positive.setText(pos);
            choice_container_negative.setText(neg);
            isChoiceNeedClose = true;
        });
    }

    /**
     * 设置确认对话框
     * @param title
     * @param pos
     */
    public void setTextCommit(String title, String pos) {
        new Handler().post(() -> {
            closeAll();
            if (choice_container == null || choice_container_title==null || choice_container_content==null ||
                    choice_container_negative == null || choice_container_positive == null || choice_container_line == null) {
                return;
            }
            if (onDialogStartListener != null) {
                onDialogStartListener.onStart();
            }
            choice_container.setVisibility(View.VISIBLE);
            choice_container_title.setVisibility(View.GONE);
            choice_container_line.setVisibility(View.GONE);
            choice_container_negative.setVisibility(View.GONE);
            choice_container_content.setText(title);
            choice_container_positive.setText(pos);
            isChoiceNeedClose = true;
        });
    }

    public void setCustomer(int viewId) {
        closeAll();
        if (customer_container == null) {
            return;
        }
        if (onDialogStartListener != null) {
            onDialogStartListener.onStart();
        }
        customer_container.setVisibility(View.VISIBLE);
        customerView=LayoutInflater.from(getActivity()).inflate(viewId, null, false);
        customer_container.addView(customerView);
        isChoiceNeedClose = true;
    }

    public void setFinish() {
        try {
            dismiss();
        } catch (Exception e) {

        }
        if (onDialogDismissListener != null) {
            onDialogDismissListener.onDismiss();
        }
    }

    private void closeAll() {
        if (loading_container == null || choice_container == null || toast_textimage_container == null ||
                toast_text_container == null || toast_titletext_container == null || customer_container==null) {
            return;
        }
        loading_container.setVisibility(View.GONE);
        choice_container.setVisibility(View.GONE);
        toast_textimage_container.setVisibility(View.GONE);
        toast_text_container.setVisibility(View.GONE);
        toast_titletext_container.setVisibility(View.GONE);
        customer_container.setVisibility(View.GONE);
    }

    public void show(FragmentManager manager, final String tag) {
        this.fragmentManager=manager;
        if (manager.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss=false;
        new Handler().post(() -> {
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.add(LoadingDialog.this, tag);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        });
    }

    public void dismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss=true;
        new Handler().post(() -> {
            if (getActivity()!=null && getActivity().isFinishing()) {
                return;
            }
            fragmentManager.popBackStack();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.remove(LoadingDialog.this);
            transaction.commitAllowingStateLoss();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            isDismiss=savedInstanceState.getBoolean("isDismiss");
        }
    }

    public View getCustomerView() {
        return customerView;
    }
}
