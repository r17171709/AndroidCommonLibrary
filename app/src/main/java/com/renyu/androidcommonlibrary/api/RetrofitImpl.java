package com.renyu.androidcommonlibrary.api;

import com.renyu.androidcommonlibrary.bean.AccessTokenResponse;
import com.renyu.androidcommonlibrary.bean.ExampleAResponse;
import com.renyu.androidcommonlibrary.bean.ExampleResponse;
import com.renyu.androidcommonlibrary.bean.RentResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by renyu on 2017/6/6.
 */

public interface RetrofitImpl {
    @GET("5943e4dc1200000f08fcb4d4")
    Observable<ExampleResponse<ExampleAResponse>> getExampleValue();

    @GET("http://aznapi.house365.com/api/58bf98c1dcb63")
    Observable<RentResponse<AccessTokenResponse>> getAccessToken(@Query("city") String city,
                                                                 @Query("timestamp") int timestamp,
                                                                 @Query("app_id") String app_id,
                                                                 @Query("rand_str") String rand_str,
                                                                 @Query("signature") String signature,
                                                                 @Query("device_id") String device_id,
                                                                 @Header("version") String version,
                                                                 @Header("debug") int debug,
                                                                 @Header("netGet") int netGet,
                                                                 @Header("netGet_cacheTime") int netGet_cacheTime);
}
