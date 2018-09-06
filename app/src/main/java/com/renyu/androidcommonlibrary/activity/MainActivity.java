package com.renyu.androidcommonlibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;

import com.blankj.utilcode.util.FileUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.impl.WebAppInterface;
import com.renyu.commonlibrary.annotation.NeedPermission;
import com.renyu.commonlibrary.annotation.PermissionDenied;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.params.InitParams;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

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
        FileUtils.createOrExistsDir(InitParams.FILE_PATH);
        FileUtils.createOrExistsDir(InitParams.LOG_PATH);
        FileUtils.createOrExistsDir(InitParams.CACHE_PATH);

        // js调用示例，使用淘房范例
        Intent intent=new Intent(MainActivity.this, MyWebActivity.class);
        // 定义跨平台交互关键字
        intent.putExtra("WebAppImplName", "house365js");
        intent.putExtra("title", "测试");
        // 定义方法实现接口
        intent.putExtra("WebAppImpl", new WebAppInterface());
        intent.putExtra("url", "https://mtt.house365.com/H5/test/vrapp.php?addtionaltype=1");
        intent.putExtra(InitParams.NEED_GOBACK, true);
        startActivity(intent);

//        Intent intent1=new Intent(MainActivity.this, MyX5WebActivity.class);
//        intent1.putExtra("url", "https://mtt.house365.com/index.php?m=home&c=fangdaijisuanqi&a=index&city=nj&q=business_fund_20__cn");
//        intent1.putExtra("cookieUrl", "mtt.house365.com");
//        ArrayList<String> cookieValues = new ArrayList<>();
//        cookieValues.add("is_close_app_down");
//        cookieValues.add("2");
//        intent1.putExtra("cookieValues", cookieValues);
//        intent1.putExtra(InitParams.NEED_GOBACK, true);
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
