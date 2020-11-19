package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.repository.CoroutineRepos;

import dagger.Subcomponent;

@Subcomponent
public interface CoroutineReposComponent {
    void inject(CoroutineRepos repos);
}
