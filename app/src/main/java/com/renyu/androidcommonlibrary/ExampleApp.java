package com.renyu.androidcommonlibrary;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.renyu.androidcommonlibrary.dbhelper.PlainTextDBHelper;
import com.renyu.commonlibrary.commonutils.ImagePipelineConfigUtils;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.io.File;

/**
 * Created by renyu on 2016/12/26.
 */

public class ExampleApp extends MultiDexApplication {

    public PlainTextDBHelper dbHelper;
    public SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

        String processName= Utils.getProcessName(android.os.Process.myPid());
        if (processName.equals(getPackageName())) {
            // 初始化网络请求
            Retrofit2Utils.getInstance("http://www.mocky.io/v2/");

            // 初始化工具库
            com.blankj.utilcode.util.Utils.init(this);

            // 初始化fresco
            Fresco.initialize(this, ImagePipelineConfigUtils.getDefaultImagePipelineConfig(this));

            // 初始化数据库
            if (dbHelper!=null && db!=null && db.isOpen()) {
                db.close();
                db=null;
                dbHelper=null;
            }
            dbHelper=new PlainTextDBHelper(getApplicationContext());
            dbHelper.setWriteAheadLoggingEnabled(true);
            db=dbHelper.getWritableDatabase();

            // 初始化相关配置参数
            // 项目根目录
            // 请注意修改xml文件夹下filepaths.xml中的external-path节点，此值需与ROOT_PATH值相同，作为fileprovider使用
            InitParams.ROOT_PATH= Environment.getExternalStorageDirectory().getPath()+ File.separator + "example";
            // 项目图片目录
            InitParams.IMAGE_PATH= InitParams.ROOT_PATH + File.separator + "image";
            // 项目文件目录
            InitParams.FILE_PATH= InitParams.ROOT_PATH + File.separator + "file";
            // 项目热修复目录
            InitParams.HOTFIX_PATH= InitParams.ROOT_PATH + File.separator + "hotfix";
            // 项目日志目录
            InitParams.LOG_PATH= InitParams.ROOT_PATH + File.separator + "log";
            InitParams.LOG_NAME= "example_log";
            // 缓存目录
            InitParams.CACHE_PATH= InitParams.ROOT_PATH + File.separator + "cache";
            // fresco缓存目录
            InitParams.FRESCO_CACHE_NAME= "fresco_cache";
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(base);
    }
}
