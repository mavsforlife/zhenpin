package com.cang.zhenpin.zhenpincang.network.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by victor on 2017/12/14.
 * Email: wwmdirk@gmail.com
 */

public class DownloadProgressInterceptor implements Interceptor {

    private DownloadProgressListener listener;

    public DownloadProgressInterceptor(DownloadProgressListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new DownlaodResponseBody(originalResponse.body(), listener))
                .build();
    }
}
