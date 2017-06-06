package com.renyu.commonlibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.renyu.commonlibrary.R;
import com.renyu.commonlibrary.bean.UpdateModel;
import com.renyu.commonlibrary.commonutils.NotificationUtils;
import com.renyu.commonlibrary.commonutils.Utils;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.params.InitParams;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by renyu on 16/1/24.
 */
public class UpdateService extends Service {

    OKHttpHelper httpHelper;

    public static ArrayList<String> downloadUrls;
    HashMap<String, Integer> ids;

    static {
        downloadUrls=new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ids=new HashMap<>();

        httpHelper=new OKHttpHelper();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent==null || intent.getExtras()==null || intent.getExtras().getString("url")==null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String url=intent.getExtras().getString("url");
        if (intent.getExtras().getBoolean("download")) {
            //新增下载添加标志
            downloadUrls.add(url);
            //这个无所谓是不是清空，只要保留键值对即可
            ids.put(url, intent.getExtras().getInt("ids"));

            NotificationUtils.getNotificationCenter(getApplicationContext())
                    .createDownloadNotification(getApplicationContext(),
                            intent.getExtras().getInt("ids"),
                            intent.getExtras().getString("name"),
                            ContextCompat.getColor(this, R.color.colorPrimary),
                            intent.getExtras().getInt("smallIcon"), intent.getExtras().getInt("largeIcon"));

            httpHelper.downloadFile(url, InitParams.FILE_PATH, new OKHttpHelper.RequestListener() {
                @Override
                public void onSuccess(String string) {
                    UpdateModel model=new UpdateModel();
                    model.setState(UpdateModel.State.SUCCESS);
                    model.setUrl(url);
                    model.setLocalPath(string);
                    model.setNotificationTitle(intent.getExtras().getString("name"));
                    updateInfo(model);
                }

                @Override
                public void onError() {
                    UpdateModel model=new UpdateModel();
                    model.setState(UpdateModel.State.FAIL);
                    model.setUrl(url);
                    model.setNotificationTitle(intent.getExtras().getString("name"));
                    updateInfo(model);
                }
            }, (progress, bytesRead, contentLength) -> {
                UpdateModel model=new UpdateModel();
                model.setState(UpdateModel.State.DOWNLOADING);
                model.setUrl(url);
                model.setProcess(progress);
                model.setBytesRead(bytesRead);
                model.setContentLength(contentLength);
                model.setNotificationTitle(intent.getExtras().getString("name"));
                updateInfo(model);
            });
        }
        else {
            if (ids.containsKey(url)) {
                NotificationUtils.getNotificationCenter(this.getApplicationContext()).cancelNotification(ids.get(url));
            }
            //取消下载移除标志
            downloadUrls.remove(url);
            httpHelper.cancel(url);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateInfo(UpdateModel model) {
        if (model.getState()== UpdateModel.State.DOWNLOADING) {
            if (ids.containsKey(model.getUrl())) {
                NotificationUtils.getNotificationCenter(this.getApplicationContext()).updateDownloadNotification(this.getApplicationContext(), ids.get(model.getUrl()), model.getProcess(), model.getNotificationTitle());
            }
        }
        else if (model.getState()== UpdateModel.State.SUCCESS) {
            //区分文件下载完整
            if (Utils.checkAPKState(this, new File(model.getLocalPath()).getPath())) {
                Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
                if (ids.containsKey(model.getUrl())) {
                    NotificationUtils.getNotificationCenter(this.getApplicationContext()).showEndNotification(this.getApplicationContext(), ids.get(model.getUrl()));
                }

                File file = fileExists(model);
                if (file!=null) {
                    startActivity(Utils.install(this, file.getPath()));
                }
            }
            else {
                model.setState(UpdateModel.State.FAIL);
                Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
                if (ids.containsKey(model.getUrl())) {
                    NotificationUtils.getNotificationCenter(this.getApplicationContext()).cancelNotification(ids.get(model.getUrl()));
                }
            }
            downloadUrls.remove(model.getUrl());
        }
        else if (model.getState()== UpdateModel.State.FAIL) {
            if (!downloadUrls.contains(model.getUrl())) {
                Toast.makeText(this, "下载已取消", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
                if (ids.containsKey(model.getUrl())) {
                    NotificationUtils.getNotificationCenter(this.getApplicationContext()).cancelNotification(ids.get(model.getUrl()));
                }
                downloadUrls.remove(model.getUrl());
            }
        }
        EventBus.getDefault().post(model);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        httpHelper=new OKHttpHelper();
        for (String downloadUrl : downloadUrls) {
            httpHelper.cancel(downloadUrl);
        }
        NotificationUtils.getNotificationCenter(this.getApplicationContext()).cancelAll();
    }


    @NonNull
    private File fileExists(UpdateModel model) {
        File file;
        String url = model.getUrl();
        if (url.indexOf("?") != -1) {
            String url_ = url.substring(0, url.indexOf("?"));
            file = new File(InitParams.FILE_PATH + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
        } else {
            file = new File(InitParams.FILE_PATH  + File.separator + url.substring(url.lastIndexOf("/") + 1));
        }
        if (file.exists() && Utils.checkAPKState(this, file.getPath())) {
            return file;
        }
        return null;
    }
}
