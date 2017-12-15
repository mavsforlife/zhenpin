package com.cang.zhenpin.zhenpincang.network.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by victor on 2017/11/22.
 * Email: wwmdirk@gmail.com
 */

public class QueryInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request()
                .newBuilder()
                .url(chain.request().url().newBuilder()
                        .addQueryParameter("appid", "28769828")
                        .addQueryParameter("did", "962f246b7d1115e7647ce90f3ce94579")
                        .addQueryParameter("noncestr", "123456")
                        .addQueryParameter("sync", "0")
                        .addQueryParameter("sync", "token")
                        .build())
                .build());
    }
}
