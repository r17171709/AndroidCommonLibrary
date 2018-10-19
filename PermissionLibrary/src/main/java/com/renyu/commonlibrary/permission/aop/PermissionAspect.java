package com.renyu.commonlibrary.permission.aop;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.renyu.commonlibrary.permission.activity.PermissionActivity;
import com.renyu.commonlibrary.permission.annotation.NeedPermission;
import com.renyu.commonlibrary.permission.annotation.PermissionDenied;
import com.renyu.commonlibrary.permission.impl.IPermissionStatue;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/5/25.
 */
@Aspect
public class PermissionAspect {

    @Pointcut("execution(@com.renyu.commonlibrary.permission.annotation.NeedPermission * *(..))")
    public void requestPermissionMethod() {

    }

    @Around("requestPermissionMethod()")
    public void aroundRequestPermissionMethod(ProceedingJoinPoint joinPoint) {
        Log.d("PermissionAspect", "aroundRequestPermissionMethod");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        NeedPermission needPermission = methodSignature.getMethod().getAnnotation(NeedPermission.class);
        PermissionActivity.gotoActivity(getContext(joinPoint.getThis()),
                needPermission.permissions(), needPermission.deniedDesp(), new IPermissionStatue() {
                    @Override
                    public void grant() {
                        try {
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void denied() {
                        Class class_ = joinPoint.getThis().getClass();
                        Method[] methods = class_.getDeclaredMethods();
                        if (methods == null || methods.length == 0) {
                            return;
                        }
                        for (Method method : methods) {
                            if (method.isAnnotationPresent(PermissionDenied.class)) {
                                String[] permissionsDenied = method.getAnnotation(PermissionDenied.class).permissions();
                                String[] permissionsNeed = needPermission.permissions();
                                boolean size = false;
                                if (permissionsDenied.length == permissionsNeed.length) {
                                    size = true;
                                }
                                if (size) {
                                    int samePermissionNum = 0;
                                    for (String s : permissionsNeed) {
                                        for (String s1 : permissionsDenied) {
                                            if (s.equals(s1)) {
                                                samePermissionNum++;
                                                break;
                                            }
                                        }
                                    }
                                    if (samePermissionNum == permissionsDenied.length) {
                                        method.setAccessible(true);
                                        try {
                                            method.invoke(joinPoint.getThis());
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            return fragment.getActivity();
        } else if (object instanceof View) {
            View view = (View) object;
            return view.getContext();
        }
        return null;
    }
}
