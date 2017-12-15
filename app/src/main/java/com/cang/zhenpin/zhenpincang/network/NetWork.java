package com.cang.zhenpin.zhenpincang.network;

import com.cang.zhenpin.zhenpincang.BuildConfig;
import com.cang.zhenpin.zhenpincang.network.gsonconverter.MyGsonConverterFactory;
import com.cang.zhenpin.zhenpincang.network.interceptor.HeadInterceptor;
import com.cang.zhenpin.zhenpincang.network.interceptor.QueryInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by victor on 2017/11/20.
 * Email: wwmdirk@gmail.com
 */

public class NetWork {

    private static BaseApi sBaseApi;
    private static WXApi sWxApi;
    private static OkHttpClient sOkHttpClient;
    private static Converter.Factory sConverterFactory = MyGsonConverterFactory.create();
    private static CallAdapter.Factory sCallAdapterFactory = RxJava2CallAdapterFactory.create();

    public static BaseApi getsBaseApi() {
        if (sBaseApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getsOkHttpClient())
                    .baseUrl("https://api.zhenpincang.com/index.php/home/")
                    .addConverterFactory(sConverterFactory)
                    .addCallAdapterFactory(sCallAdapterFactory)
                    .build();
            sBaseApi = retrofit.create(BaseApi.class);
        }
        return sBaseApi;
    }

    public static WXApi getWxApi() {
        if (sWxApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getsOkHttpClient())
                    .baseUrl("https://api.weixin.qq.com/sns/")
                    .addConverterFactory(sConverterFactory)
                    .addCallAdapterFactory(sCallAdapterFactory)
                    .build();
            sWxApi = retrofit.create(WXApi.class);
        }
        return sWxApi;
    }

    private static OkHttpClient getsOkHttpClient() {
        if (null == sOkHttpClient) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.addInterceptor(new HeadInterceptor())
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS);
//            if (BuildConfig.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY));
//            }
            sOkHttpClient = builder.build();
        }
        return sOkHttpClient;
    }
}
