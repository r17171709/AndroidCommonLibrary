package com.renyu.commonlibrary.network;

import android.content.Context;

import com.renyu.commonlibrary.commonutils.ACache;
import com.renyu.commonlibrary.commonutils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by renyu on 2017/3/13.
 */

public class TokenInterceptor implements Interceptor {

    Context context;

    public TokenInterceptor(Context context) {
        this.context=context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response originalResponse=chain.proceed(request);
        originalResponse.code();
        if (originalResponse.body()!=null) {
            String bodyString=getBodyString(originalResponse);
            try {
                JSONObject jsonObject=new JSONObject(bodyString);
                if (jsonObject.getInt("code")==-996) {
                    originalResponse.body().close();
                    OKHttpHelper okHttpHelper=new OKHttpHelper();
                    // https://aznapi.house365.com/api/58bf98c1dcb63?city=nj&timestamp=2489390618&app_id=96625801&rand_str=abcdefghijklmn&signature=dc607b0e91667409d99a42ea241fb6c0&device_id=00000000-7deb-db10-ffff-ffff8dcff671
                    int timestamp= (int) (System.currentTimeMillis()/1000);
                    String random="abcdefghijklmn";
                    String signature="app_id=46877648&app_secret=kCkrePwPpHOsYYSYWTDKzvczWRyvhknG&device_id="+
                            Utils.getUniquePsuedoID()+"&rand_str="+random+"&timestamp="+timestamp;
                    String url="https://aznapi.house365.com/api/58bf98c1dcb63?city=nj&timestamp="+timestamp+
                            "&app_id=46877648&rand_str="+random+
                            "&signature="+Utils.getMD5(signature)+
                            "&device_id="+Utils.getUniquePsuedoID();
                    HashMap<String, String> headMaps=new HashMap<>();
                    headMaps.put("version", "v1.0");
                    Response tokenResponse=okHttpHelper.syncGetRequest(url, headMaps);
                    String value=getBodyString(tokenResponse);
                    if (new JSONObject(value).getInt("code")==1) {
                        String access_token=new JSONObject(value).getJSONObject("data").getString("access_token");
                        ACache.get(context).put("access_token", access_token);
                        tokenResponse.body().close();
                        Request newRequest = request.newBuilder().header("access-token", access_token).build();
                        return chain.proceed(newRequest);
                    }
                    return originalResponse;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return originalResponse;
    }

    public String getBodyString(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            contentType.charset(charset);
        }
        return buffer.clone().readString(charset);
    }
}
