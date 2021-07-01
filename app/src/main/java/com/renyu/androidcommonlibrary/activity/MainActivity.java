package com.renyu.androidcommonlibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;

import com.blankj.utilcode.util.FileUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.impl.X5WebAppInterface;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.permission.annotation.NeedPermission;
import com.renyu.commonlibrary.permission.annotation.PermissionDenied;
import com.tencent.mars.xlog.Log;

import java.util.Random;

public class MainActivity extends BaseActivity {
    private String[] urls = {"http://newrenttest.house365.com/activity/house-show/#/pages/index/index",
            "http://newrent.house365.com/zsbapi/public/consult-list?city="};
    private String htmlCode = "<p style=\"\"><strong>365淘房 魏玉蓉 今天给大家带来一套城东140.06㎡的精装</strong><strong>房源，房主容女士是我们安家城东的铁杆粉丝，这次让我们帮他出售的是银城东苑</strong><strong>房源。</strong></p><p style=\"\"><strong>房源信息</strong></p><p style=\"\"><strong>【小区】银城东苑东篱园</strong></p><p style=\"\"><strong>【居室】3房2厅2卫</strong></p><p style=\"\"><strong>【面积】</strong><strong>140.06㎡</strong><strong> </strong></p><p style=\"\"><strong>【楼层】21/22</strong></p><p style=\"\"><strong>【装修】精装</strong></p><p style=\"\"><strong>【总价】</strong><strong>640万</strong></p><p style=\"\"><strong><strong>【均价】</strong></strong><strong><strong>45694元/㎡</strong></strong></p><p style=\"\"><strong>【房屋年限】满5年不唯一</strong></p><p style=\"\"><strong>【房主联系方式】</strong><strong>容女土，电话：<strong>13305192718</strong></strong></p><p style=\"\"><strong>房主自述</strong></p><p style=\"\"><img src=\"http://img31.house365.com/M01/E9/F8/rBEBYF7CYnWALt5cAAP65eZ_Ahg04.jpeg\"/>";

    @Override
    public void initParams() {

    }

    @Override
    public int initViews() {
        return R.layout.activity_main;
    }


    @Override
    public void loadData() {
        showDemo();
    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }

    @NeedPermission(permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
            deniedDesp = "请授予存储卡读取权限")
    public void showDemo() {
        // 初始化文件夹
        FileUtils.createOrExistsDir(InitParams.IMAGE_PATH);
        FileUtils.createOrExistsDir(InitParams.HOTFIX_PATH);
        FileUtils.createOrExistsDir(InitParams.LOG_PATH);
        FileUtils.createOrExistsDir(InitParams.CACHE_PATH);
        // app更新功能文件夹初始化
        FileUtils.createOrExistsDir(com.renyu.commonlibrary.update.params.InitParams.FILE_PATH);

        Log.d("Test", "Hello");

        // js调用示例，使用淘房范例
        Intent intent = new Intent(MainActivity.this, MyX5WebActivity.class);
        // 定义跨平台交互关键字
        intent.putExtra("IWebAppName", "house365js");
        intent.putExtra("title", "测试");
        // 定义方法实现接口
        intent.putExtra("IWebApp", new X5WebAppInterface());
//        intent.putExtra("url", "http://house.3dnest.cn/house365/index.html?city=nj&house_type=sell&house_id=211246849");
//        intent.putExtra("url", "https://mtt.house365.com/H5/test/vrapp.php?addtionaltype=1");
//        intent.putExtra("url", "file:///android_asset/new_file.html");
//        intent.putExtra("url", "http://app.wokeshop.com/chatlink.html");
        int value = new Random().nextInt(10);
        if (value % 2 == 0) {
            intent.putExtra("url", urls[0]);
        } else {
            intent.putExtra("htmlCode", htmlCode);
        }
        intent.putExtra(com.renyu.commonlibrary.web.params.InitParams.NEED_GOBACK, true);
        startActivity(intent);

//        Intent intent1 = new Intent(MainActivity.this, MyX5WebActivity.class);
//        intent1.putExtra("url", "https://m.house365.com/");
//        intent1.putExtra("cookieUrl", "mtt.house365.com");
//        ArrayList<String> cookieValues = new ArrayList<>();
//        cookieValues.add("is_close_app_down");
//        cookieValues.add("2");
//        intent1.putExtra("cookieValues", cookieValues);
//        intent1.putExtra(com.renyu.commonlibrary.web.params.InitParams.NEED_GOBACK, true);
//        startActivity(intent1);

//                ChoiceDialog choiceDialog = ChoiceDialog.getInstanceByChoice("内容", "确定", "取消");
//                choiceDialog.setOnDialogPosListener(() -> {
//                    // 测试网络请求
//                    retrofit.create(RetrofitImpl.class)
//                            .getExampleValue()
//                            .compose(Retrofit2Utils.background())
//                            .subscribe(new BaseObserver<ExampleAResponse>(MainActivity.this, "试试看") {
//                                @Override
//                                public void onNext(ExampleAResponse exampleAResponse) {
//                                    networkLoadingDialog.closeWithTextAndImage(exampleAResponse.getU_id(), R.mipmap.ic_launcher);
//                                }
//                            });
//                });
//                choiceDialog.show(MainActivity.this);
    }

    @PermissionDenied(permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE})
    public void permissionDenied() {
        finish();
    }
}
