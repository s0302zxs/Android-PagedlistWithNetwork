package com.giles.android.pagedlistwithnetwork.di;

import com.giles.android.pagedlistwithnetwork.PagelistWithNetworkApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by ggshao on 2018/5/22.
 */

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuildersModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(PagelistWithNetworkApp application);
        AppComponent build();
    }

    void inject(PagelistWithNetworkApp app);
}