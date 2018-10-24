package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.di.module.ApiModule;
import com.renyu.androidcommonlibrary.di.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
//    void injectApp(ExampleApp application);

    ActivityComponent plusAct();

    ReposComponent plusRepos();
}
