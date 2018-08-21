package com.giles.android.pagedlistwithnetwork.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.giles.android.pagedlistwithnetwork.MainActivityViewModel;
import com.giles.android.pagedlistwithnetwork.ui.userDetail.UserDetailViewModel;
import com.giles.android.pagedlistwithnetwork.ui.common.SharedViewModel;
import com.giles.android.pagedlistwithnetwork.ui.user.UserViewModel;
import com.giles.android.pagedlistwithnetwork.viewModel.PagedlistWithNetworkViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by ggshao on 2018/5/29.
 */

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PagedlistWithNetworkViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel.class)
    abstract ViewModel bindSharedViewModel(SharedViewModel sharedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel.class)
    abstract ViewModel bindUserDetailViewModel(UserDetailViewModel userDetailViewModel);
}
