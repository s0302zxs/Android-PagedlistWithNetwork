package com.giles.android.pagedlistwithnetwork.ui.userDetail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.UserDetail;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

public class UserDetailViewModel extends ViewModel {

    private UserDetailRepository userDetailRepository;
    private final MutableLiveData<String> queryUsername = new MutableLiveData<>();
    private final MutableLiveData<UserDetail> userDetailLiveData = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable;
    private Completable retryCompletable;
    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    @Inject
    public UserDetailViewModel(UserDetailRepository userDetailRepository, CompositeDisposable compositeDisposable) {
        super();
        this.userDetailRepository = userDetailRepository;
        this.compositeDisposable = compositeDisposable;
    }


    public void getUserDetail(String username){
        Timber.d("getUserDetail username = " + username);
        initialLoad.setValue(NetworkState.Companion.getLOADING());
        queryUsername.setValue(username);

        DisposableObserver<UserDetail> disposableObserver = new DisposableObserver<UserDetail>() {
            @Override
            public void onNext(UserDetail value) {
                Timber.d("onNext value = " + value);
                //check isSuccessful here
                setRetry(null);
                userDetailLiveData.setValue(value);

                //check code
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError e = " + e);
                setRetry(() -> getUserDetail(username));
                userDetailLiveData.setValue(null);
                initialLoad.setValue(NetworkState.Companion.error(e.getMessage()));
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
                initialLoad.setValue(NetworkState.Companion.getLOADED());
            }

        };

        compositeDisposable.add(userDetailRepository.getUserDetail(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver));
    }

    public void retry() {
        Timber.d("retry retryCompletable = " + retryCompletable);
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
//                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> initialLoad.setValue(NetworkState.Companion.error(throwable.getMessage()))));
        }
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

    MutableLiveData<UserDetail> getUserDetailLiveData() {
        return userDetailLiveData;
    }

    MutableLiveData<NetworkState> getInitialState() {
        return initialLoad;
    }



}
