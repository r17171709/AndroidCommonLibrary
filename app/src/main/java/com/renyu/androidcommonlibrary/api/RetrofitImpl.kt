package com.renyu.androidcommonlibrary.api

import com.renyu.androidcommonlibrary.bean.*
import io.reactivex.Observable
import retrofit2.http.GET

interface RetrofitImpl {
    @GET("https://run.mocky.io/v3/349f82af-5011-4573-a037-b38c8243d54d")
    fun getAccessToken(): Observable<RentResponse<AccessTokenResponse>>

    @GET("http://www.mocky.io/v2/5c933487320000ea6e6bd231")
    fun getDataList(): Observable<ExampleResponseList<DataListResponse>>

    @GET("https://run.mocky.io/v3/349f82af-5011-4573-a037-b38c8243d54d")
    suspend fun getAccessToken2(): RentResponse<AccessTokenResponse>
}