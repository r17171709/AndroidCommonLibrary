package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.activity.OKHttpActivity;
import com.renyu.androidcommonlibrary.activity.RetrofitActivity;
import com.renyu.androidcommonlibrary.activity.ScrollRVActivity;
import com.renyu.androidcommonlibrary.activity.WCDBActivity;

import dagger.Subcomponent;

@Subcomponent
public interface ActivityComponent {
    void inject(RetrofitActivity retrofitActivity);

    void inject(WCDBActivity wcdbActivity);

    void inject(ScrollRVActivity scrollRVActivity);

    void inject(OKHttpActivity okHttpActivity);
}
