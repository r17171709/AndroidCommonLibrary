package com.renyu.commonlibrary.update.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.*;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.renyu.commonlibrary.update.R;
import com.renyu.commonlibrary.update.bean.UpdateModel;
import com.renyu.commonlibrary.update.params.InitParams;
import com.renyu.commonlibrary.update.service.UpdateService;
import com.renyu.commonlibrary.update.utils.RxBus;
import com.renyu.commonlibrary.update.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import java.io.File;

/**
 * Created by renyu on 16/1/23.
 */
public class AppUpdateDialogFragment extends DialogFragment {

    private TextView custom_title;
    private TextView custom_content;
    private Button custom_negativeButton;
    private Button custom_positiveButton;
    private ProgressBar custom_pb;
    private RelativeLayout custom_pblayout;
    private TextView custom_pblayout_readsize;
    private TextView custom_pblayout_totalsize;
    private TextView custom_pblayout_progress;

    // 不需要再getActivity()了
    public Context context;

    private boolean isCanCancel;

    private UpdateModel model;
    // 下载通知ID
    private int ids;
    // 最后一次下载值
    private int lastProgressNum = 0;
    // 是否为首次刷新
    private boolean isFirstRefresh = true;

    private FragmentManager manager;

    // 是否已经关闭
    private boolean isDismiss = true;

    // 强制升级接口
    private OnMandatoryUpdateListener mandatoryUpdateListener;

    public interface OnMandatoryUpdateListener {
        void something();
    }

    public void setOnMandatoryUpdateListener(OnMandatoryUpdateListener mandatoryUpdateListener) {
        this.mandatoryUpdateListener = mandatoryUpdateListener;
    }

    // 升级弹窗关闭接口
    private OnDismissListener dismissListener;

    public interface OnDismissListener {
        void dismissFragment();
    }

    public void setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    // RxBus监听
    private CompositeDisposable compositeDisposable;

    public static AppUpdateDialogFragment getInstance(UpdateModel model, int ids, int smallIcon, int largeIcon) {
        AppUpdateDialogFragment fragment = new AppUpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putInt("ids", ids);
        bundle.putInt("smallIcon", smallIcon);
        bundle.putInt("largeIcon", largeIcon);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected void onAttachToContext(Context context) {
        this.context = context;
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
        mWindowAttributes.width = Utils.getScreenWidth(getContext());
        mWindowAttributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener((dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK);

        compositeDisposable = new CompositeDisposable();

        ids = getArguments().getInt("ids");
        model = (UpdateModel) getArguments().getSerializable("model");
        isCanCancel = model.getForced() != 1;

        View view = inflater.inflate(R.layout.view_material_dialogs, container, false);
        custom_title = view.findViewById(R.id.custom_title);
        custom_title.setText("发现新版本");
        custom_content = view.findViewById(R.id.custom_content);
        custom_content.setText(model.getContent().replace("\\n", "\n"));
        custom_negativeButton = view.findViewById(R.id.custom_negativeButton);
        if (isCanCancel) {
            custom_negativeButton.setVisibility(View.VISIBLE);
        } else {
            custom_negativeButton.setVisibility(View.GONE);
        }
        custom_negativeButton.setText("以后再说");
        custom_negativeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateService.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", model.getUrl());
            bundle.putBoolean("download", false);
            intent.putExtras(bundle);
            if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                getActivity().startForegroundService(intent);
            } else {
                getActivity().startService(intent);
            }

            if (isCanCancel) {
                dismissDialog();
            } else {
                if (mandatoryUpdateListener != null) {
                    mandatoryUpdateListener.something();
                } else {
                    throw new RuntimeException("必须实现强制升级接口");
                }
            }
        });
        custom_positiveButton = view.findViewById(R.id.custom_positiveButton);
        custom_positiveButton.setVisibility(View.VISIBLE);
        custom_pb = view.findViewById(R.id.custom_pb);
        custom_pblayout = view.findViewById(R.id.custom_pblayout);
        custom_pblayout_readsize = view.findViewById(R.id.custom_pblayout_readsize);
        custom_pblayout_totalsize = view.findViewById(R.id.custom_pblayout_totalsize);
        custom_pblayout_progress = view.findViewById(R.id.custom_pblayout_progress);
        normalClick();

