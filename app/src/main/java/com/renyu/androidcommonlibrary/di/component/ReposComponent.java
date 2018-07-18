package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.di.module.ReposModule;
import com.renyu.androidcommonlibrary.repository.Repos;

import dagger.Subcomponent;

@Subcomponent(modules = ReposModule.class)
public interface ReposComponent {
    void inject(Repos repos);
}
