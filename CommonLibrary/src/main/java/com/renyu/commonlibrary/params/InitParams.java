package com.renyu.commonlibrary.params;

import com.blankj.utilcode.util.Utils;

import java.io.File;

/**
 * Created by renyu on 2017/4/19.
 */

public class InitParams {
    // 项目根目录
    public static String ROOT_PATH = Utils.getApp().getExternalFilesDir(null).getPath();
    // 项目图片目录
    public static String IMAGE_PATH = ROOT_PATH + File.separator + "image";
    // 项目热修复目录
    public static String HOTFIX_PATH = ROOT_PATH + File.separator + "hotfix";
    // 项目日志目录
    public static String LOG_PATH = ROOT_PATH + File.separator + "log";
    public static String LOG_NAME = "example_log";
    // 缓存目录
    public static String CACHE_PATH = ROOT_PATH + File.separator + "cache";
    // fresco缓存目录
    public static String FRESCO_CACHE_NAME = "fresco_cache";
}
