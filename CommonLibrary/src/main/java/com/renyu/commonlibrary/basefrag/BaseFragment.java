package com.renyu.commonlibrary.basefrag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Created by renyu on 15/12/3.
 */
public abstract class BaseFragment extends Fragment {
    public abstract void initParams();

    public abstract int initViews();

    public abstract void loadData();

    public View view = null;

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
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(initViews(), container, false);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initParams();
        loadData();
    }

    public <D extends ViewModel> D getFragmentScopeViewModel(Class<D> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    public <D extends ViewModel> D getFragmentScopeViewModel(Class<D> modelClass, ViewModelProvider.Factory factory) {
        return new ViewModelProvider(this, factory).get(modelClass);
    }

    public <D extends ViewModel> D getActivityScopeViewModel(Class<D> modelClass) {
        return new ViewModelProvider((AppCompatActivity) context).get(modelClass);
    }

    public <D extends ViewModel> D getActivityScopeViewModel(Class<D> modelClass, ViewModelProvider.Factory factory) {
        return new ViewModelProvider((AppCompatActivity) context, factory).get(modelClass);
    }

    public <D extends ViewModel, R extends Application & ViewModelStoreOwner> D getApplicationScopeViewModel(Class<D> modelClass, R application) {
        Context context = application.getApplicationContext();
        ViewModelProvider.AndroidViewModelFactory androidViewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        return new ViewModelProvider((ViewModelStoreOwner) context, androidViewModelFactory).get(modelClass);
    }
}
