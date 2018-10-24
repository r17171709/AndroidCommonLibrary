package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.repository.Repos;

import dagger.Subcomponent;

@Subcomponent
public interface ReposComponent {
    void inject(Repos repos);
}
