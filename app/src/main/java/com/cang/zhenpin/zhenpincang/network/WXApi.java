package com.cang.zhenpin.zhenpincang.network;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.WxAccessResult;
import com.cang.zhenpin.zhenpincang.model.WxUserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by victor on 2017/12/6.
 * Email: wwmdirk@gmail.com
 */

public interface WXApi {

    @GET("oauth2/access_token")
    Observable<WxAccessResult> getAccessToken(@Query("appid") String appid,
                                              @Query("secret") String secret,
                                              @Query("code") String code,
                                              @Query("grant_type") String grant_type);

    @GET("userinfo")
    Observable<WxUserInfo> getUserInfo(@Query("access_token") String access_token,
                                       @Query("openid") String openid,
                                       @Query("lang") String lang);
}
