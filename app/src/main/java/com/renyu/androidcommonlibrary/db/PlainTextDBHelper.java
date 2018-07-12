package com.renyu.androidcommonlibrary.db;

import android.content.Context;

import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteOpenHelper;

/**
 * Created by renyu on 2017/6/16.
 */

public class PlainTextDBHelper extends SQLiteOpenHelper {

    private final static String DATABASENAME="test.db";
    private final static int VERSION=1;
    private final static String ID="_id";
    public final static String TABLENAME="testtahle";
    public final static String COLUMNNAME="testcolumn";

    public PlainTextDBHelper(Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLENAME+"("+ID+" integer primary key AUTOINCREMENT, "+COLUMNNAME+" text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
