package com.giles.android.pagedlistwithnetwork.di;

import com.giles.android.pagedlistwithnetwork.Api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by ggshao on 2018/5/22.
 */

@Module(includes = ViewModelModule.class)
public class AppModule {
    private static String SERVER_URL = "https://api.github.com/";

    @Provides
    @Singleton
    ApiService provideApiService(){
        Timber.d("provideApiService");
        return new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(new LiveDataCallAdapterFactory())  //for retrofit without rxJava
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

    @Provides
    @Singleton
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

}
