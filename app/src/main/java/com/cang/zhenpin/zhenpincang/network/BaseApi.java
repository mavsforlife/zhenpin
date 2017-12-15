package com.cang.zhenpin.zhenpincang.network;


import com.cang.zhenpin.zhenpincang.model.AppInfoModel;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.BrandList;
import com.cang.zhenpin.zhenpincang.model.UserInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by victor on 2017/11/20.
 * Email: wwmdirk@gmail.com
 */

public interface BaseApi {

    String PAGE = "page";
    String QUERY = "query";
    String BRAND = "brand";
    String OPEN_ID = "openid";
    String NICK_NAME = "nickname";
    String HEAD_IMAGE_URL = "headimgurl";
    String SEX = "sex";
    String PRINCIPAL ="principal";
    String EMAIL = "email";
    String MOBILE = "mobile";
    String UID = "UID";
    String ID = "ID";
    String CONCERN = "concern";
    String CONCERN_CANCEL = "concerncancel";

    @GET("index/brandlist")
    Observable<BaseResult<BrandList>> brandList(@Query(PAGE) int page,
                                                @Query(QUERY) String query,
                                                @Query(BRAND) String brandId,
                                                @Query(UID) int uid);

    @GET("index/concernlist")
    Observable<BaseResult<BrandList>> concernList(@Query(UID) int uId,
                                                  @Query(PAGE) int page);

    @GET("index/wxlogin")
    Observable<BaseResult<UserInfo>> wxLogin(@Query(OPEN_ID) String openId,
                                             @Query(NICK_NAME) String nickName,
                                             @Query(HEAD_IMAGE_URL) String headImgUrl,
                                             @Query(SEX) int sex);

    @GET("index/wxappvip")
    Observable<BaseResult<UserInfo>> requestVip(@Query(OPEN_ID) String openId,
                                      @Query(PRINCIPAL) String principal,
                                      @Query(EMAIL) String email,
                                      @Query(MOBILE) String mobile);

    @GET("index/aboutus")
    Observable<BaseResult<String>> aboutUs();

    @GET("index/contactus")
    Observable<BaseResult<String>> contactUs();

    @GET("index/{path}")
    Observable<BaseResult> attention(@Path("path") String attention,
                                     @Query(UID) int uid,
                                     @Query(ID) String id);

    @GET("index/appversion")
    Observable<BaseResult<AppInfoModel>> getUpdateInfo();
}
