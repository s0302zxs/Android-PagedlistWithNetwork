package com.giles.android.pagedlistwithnetwork.di;

import com.giles.android.pagedlistwithnetwork.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ggshao on 2018/5/22.
 */

@Module
public abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();


}
