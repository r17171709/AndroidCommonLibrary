package com.renyu.androidcommonlibrary.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.Utils;
import com.renyu.androidcommonlibrary.ExampleApp;

public class ExampleUtils {
    public static <T extends ViewModel> T getActivityViewModel(AppCompatActivity owner, Class<T> modelClass) {
        return new ViewModelProvider(owner).get(modelClass);
    }

    public static <T extends ViewModel> T getFragmentViewModel(Fragment owner, Class<T> modelClass) {
        return new ViewModelProvider(owner).get(modelClass);
    }

    public static <T extends ViewModel> T getAppViewModel(Class<T> modelClass) {
        return getAppViewModelProvider().get(modelClass);
    }

    public static ViewModelProvider getAppViewModelProvider() {
        return new ViewModelProvider(((ExampleApp) Utils.getApp()), getAppFactory());
    }

    /**
     * 获取默认的单例AndroidViewModelFactory
     *
     * @return
     */
    public static ViewModelProvider.Factory getAppFactory() {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(Utils.getApp());
    }
}
