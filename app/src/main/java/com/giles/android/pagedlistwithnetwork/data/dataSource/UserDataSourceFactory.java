package com.giles.android.pagedlistwithnetwork.data.dataSource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.giles.android.pagedlistwithnetwork.Api.ApiService;
import com.giles.android.pagedlistwithnetwork.data.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

@Singleton
public class UserDataSourceFactory extends DataSource.Factory<Integer, User> {

    private ApiService apiService;
    private CompositeDisposable compositeDisposable;

    private final MutableLiveData<UserDataSource> userDataSourceLiveData = new MutableLiveData<>();

    @Inject
    public UserDataSourceFactory(ApiService apiService, CompositeDisposable compositeDisposable) {
        this.apiService = apiService;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public DataSource<Integer, User> create() {
        UserDataSource userDataSource = new UserDataSource(apiService, compositeDisposable);
        userDataSourceLiveData.postValue(userDataSource);
        return userDataSource;
    }

    @NonNull
    public MutableLiveData<UserDataSource> getUserDataSourceLiveData() {
        return userDataSourceLiveData;
    }


}