        //正在执行更新操作
        if (UpdateService.downloadUrls.contains(model.getUrl())) {
            //直接设置成下载时的样式
            if (isCanCancel) {
                custom_positiveButton.setText("后台下载");
                custom_positiveButton.setOnClickListener(v -> {
                    dismissDialog();
                });
                custom_positiveButton.setVisibility(View.VISIBLE);
                custom_negativeButton.setVisibility(View.VISIBLE);
            } else {
                custom_positiveButton.setEnabled(false);
                custom_positiveButton.setText("正在下载");
                custom_positiveButton.setVisibility(View.GONE);
                custom_negativeButton.setVisibility(View.GONE);
            }
            custom_pb.setVisibility(View.VISIBLE);
            custom_pblayout.setVisibility(View.VISIBLE);
            custom_content.setVisibility(View.GONE);
            custom_pb.setProgress(0);
            custom_negativeButton.setText("取消");
        }
        //如果在下载过程中，不能对文件进行读写操作，否则会下载失败
        else {
            File file = fileExists(model);
            if (file != null) {
                custom_positiveButton.setText("安装");
            } else {
                custom_positiveButton.setText("确定");
            }
        }
        return view;
    }

    private void normalClick() {
        custom_positiveButton.setOnClickListener(v -> {
            // 出现严重错误时跳转浏览器
            if (!TextUtils.isEmpty(model.getFatalErrorUrl())) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(model.getFatalErrorUrl());
                intent.setData(content_url);
                startActivity(intent);
                return;
            }
            File file = fileExists(model);
            if (file != null) {
                startActivity(Utils.install(getActivity(), file.getPath()));
                if (isCanCancel) {
                    dismissDialog();
                }
            } else {
                if (!UpdateService.downloadUrls.contains(model.getUrl())) {
                    Intent intent = new Intent(getActivity(), UpdateService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", model.getUrl());
                    bundle.putBoolean("download", true);
                    bundle.putInt("ids", ids);
                    bundle.putString("name", model.getNotificationTitle());
                    bundle.putInt("smallIcon", getArguments().getInt("smallIcon"));
                    bundle.putInt("largeIcon", getArguments().getInt("largeIcon"));
                    intent.putExtras(bundle);
                    if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                        getActivity().startForegroundService(intent);
                    } else {
                        getActivity().startService(intent);
                    }
                    isFirstRefresh = true;

                    //直接设置成下载时的样式
                    if (isCanCancel) {
                        custom_positiveButton.setText("后台下载");
                        custom_positiveButton.setOnClickListener(v1 -> dismissDialog());
                        custom_positiveButton.setVisibility(View.VISIBLE);
                        custom_negativeButton.setVisibility(View.VISIBLE);
                    } else {
                        custom_positiveButton.setEnabled(false);
                        custom_positiveButton.setText("正在下载");
                        custom_positiveButton.setVisibility(View.GONE);
                        custom_negativeButton.setVisibility(View.GONE);
                    }
                    custom_pb.setVisibility(View.VISIBLE);
                    custom_pblayout.setVisibility(View.VISIBLE);
                    custom_content.setVisibility(View.GONE);
                    custom_pb.setProgress(0);
                    custom_pblayout_readsize.setText("");
                    custom_pblayout_totalsize.setText("");
                    custom_pblayout_progress.setText("");
                    lastProgressNum = 0;
                    custom_negativeButton.setText("取消");
                }
            }
        });
    }

    private File fileExists(UpdateModel model) {
        File file;
        String url = model.getUrl();
        if (url.indexOf("?") != -1) {
            String url_ = url.substring(0, url.indexOf("?"));
            file = new File(InitParams.FILE_PATH + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
        } else {
            file = new File(InitParams.FILE_PATH + File.separator + url.substring(url.lastIndexOf("/") + 1));
        }
        if (file.exists() && Utils.checkAPKState(getActivity(), file.getPath())) {
            return file;
        }
        return null;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.dismissFragment();
        }
    }

    public void show(FragmentActivity fragmentActivity) {
        show(fragmentActivity, "update");
    }

    public void show(FragmentActivity fragmentActivity, final String tag) {
        if (fragmentActivity.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss = false;
        manager = fragmentActivity.getSupportFragmentManager();
        new Handler().post(() -> {
            super.show(manager, tag);

            isDismiss = false;
        });
    }

    private void dismissDialog() {
        new Handler().post(() -> {
            if (isDismiss) {
                return;
            }
            isDismiss = true;
            try {
                dismissAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isDismiss", isDismiss);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            isDismiss = savedInstanceState.getBoolean("isDismiss");
            FragmentActivity activity = getActivity();
            if (activity != null) {
                manager = activity.getSupportFragmentManager();
            }
            try {
                dismissDialog();
            } catch (Exception e) {

            }
        }
        compositeDisposable.add(RxBus.getDefault()
                .toObservable(UpdateModel.class)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::onEventMainThread)
                .subscribe());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        compositeDisposable.clear();
    }

    private void onEventMainThread(UpdateModel model) {
        if (!model.getUrl().equals(AppUpdateDialogFragment.this.model.getUrl())) {
            return;
        }
        if (model.getState() == UpdateModel.State.DOWNLOADING) {
            if (isFirstRefresh) {
                isFirstRefresh = false;
                if (isCanCancel) {
                    custom_positiveButton.setText("后台下载");
                    custom_positiveButton.setOnClickListener(v -> {
                        dismissDialog();
                    });
                    custom_positiveButton.setVisibility(View.VISIBLE);
                    custom_negativeButton.setVisibility(View.VISIBLE);
                } else {
                    custom_positiveButton.setEnabled(false);
                    custom_positiveButton.setText("正在下载");
                    custom_positiveButton.setVisibility(View.GONE);
                    custom_negativeButton.setVisibility(View.GONE);
                }
                custom_pb.setVisibility(View.VISIBLE);
                custom_pblayout.setVisibility(View.VISIBLE);
                custom_content.setVisibility(View.GONE);
            }

            if (model.getProcess() - lastProgressNum > 5 || model.getProcess() == 100) {
                lastProgressNum = model.getProcess();

                custom_pb.setProgress(model.getProcess());
                custom_pblayout_readsize.setText(Utils.bytes2mb(model.getBytesRead()) + "/");
                custom_pblayout_totalsize.setText(Utils.bytes2mb(model.getContentLength()));
                custom_pblayout_progress.setText("已完成" + model.getProcess() + "%");
                custom_negativeButton.setText("取消");
            }
        } else if (model.getState() == UpdateModel.State.SUCCESS) {
            custom_positiveButton.setEnabled(true);
            custom_pb.setVisibility(View.GONE);
            custom_pblayout.setVisibility(View.GONE);
            custom_content.setVisibility(View.VISIBLE);
            custom_content.setText(AppUpdateDialogFragment.this.model.getContent().replace("\\n", "\n"));
            custom_positiveButton.setText("安装");
            custom_positiveButton.setVisibility(View.VISIBLE);
            custom_negativeButton.setText("以后再说");
            if (isCanCancel) {
                custom_negativeButton.setVisibility(View.VISIBLE);
            } else {
                custom_negativeButton.setVisibility(View.GONE);
            }
            normalClick();
            if (isCanCancel) {
                dismissDialog();
            }
        } else if (model.getState() == UpdateModel.State.FAIL) {
            custom_positiveButton.setEnabled(true);
            custom_pb.setVisibility(View.GONE);
            custom_pblayout.setVisibility(View.GONE);
            custom_content.setText("下载失败");
            custom_content.setVisibility(View.VISIBLE);
            custom_positiveButton.setText("重新下载");
            custom_positiveButton.setVisibility(View.VISIBLE);
            custom_negativeButton.setText("取消");
            if (isCanCancel) {
                custom_negativeButton.setVisibility(View.VISIBLE);
            } else {
                custom_negativeButton.setVisibility(View.GONE);
            }
            normalClick();
        }
    }
}
