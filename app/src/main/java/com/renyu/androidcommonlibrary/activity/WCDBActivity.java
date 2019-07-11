package com.renyu.androidcommonlibrary.activity;

import android.graphics.Color;
import com.blankj.utilcode.util.Utils;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.bean.UserBean;
import com.renyu.androidcommonlibrary.db.IDBManager;
import com.renyu.androidcommonlibrary.db.UserDB;
import com.renyu.commonlibrary.baseact.BaseActivity;

import javax.inject.Inject;

/**
 * Created by renyu on 2017/6/16.
 */

public class WCDBActivity extends BaseActivity {
    @Inject
    IDBManager db;

    @Override
    public void initParams() {
        ((ExampleApp) (Utils.getApp())).appComponent.plusAct().inject(this);

        UserBean userBean = new UserBean();
        userBean.userId = 123;
        userBean.userName = "pq";
        db.createDB(UserBean.class);
        db.insertDB(userBean);
//        UserBean userBeanM = new UserBean();
//        userBeanM.userName = "pq";
//        db.updateDB(userBean, userBeanM);
//        db.queryDB(UserBean.class);
//        db.deleteDB(userBeanM);
    }

    @Override
    public int initViews() {
        return R.layout.activity_wcdb;
    }

    @Override
    public void loadData() {

    }

    @Override
    public int setStatusBarColor() {
        return Color.BLACK;
    }

    @Override
    public int setStatusBarTranslucent() {
        return 0;
    }
}
