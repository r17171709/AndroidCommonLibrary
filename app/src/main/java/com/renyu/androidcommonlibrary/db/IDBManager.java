package com.renyu.androidcommonlibrary.db;

import java.util.List;

public interface IDBManager<T> {
    void createDB(Class<T> clazz);

    long insertDB(T t);

    int updateDB(T t, T where);

    List<T> queryDB(Class<T> clazz);

    int deleteDB(T where);

    void closeDB();
}
