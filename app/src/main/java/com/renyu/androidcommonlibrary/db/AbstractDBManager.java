package com.renyu.androidcommonlibrary.db;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Pair;
import com.renyu.androidcommonlibrary.annotation.DBClass;
import com.renyu.androidcommonlibrary.annotation.DBField;
import com.renyu.commonlibrary.params.InitParams;
import com.tencent.wcdb.Cursor;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;


public abstract class AbstractDBManager<T> implements IDBManager<T> {
    private SQLiteDatabase database;

    AbstractDBManager() {
        database = SQLiteDatabase.openOrCreateDatabase(new File(InitParams.ROOT_PATH + File.separator + "example.db"), null);
    }

    @Override
    public void createDB(Class<T> clazz) {
        HashMap<String, Field> columnsMap = new HashMap<>();
        DBClass dbClass = clazz.getAnnotation(DBClass.class);
        if (dbClass != null) {
            for (Field field : clazz.getFields()) {
                field.setAccessible(true);
                DBField dbField = field.getAnnotation(DBField.class);
                if (dbField != null) {
                    columnsMap.put(TextUtils.isEmpty(dbField.value()) ? field.getName() : dbField.value(), field);
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ")
                .append(getTableName(clazz))
                .append("(");
        Iterator<Map.Entry<String, Field>> it = columnsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Field> entry = it.next();
            sql.append(entry.getKey());
            Field field = entry.getValue();
            if (field.getType().toString().equals("int")) {
                sql.append(" integer, ");
            } else if (field.getType() == String.class) {
                sql.append(" text, ");
            }
        }
        sql.append("_id integer primary key AUTOINCREMENT);");
        database.execSQL(sql.toString());
    }

    @Override
    public long insertDB(T t) {
        ContentValues values = setAllContentValues(t);
        return database.insert(getTableName((Class<T>) t.getClass()), null, values);
    }

    @Override
    public int updateDB(T t, T where) {
        ContentValues values = setAllContentValues(t);
        Pair<String, String[]> clauseAndArgs = getWhereClauseAndArgs(where);
        return database.update(getTableName((Class<T>) t.getClass()), values, clauseAndArgs.first, clauseAndArgs.second);
    }

    @Override
    public int deleteDB(T where) {
        Pair<String, String[]> clauseAndArgs = getWhereClauseAndArgs(where);
        return database.delete(getTableName((Class<T>) where.getClass()), clauseAndArgs.first, clauseAndArgs.second);
    }

    @Override
    public List<T> queryDB(Class<T> clazz) {
        ArrayList<T> items = new ArrayList<>();

        Cursor cursor = database.query(getTableName(clazz), null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            try {
                Object item = clazz.newInstance();
                DBClass dbClass = clazz.getAnnotation(DBClass.class);
                if (dbClass != null) {
                    for (Field field : clazz.getFields()) {
                        field.setAccessible(true);
                        DBField dbField = field.getAnnotation(DBField.class);
                        if (dbField != null) {
                            String columnNanme = TextUtils.isEmpty(dbField.value()) ? field.getName() : dbField.value();
                            if (field.getType().toString().equals("int")) {
                                int columnValue = cursor.getInt(cursor.getColumnIndex(columnNanme));
                                field.set(item, columnValue);
                            } else if (field.getType() == String.class) {
                                String columnValue = cursor.getString(cursor.getColumnIndex(columnNanme));
                                field.set(item, columnValue);
                            }
                        }
                    }
                    items.add((T) item);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return items;
    }

    private String getTableName(Class<T> clazz) {
        String tableName = "";
        if (clazz.getAnnotation(DBClass.class) != null) {
            DBClass dbClass = clazz.getAnnotation(DBClass.class);
            tableName = TextUtils.isEmpty(dbClass.value()) ? clazz.getSimpleName() : dbClass.value();
        }
        return tableName;
    }

    private ContentValues setAllContentValues(T t) {
        ContentValues values = new ContentValues();
        for (Field field : t.getClass().getFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(DBField.class) != null) {
                String columnName = TextUtils.isEmpty(field.getAnnotation(DBField.class).value()) ? field.getName() : field.getAnnotation(DBField.class).value();
                try {
                    if (field.getType().toString().equals("int")) {
                        values.put(columnName, Integer.parseInt(field.get(t).toString()));
                    } else if (field.getType() == String.class) {
                        values.put(columnName, field.get(t).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    private Pair<String, String[]> getWhereClauseAndArgs(T where) {
        StringBuilder whereClause = new StringBuilder("1=1 ");
        ArrayList<String> whereArgs = new ArrayList<>();
        for (Field field : where.getClass().getFields()) {
            field.setAccessible(true);
            try {
                if (field.get(where) == null) {
                    continue;
                }
                if (field.getType().toString().equals("int") && Integer.parseInt(field.get(where).toString()) == 0) {
                    continue;
                }
                if (field.getAnnotation(DBField.class) != null) {
                    String columnName = TextUtils.isEmpty(field.getAnnotation(DBField.class).value()) ? field.getName() : field.getAnnotation(DBField.class).value();
                    whereClause.append("and ").append(columnName).append("=? ");
                    whereArgs.add(field.get(where).toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new Pair<>(whereClause.toString(), whereArgs.toArray(new String[whereArgs.size()]));
    }

    @Override
    public void closeDB() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
