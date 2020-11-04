package com.renyu.androidcommonlibrary.utils;

import android.util.Log;

import com.renyu.androidcommonlibrary.bean.DataListResponse;

import java.lang.reflect.Method;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class TestManager {
    private static volatile TestManager testManager = null;

    public static TestManager getInstance() {
        if (testManager == null) {
            synchronized (TestManager.class) {
                if (testManager == null) {
                    testManager = new TestManager();
                }
            }
        }
        return testManager;
    }

    public void initAllSuites() {
        Method method = XposedHelpers.findMethodExact(DataListResponse.class, "setId", String.class);
        DexposedBridge.hookMethod(method, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d("TAGTAGTAG", "onPageFinished beforeHookedMethod");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d("TAGTAGTAG", "onPageFinished afterHookedMethod");
            }
        });
    }
}
