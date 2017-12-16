package com.cang.zhenpin.zhenpincang.network.interceptor;

import com.cang.zhenpin.zhenpincang.base.App;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by victor on 2017/11/22.
 * Email: wwmdirk@gmail.com
 */

public class HeadInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request()
                .newBuilder()
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("PHONE-MODEL", DeviceUtil.getPhoneModel())
                .addHeader("PHONE-BRAND", DeviceUtil.getPhoneBrand())
                .addHeader("OS", "android")
                .addHeader("OS-VERSION", DeviceUtil.getBuildVersion())
                .addHeader("APP-VERSION-CODE", DeviceUtil.getVersionCode())
                .addHeader("APP-VERSION", DeviceUtil.getVersionName())
                .addHeader("Content-Type", "application/json")
                .build());
    }
}
