package com.renyu.androidcommonlibrary.bean;

import com.renyu.androidcommonlibrary.annotation.DBClass;
import com.renyu.androidcommonlibrary.annotation.DBField;

@DBClass()
public class UserBean {
    @DBField()
    public String userName;
    @DBField()
    public int userId;
}
