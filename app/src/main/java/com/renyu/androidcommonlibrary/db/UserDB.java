package com.renyu.androidcommonlibrary.db;

import com.renyu.androidcommonlibrary.bean.UserBean;

public class UserDB extends AbstractDBManager<UserBean> {
    private static volatile UserDB db = null;

    public static UserDB getInstance() {
        if (db == null) {
            synchronized (UserDB.class) {
                if (db == null) {
                    db = new UserDB();
                }
            }
        }
        return db;
    }
}
