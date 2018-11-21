package com.renyu.commonlibrary.network.impl;

/**
 * Created by Administrator on 2018/11/21.
 */
public interface IRetryCondition {
    boolean canRetry(Throwable throwable);
    void doBeforeRetry();
}
