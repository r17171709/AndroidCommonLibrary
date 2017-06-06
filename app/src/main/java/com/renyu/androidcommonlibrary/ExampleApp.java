package com.renyu.androidcommonlibrary;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.renyu.commonlibrary.commonutils.ImagePipelineConfigUtils;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;

import java.io.File;

import static com.renyu.commonlibrary.params.InitParams.ROOT_PATH;

/**
 * Created by renyu on 2016/12/26.
 */

public class ExampleApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        String processName= Utils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getPackageName())) {
            // 初始化网络请求
            Retrofit2Utils.getInstance("http://www.baidu.com");

            // 初始化工具库
            com.blankj.utilcode.util.Utils.init(this);

            // 初始化fresco
            Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));

            // 初始化相关配置参数
            // 项目根目录
            ROOT_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator + "demo";
            // 项目图片目录
            InitParams.IMAGE_PATH= ROOT_PATH + File.separator + "image";
            // 项目文件目录
            InitParams.FILE_PATH= ROOT_PATH + File.separator + "file";
            // 项目热修复目录
            InitParams.HOTFIX_PATH= ROOT_PATH + File.separator + "hotfix";
            // 项目日志目录
            InitParams.LOG_PATH= ROOT_PATH + File.separator + "log";
            InitParams.LOG_NAME= "demo_log";
            // 缓存目录
            InitParams.CACHE_PATH= ROOT_PATH + File.separator + "cache";
            // fresco缓存目录
            InitParams.FRESCO_CACHE_NAME= "fresco_cache";
            // 升级文件放在Download文件夹下
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
