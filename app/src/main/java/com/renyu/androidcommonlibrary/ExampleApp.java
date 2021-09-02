package com.renyu.androidcommonlibrary;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.renyu.androidcommonlibrary.di.component.AppComponent;
import com.renyu.androidcommonlibrary.di.component.DaggerAppComponent;
import com.renyu.androidcommonlibrary.di.module.ApiModule;
import com.renyu.androidcommonlibrary.di.module.AppModule;
import com.renyu.androidcommonlibrary.utils.TestManager;
import com.renyu.commonlibrary.commonutils.ImagePipelineConfigUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.web.util.PreloadWebView;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by renyu on 2016/12/26.
 */

public class ExampleApp extends MultiDexApplication implements ViewModelStoreOwner {
    public AppComponent appComponent;

    public ArrayList<String> openClassNames;

    private ViewModelStore viewModelStore;

    @Override
    public void onCreate() {
        super.onCreate();

        openClassNames = new ArrayList<>();

        viewModelStore = new ViewModelStore();

        // Android P 以及之后版本不支持同时从多个进程使用具有相同数据目录的WebView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = ProcessUtils.getCurrentProcessName();
            if (!Utils.getApp().getPackageName().equals(processName)) {
                //判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);
            }
        }

        String processName = ProcessUtils.getCurrentProcessName();
        if (processName != null && processName.equals(getPackageName())) {
            // dagger2注入
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .apiModule(new ApiModule())
                    .build();

            // 初始化工具库
            Utils.init(this);

            // 设置网络请求成功码
            Retrofit2Utils.sucessedCode = 1;

            // 初始化MMKV
            MMKV.initialize(this);

            // 初始化相关配置参数
            // 项目根目录
            // 请注意修改xml文件夹下filepaths.xml中的external-path节点，此值需与ROOT_PATH值相同，作为fileprovider使用
            InitParams.ROOT_PATH = Utils.getApp().getExternalFilesDir(null).getPath();
            // 项目图片目录
            InitParams.IMAGE_PATH = InitParams.ROOT_PATH + File.separator + "image";
            // 项目热修复目录
            InitParams.HOTFIX_PATH = InitParams.ROOT_PATH + File.separator + "hotfix";
            // 项目日志目录
            InitParams.LOG_PATH = InitParams.ROOT_PATH + File.separator + "log";
            InitParams.LOG_NAME = "example_log";
            // 缓存目录
            InitParams.CACHE_PATH = InitParams.ROOT_PATH + File.separator + "cache";
            // fresco缓存目录
            InitParams.FRESCO_CACHE_NAME = "fresco_cache";
            // app更新功能目录
            com.renyu.commonlibrary.update.params.InitParams.FILE_PATH = InitParams.ROOT_PATH + File.separator + "file";

            // 初始化fresco
            Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));

            PreloadWebView.getInstance().preload();

            // 注册统计Activity生命周期所用的LifeCycle
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {
                    openClassNames.add(activity.getLocalClassName());
                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {
                    openClassNames.remove(activity.getLocalClassName());
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }

        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationLifeCycleObserver());

        // 测试epic
//        TestManager.getInstance().initAllSuites();
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    /**
     * Application生命周期观察，提供整个应用进程的生命周期
     * Lifecycle.Event.ON_CREATE只会分发一次，Lifecycle.Event.ON_DESTROY不会被分发。
     * 第一个Activity进入时，ProcessLifecycleOwner将分派Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME。
     * 而Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP，将在最后一个Activit退出后后延迟分发。如果由于配置更改而销毁并重新创建活动，则此延迟足以保证ProcessLifecycleOwner不会发送任何事件。
     */
    private static class ApplicationLifeCycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        private void onAppForeground() {
            Log.w("TAGTAGTAG", "ApplicationObserver: app moved to foreground");
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        private void onAppBackground() {
            Log.w("TAGTAGTAG", "ApplicationObserver: app moved to background");
        }
    }
}
