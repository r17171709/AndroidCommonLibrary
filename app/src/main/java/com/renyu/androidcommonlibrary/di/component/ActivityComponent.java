package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.activity.CoroutinesDemoActivity;
import com.renyu.androidcommonlibrary.activity.RetrofitActivity;
import com.renyu.androidcommonlibrary.activity.WCDBActivity;
import com.renyu.androidcommonlibrary.di.module.ReposModule;

import dagger.Subcomponent;

@Subcomponent(modules = ReposModule.class)
public interface ActivityComponent {
    void inject(RetrofitActivity retrofitActivity);

    void inject(WCDBActivity wcdbActivity);

    void inject(CoroutinesDemoActivity wcdbActivity);
}
