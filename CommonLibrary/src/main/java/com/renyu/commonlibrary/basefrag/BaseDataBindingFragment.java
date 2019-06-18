package com.renyu.commonlibrary.basefrag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by renyu on 15/12/3.
 */
public abstract class BaseDataBindingFragment<T extends ViewDataBinding> extends Fragment {
    public abstract void initParams();

    public abstract int initViews();

    public abstract void loadData();

    public T viewDataBinding;

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
        if (viewDataBinding == null) {
            viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), initViews(), container, false);
        }
        ViewGroup parent = (ViewGroup) (viewDataBinding.getRoot().getParent());
        if (parent != null) {
            parent.removeView(viewDataBinding.getRoot());
        }
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initParams();
        loadData();
    }
}
