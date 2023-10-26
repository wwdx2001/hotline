package com.sh3h.dataprovider;

import android.content.Context;

import com.sh3h.dataprovider.injection.annotation.ApplicationContext;
import com.sh3h.dataprovider.util.EventPosterHelper;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author xiaochao.dev@gamil.com
 * @date 2019/5/28 13:29
 */
@Singleton
@Component(modules = BaseApplicationModule.class)
public interface BaseApplicationComponent {
    void inject(BaseApplication baseApplication);

    @ApplicationContext
    Context context();


    EventPosterHelper eventPosterHelper();

    Bus eventBus();

}
