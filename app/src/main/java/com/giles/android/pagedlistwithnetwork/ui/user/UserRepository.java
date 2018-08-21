package com.giles.android.pagedlistwithnetwork.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.giles.android.pagedlistwithnetwork.data.dataSource.UserDataSource;
import com.giles.android.pagedlistwithnetwork.data.dataSource.UserDataSourceFactory;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class UserRepository {
    private UserDataSourceFactory userDataSourceFactory;


    @Inject
    public UserRepository(UserDataSourceFactory userDataSourceFactory) {
        this.userDataSourceFactory = userDataSourceFactory;
    }


    public LiveData<PagedList<User>> getUser(int since, int perPage){
        Timber.d("getUser since = " + since + " perPage = " + perPage);
        return getUserFromApiPaging(since, perPage);
    }


    public LiveData<PagedList<User>> getUserFromApiPaging(int page, int perPage){
        Timber.d("getReposFromApiPaging page = " + page + " perPage = " + perPage);
        PagedList.Config config = new PagedList.Config.Builder()
                .setPageSize(perPage)
                .setInitialLoadSizeHint(perPage)
                .setEnablePlaceholders(false)
                .build();

        return new LivePagedListBuilder<>(userDataSourceFactory, config).build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return Transformations.switchMap(userDataSourceFactory.getUserDataSourceLiveData(), UserDataSource::getNetworkState);
    }

    public LiveData<NetworkState> getRefreshState() {
        return Transformations.switchMap(userDataSourceFactory.getUserDataSourceLiveData(), UserDataSource::getInitialLoad);
    }

    @NonNull
    public MutableLiveData<UserDataSource> getUserDataSourceLiveData() {
        return userDataSourceFactory.getUserDataSourceLiveData();
    }
}
