package com.renyu.androidcommonlibrary.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.Utils;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.R;
import com.renyu.androidcommonlibrary.db.PlainTextDBHelper;
import com.renyu.androidcommonlibrary.di.module.ReposModule;
import com.renyu.commonlibrary.baseact.BaseActivity;
import com.tencent.wcdb.database.SQLiteDatabase;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by renyu on 2017/6/16.
 */

public class WCDBActivity extends BaseActivity {

    @Inject
    SQLiteDatabase db;

    @Override
    public void initParams() {
        ((ExampleApp) (Utils.getApp())).appComponent.plusAct(new ReposModule()).inject(this);
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

    @OnClick({R.id.btn_wcdb_insert, R.id.btn_wcdb_read})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wcdb_insert:
                ContentValues cv=new ContentValues();
                cv.put(PlainTextDBHelper.COLUMNNAME,"hello" );
                Log.d("WCDBActivity", ""+db.insert(PlainTextDBHelper.TABLENAME, null, cv));
                break;
            case R.id.btn_wcdb_read:
                Cursor cursor= db.rawQuery("Select * from "+ PlainTextDBHelper.TABLENAME, new String[]{});
                cursor.moveToFirst();
                for (int i=0;i<cursor.getCount();i++) {
                    cursor.moveToPosition(i);
                    Log.d("WCDBActivity", cursor.getString(cursor.getColumnIndex(PlainTextDBHelper.COLUMNNAME)));
                }
                cursor.close();
        }
    }
}
