package com.giles.android.pagedlistwithnetwork.ui.userDetail;

import com.giles.android.pagedlistwithnetwork.Api.ApiService;
import com.giles.android.pagedlistwithnetwork.data.model.UserDetail;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@Singleton
public class UserDetailRepository {
    private final ApiService apiService;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public UserDetailRepository(ApiService apiService, CompositeDisposable compositeDisposable) {
        this.apiService = apiService;
        this.compositeDisposable = compositeDisposable;
    }


    public Observable<UserDetail> getUserDetail(String username){
        return getUserDetailFromApi(username);
    }


    public Observable<UserDetail> getUserDetailFromApi(final String username){
        return apiService.getUsersDetail(username)
                .subscribeOn(Schedulers.io())
                .map(new Function<Response<UserDetail>, UserDetail>() {
                    @Override
                    public UserDetail apply(Response<UserDetail> userDetailResponse) throws Exception {
                        return userDetailResponse.body();
                    }
                });
    }
}
