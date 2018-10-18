package com.renyu.androidcommonlibrary.di.module;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.renyu.androidcommonlibrary.ExampleApp;
import com.renyu.androidcommonlibrary.api.RetrofitImpl;
import com.renyu.androidcommonlibrary.db.PlainTextDBHelper;
import com.renyu.commonlibrary.network.HttpsUtils;
import com.renyu.commonlibrary.network.OKHttpHelper;
import com.renyu.commonlibrary.network.OKHttpUtils;
import com.renyu.commonlibrary.network.Retrofit2Helper;
import com.renyu.commonlibrary.network.Retrofit2Utils;
import com.tencent.wcdb.database.SQLiteDatabase;

import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module
public class ApiModule {

    /**
     * 提供HttpClient对象
     * @param application
     * @return
     */
    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClientBuilder(ExampleApp application) {
        OkHttpClient.Builder baseBuilder = new OkHttpClient.Builder()
                // 限制抓包
                .proxy(Proxy.NO_PROXY)
//                    .addInterceptor(new TokenInterceptor(this))
                .addInterceptor(new ChuckInterceptor(application))
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS);
        HttpsUtils.SSLParams sslParams= HttpsUtils.getSslSocketFactory(null, null, null);
        baseBuilder.hostnameVerifier((s, sslSession) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return baseBuilder.build();
    }

    /**
     * 提供Retrofit对象
     * @param okHttpClient
     * @return
     */
    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit2Helper retrofit2Utils=Retrofit2Helper.getInstance("http://www.mocky.io/v2/");
        retrofit2Utils.addBaseOKHttpClient(okHttpClient);
        retrofit2Utils.baseBuild();
        return retrofit2Utils.getBaseRetrofit();
    }

    /**
     * 提供RetrofitImpl接口
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    public RetrofitImpl provideRetrofitImpl(Retrofit retrofit) {
        return retrofit.create(RetrofitImpl.class);
    }

    /**
     * 提供OKHttpUtils
     * @return
     */
    @Singleton
    @Provides
    public OKHttpUtils provideOKHttpUtils() {
        return OKHttpHelper.getInstance().getOkHttpUtils();
    }

    /**
     * 提供wcdb对象
     * @param application
     * @return
     */
    @Singleton
    @Provides
    public SQLiteDatabase provideSQLiteDatabase(ExampleApp application) {
        PlainTextDBHelper dbHelper=new PlainTextDBHelper(application);
        dbHelper.setWriteAheadLoggingEnabled(true);
        return dbHelper.getWritableDatabase();
    }
}
