package com.renyu.androidcommonlibrary.impl;

import com.renyu.androidcommonlibrary.bean.ExampleAResponse;
import com.renyu.androidcommonlibrary.bean.ExampleResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by renyu on 2017/6/6.
 */

public interface RetrofitImpl {
    @GET("5943e4dc1200000f08fcb4d4")
    Observable<ExampleResponse<ExampleAResponse>> getExampleValue();
}
