package com.renyu.androidcommonlibrary.api;

import com.renyu.androidcommonlibrary.bean.AccessTokenResponse;
import com.renyu.androidcommonlibrary.bean.DataListResponse;
import com.renyu.androidcommonlibrary.bean.ExampleAResponse;
import com.renyu.androidcommonlibrary.bean.ExampleResponse;
import com.renyu.androidcommonlibrary.bean.ExampleResponseList;
import com.renyu.androidcommonlibrary.bean.RentResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by renyu on 2017/6/6.
 */

public interface RetrofitImpl {
    @GET("5943e4dc1200000f08fcb4d4")
    Observable<ExampleResponse<ExampleAResponse>> getExampleValue();

    @GET("https://run.mocky.io/v3/349f82af-5011-4573-a037-b38c8243d54d")
    Observable<RentResponse<AccessTokenResponse>> getAccessToken();

    @GET("http://www.mocky.io/v2/5c933487320000ea6e6bd231")
    Observable<ExampleResponseList<DataListResponse>> getDataList();
}
