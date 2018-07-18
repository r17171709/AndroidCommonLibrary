package com.renyu.androidcommonlibrary.di.module;

import com.renyu.androidcommonlibrary.ExampleApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private ExampleApp application;

    public AppModule(ExampleApp application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public ExampleApp provideApplication() {
        return application;
    }
}
