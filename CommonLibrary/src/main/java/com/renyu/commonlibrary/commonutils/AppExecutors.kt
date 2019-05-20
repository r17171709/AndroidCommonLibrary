package com.renyu.commonlibrary.commonutils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Administrator on 2019/5/20.
 */
private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

private val MAIN_EXECUTOR = MainThreadExecutor()

private class MainThreadExecutor : Executor {
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        mainThreadHandler.post(command)
    }
}

fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}

fun mainThread(f: () -> Unit) {
    MAIN_EXECUTOR.execute(f)
}