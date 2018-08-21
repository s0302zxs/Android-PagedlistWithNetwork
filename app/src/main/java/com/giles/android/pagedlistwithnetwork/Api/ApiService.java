package com.giles.android.pagedlistwithnetwork.Api;

import com.giles.android.pagedlistwithnetwork.data.model.User;
import com.giles.android.pagedlistwithnetwork.data.model.UserDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ggshao on 2018/5/21.
 */

public interface ApiService {
    @GET("users")
    Observable<Response<List<User>>> getUsers(@Query("since") int page, @Query("per_page") int perPage);

    @GET("users/{username}")
    Observable<Response<UserDetail>> getUsersDetail(@Path("username") String username);

}
