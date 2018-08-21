package com.giles.android.pagedlistwithnetwork.data.dataSource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.giles.android.pagedlistwithnetwork.Api.ApiService;
import com.giles.android.pagedlistwithnetwork.data.model.NetworkState;
import com.giles.android.pagedlistwithnetwork.data.model.User;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class UserDataSource extends PageKeyedDataSource<Integer, User> {

    private CompositeDisposable compositeDisposable;
    private ApiService apiService;

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    private MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();
//
    private Completable retryCompletable;

    @Inject
    public UserDataSource(ApiService apiService, CompositeDisposable compositeDisposable) {
//        this.repoDao = repoDao;
        this.apiService = apiService;
        this.compositeDisposable = compositeDisposable;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> networkState.postValue(NetworkState.Companion.error(throwable.getMessage()))));
        }
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, User> callback) {
        Timber.d("loadInitial");

        networkState.postValue(NetworkState.Companion.getLOADING());
        initialLoad.postValue(NetworkState.Companion.getLOADING());

        DisposableObserver<List<User>> disposableObserver = new DisposableObserver<List<User>>() {
            @Override
            public void onNext(List<User> value) {
                Timber.d("loadInitial onNext value = " + value);
                setRetry(null);
                callback.onResult(value, 0, value.size());
                networkState.postValue(NetworkState.Companion.getLOADED());
                initialLoad.postValue(NetworkState.Companion.getLOADED());
                //check code
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError e = " + e);
                setRetry(() -> loadInitial(params, callback));
                networkState.postValue(NetworkState.Companion.error("error e.getMessage() = " + e.getMessage()));
                initialLoad.postValue(NetworkState.Companion.error("error e.getMessage() = " + e.getMessage()));
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }

        };

        Timber.d("params.requestedLoadSize = " + params.requestedLoadSize);
        compositeDisposable.add(getUsersFromApiPaging(0, params.requestedLoadSize)
                .subscribeWith(disposableObserver));
    }


    public Observable<List<User>> getUsersFromApiPaging(int since, int perPage){

        return apiService.getUsers(since, perPage)
                .map(getUserResponse -> {
                    Timber.d("getUserResponse = " + getUserResponse);
                    return getUserResponse.body();
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {
        Timber.d("loadAfter params.key = " + params.key);

        networkState.postValue(NetworkState.Companion.getLOADING());
        //do api
        DisposableObserver<List<User>> disposableObserver = new DisposableObserver<List<User>>() {
            @Override
            public void onNext(List<User> value) {
                Timber.d("loadAfter onNext value = " + value);
                setRetry(null);
                networkState.postValue(NetworkState.Companion.getLOADED());
                callback.onResult(value, params.key + value.size());
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("onError e = " + e);
                setRetry(() -> loadAfter(params, callback));
                networkState.postValue(NetworkState.Companion.error(e.getMessage()));
            }

            @Override
            public void onComplete() {
                Timber.d("onComplete");
            }

        };

        compositeDisposable.add(getUsersFromApiPaging(params.key, params.requestedLoadSize)
                .subscribeWith(disposableObserver));

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, User> callback) {

    }


    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }
}
