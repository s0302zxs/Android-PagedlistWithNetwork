package com.giles.android.pagedlistwithnetwork.di;

import com.giles.android.pagedlistwithnetwork.ui.userDetail.UserDetailFragment;
import com.giles.android.pagedlistwithnetwork.ui.user.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ggshao on 2018/5/29.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract UserDetailFragment contributeUserDetailFragment();
}
