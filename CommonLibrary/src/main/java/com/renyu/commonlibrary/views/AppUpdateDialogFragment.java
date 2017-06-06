package com.renyu.commonlibrary.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.bean.UpdateModel;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.service.UpdateService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

/**
 * Created by renyu on 16/1/23.
 */
public class AppUpdateDialogFragment extends DialogFragment {

    TextView custom_title;
    TextView custom_content;
    Button custom_negativeButton;
    Button custom_positiveButton;
    ProgressBar custom_pb;
    RelativeLayout custom_pblayout;
    TextView custom_pblayout_readsize;
    TextView custom_pblayout_totalsize;
    TextView custom_pblayout_progress;

    private boolean isCanCancel;

    private UpdateModel model;
    // 下载通知ID
    private int ids;
    // 最后一次下载值
    private int lastProgressNum = 0;
    // 是否为首次刷新
    private boolean isFirstRefresh=true;

    FragmentManager fragmentManager;

    // 是否已经关闭
    boolean isDismiss=true;

    // 强制升级接口
    OnMandatoryUpdateListener mandatoryUpdateListener;
    public interface OnMandatoryUpdateListener {
        void something();
    }
    public void setOnMandatoryUpdateListener(OnMandatoryUpdateListener mandatoryUpdateListener) {
        this.mandatoryUpdateListener = mandatoryUpdateListener;
    }

    // 升级弹窗关闭接口
    OnDismissListener dismissListener;
    public interface OnDismissListener {
        void dismissFragment();
    }
    public void setOnDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public static AppUpdateDialogFragment getInstance(UpdateModel model, int ids) {
        AppUpdateDialogFragment fragment = new AppUpdateDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putInt("ids", ids);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        ids = getArguments().getInt("ids");
        model = (UpdateModel) getArguments().getSerializable("model");
        isCanCancel = model.getForced() == 1 ? false : true;

        View view = inflater.inflate(R.layout.view_material_dialogs, container, false);
        custom_title = (TextView) view.findViewById(R.id.custom_title);
        custom_title.setText("发现新版本");
        custom_content = (TextView) view.findViewById(R.id.custom_content);
        custom_content.setText(model.getContent().replace("\\n", "\n"));
        custom_negativeButton = (Button) view.findViewById(R.id.custom_negativeButton);
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
            bundle.putInt("ids", ids);
            bundle.putString("name", model.getNotificationTitle());
            intent.putExtras(bundle);
            getActivity().startService(intent);

            if (isCanCancel) {
                dismiss();
            } else {
                if (mandatoryUpdateListener!=null) {
                    mandatoryUpdateListener.something();
                }
                else {
                    throw new RuntimeException("必须实现强制升级接口");
                }
            }
        });
        custom_positiveButton = (Button) view.findViewById(R.id.custom_positiveButton);
        custom_positiveButton.setVisibility(View.VISIBLE);
        custom_pb = (ProgressBar) view.findViewById(R.id.custom_pb);
        custom_pblayout = (RelativeLayout) view.findViewById(R.id.custom_pblayout);
        custom_pblayout_readsize = (TextView) view.findViewById(R.id.custom_pblayout_readsize);
        custom_pblayout_totalsize = (TextView) view.findViewById(R.id.custom_pblayout_totalsize);
        custom_pblayout_progress = (TextView) view.findViewById(R.id.custom_pblayout_progress);
        normalClick();

        //正在执行更新操作
        if (UpdateService.downloadUrls.contains(model.getUrl())) {
            //直接设置成下载时的样式
            if (isCanCancel) {
                custom_positiveButton.setText("后台下载");
                custom_positiveButton.setOnClickListener(v -> {
                    dismiss();
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
            if (file!=null) {
                custom_positiveButton.setText("安装");
            } else {
                custom_positiveButton.setText("确定");
            }
        }

        EventBus.getDefault().register(this);
        return view;
    }

    private void normalClick() {
        custom_positiveButton.setOnClickListener(v -> {
            File file = fileExists(model);
            if (file!=null) {
                startActivity(Utils.install(getActivity(), file.getPath()));
                if (isCanCancel) {
                    dismiss();
                }
            } else {
                if (!UpdateService.downloadUrls.contains(model.getUrl())) {
                    Intent intent = new Intent(getActivity(), UpdateService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", model.getUrl());
                    bundle.putBoolean("download", true);
                    bundle.putInt("ids", ids);
                    bundle.putString("name", model.getNotificationTitle());
                    intent.putExtras(bundle);
                    getActivity().startService(intent);
                    isFirstRefresh=true;

                    //直接设置成下载时的样式
                    if (isCanCancel) {
                        custom_positiveButton.setText("后台下载");
                        custom_positiveButton.setOnClickListener(v1 -> dismiss());
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

    @NonNull
    private File fileExists(UpdateModel model) {
        File file;
        String url = model.getUrl();
        if (url.indexOf("?") != -1) {
            String url_ = url.substring(0, url.indexOf("?"));
            file = new File(InitParams.FILE_PATH + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
        } else {
            file = new File(InitParams.FILE_PATH  + File.separator + url.substring(url.lastIndexOf("/") + 1));
        }
        if (file.exists() && Utils.checkAPKState(getActivity(), file.getPath())) {
            return file;
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateModel model) {
        if (!model.getUrl().equals(AppUpdateDialogFragment.this.model.getUrl())) {
            return;
        }
        if (model.getState() == UpdateModel.State.DOWNLOADING) {
            if (isFirstRefresh) {
                isFirstRefresh=false;
                if (isCanCancel) {
                    custom_positiveButton.setText("后台下载");
                    custom_positiveButton.setOnClickListener(v -> {
                        dismiss();
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener!=null) {
            dismissListener.dismissFragment();
        }
    }

    public void show(FragmentManager manager, final String tag) {
        this.fragmentManager=manager;
        if (manager.isDestroyed() || !isDismiss) {
            return;
        }
        isDismiss=false;
        new Handler().post(() -> {
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.add(AppUpdateDialogFragment.this, tag);
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
            fragmentManager.popBackStack();
            FragmentTransaction transaction=fragmentManager.beginTransaction();
            transaction.remove(AppUpdateDialogFragment.this);
            transaction.commitAllowingStateLoss();
        });
    }
}
