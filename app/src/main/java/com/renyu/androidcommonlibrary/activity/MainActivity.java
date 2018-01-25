package com.renyu.androidcommonlibrary.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.bean.ExampleAResponse;
import com.renyu.androidcommonlibrary.impl.RetrofitImpl;
import com.renyu.androidcommonlibrary.impl.X5WebAppInterface;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.renyu.commonlibrary.commonutils.NotificationUtils;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.renyu.commonlibrary.params.InitParams;
import com.renyu.commonlibrary.views.dialog.LoadingDialog;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_hello)
    TextView tv_hello;

    LoadingDialog loadingDialog;

    @Override
    public void initParams() {
        tv_hello.setOnClickListener(v -> {
            NotificationUtils.getNotificationCenter(this).createNormalNotification(this, "ticker", "title", "content", Color.RED, R.mipmap.ic_launcher, R.mipmap.ic_launcher, new Intent());
        });
    }

    @Override
    public int initViews() {
        return R.layout.activity_main;
    }

    @Override
    public void loadData() {
        String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        checkPermission(permissions, "请授予SD卡读写权限", new BaseActivity.OnPermissionCheckedListener() {
            @Override
            public void checked(boolean flag) {

            }

            @Override
            public void grant() {
                // 初始化文件夹
                FileUtils.createOrExistsDir(InitParams.IMAGE_PATH);
                FileUtils.createOrExistsDir(InitParams.HOTFIX_PATH);
                FileUtils.createOrExistsDir(InitParams.FILE_PATH);
                FileUtils.createOrExistsDir(InitParams.LOG_PATH);
                FileUtils.createOrExistsDir(InitParams.CACHE_PATH);

                // js调用示例
                Intent intent=new Intent(MainActivity.this, MyX5WebActivity.class);
                // 定义跨平台交互关键字
                intent.putExtra("WebAppImplName", "android");
                intent.putExtra("title", "测试");
                // 定义方法实现接口
                intent.putExtra("WebAppImpl", new X5WebAppInterface());
                intent.putExtra("url", "https://aznapi.house365.com/Home/Information/lists");
                intent.putExtra(InitParams.NEED_GOBACK, true);
                //startActivity(intent);

                // 测试网络请求
                retrofit.create(RetrofitImpl.class)
                        .getExampleValue()
                        .compose(Retrofit2Utils.background())
                        .subscribe(new Observer<ExampleAResponse>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                loadingDialog = LoadingDialog.getInstance_TextLoading("Hello");
                                loadingDialog.show(MainActivity.this);

                            }

                            @Override
                            public void onNext(ExampleAResponse value) {
                                Toast.makeText(MainActivity.this, value.getU_id(), Toast.LENGTH_SHORT).show();
                                loadingDialog.setCloseChoice("title", "确定", "取消");
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }

            @Override
            public void denied() {
                finish();
            }
        });
    }

    @Override
    public int setStatusBarColor() {
        return 0;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
