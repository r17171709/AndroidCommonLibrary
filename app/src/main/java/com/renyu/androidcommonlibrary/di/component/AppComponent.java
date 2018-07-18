package com.renyu.androidcommonlibrary.di.component;

import com.renyu.androidcommonlibrary.di.module.ApiModule;
import com.renyu.androidcommonlibrary.di.module.AppModule;
import com.renyu.androidcommonlibrary.di.module.ReposModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {
//    void injectApp(ExampleApp application);

    ReposComponent plus(ReposModule architectureModule);

    ActivityComponent plusAct(ReposModule architectureModule);
}
