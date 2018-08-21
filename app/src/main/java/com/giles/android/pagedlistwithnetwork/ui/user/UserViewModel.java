package com.giles.android.pagedlistwithnetwork.ui.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.databinding.ObservableBoolean;

import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.User;

import javax.inject.Inject;

import timber.log.Timber;

public class UserViewModel extends ViewModel {

    private final LiveData<PagedList<User>> users;
    public final ObservableBoolean isLoading = new ObservableBoolean(false);
    private UserRepository userRepository;
    @Inject
    public UserViewModel(UserRepository userRepository) {
        super();

        this.userRepository = userRepository;
        users = this.userRepository.getUser(1, 20);
    }

    public LiveData<PagedList<User>> getUsers() {
        return users;
    }

    public LiveData<NetworkState> getNetworkState() {
        return userRepository.getNetworkState();
    }

    public void refresh() {
        Timber.d("refresh");
        userRepository.getUserDataSourceLiveData().getValue().invalidate();
    }

    public void retry() {
        userRepository.getUserDataSourceLiveData().getValue().retry();
    }

    public LiveData<NetworkState> getRefreshState() {
        return userRepository.getRefreshState();
    }
}
