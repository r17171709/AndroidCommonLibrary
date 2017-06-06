package com.renyu.commonlibrary.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Clevo on 2016/6/20.
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        //拿到之前的Request之后，就可以做其他事情了
        Request.Builder requestBuilder=request.newBuilder();

        Response response=chain.proceed(request);response.code();
        if (response.body()!=null) {
            MediaType contentType=response.body().contentType();
            String bodyString=response.body().string();

            Log.d("LogInterceptor", request.method() + "\n" + request.url() + "\n" + response.code() + "\n" + bodyString);
            // 深坑！
            // 在调用了response.body().string()方法之后，response中的流会被关闭，我们需要创建出一个新的response给应用层处理通过，重新设置body
            ResponseBody responseBody=ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(responseBody).build();
        }
        else {
            return response;
        }
    }
}
