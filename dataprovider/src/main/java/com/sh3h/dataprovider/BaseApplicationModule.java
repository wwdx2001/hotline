package com.sh3h.dataprovider;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/28 13:30
 */

import android.app.Application;
import android.content.Context;

import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Provide application-level dependencies.
 * 提供在Application的生命周期中存活的对象，必须是Singleton
 */
@Module
public class BaseApplicationModule {
    protected final Application mApplication;

    public BaseApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Bus provideEventBus() {
        return new Bus();
    }

}

