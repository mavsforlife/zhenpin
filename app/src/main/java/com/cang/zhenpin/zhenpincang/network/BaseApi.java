package com.cang.zhenpin.zhenpincang.network;


import com.cang.zhenpin.zhenpincang.model.AddOrder;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.model.AddressList;
import com.cang.zhenpin.zhenpincang.model.AppInfoModel;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.BrandForFuckList;
import com.cang.zhenpin.zhenpincang.model.BrandList;
import com.cang.zhenpin.zhenpincang.model.CartBrandList;
import com.cang.zhenpin.zhenpincang.model.OrderDetail;
import com.cang.zhenpin.zhenpincang.model.OrderModel;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.victor.addresspicker.model.ProvinceModel;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    String PRINCIPAL = "principal";
    String EMAIL = "email";
    String MOBILE = "mobile";
    String UID = "UID";
    String ID = "ID";
    String CONCERN = "concern";
    String CONCERN_CANCEL = "concerncancel";
    String OS = "os";
    String AID = "AID";
    String QUANTITY = "Quantity";
    String ADDRESS = "Address";
    String ADDRESSID = "AddressID";
    String MOBILE_ADDRESS = "Mobile";
    String NAME = "Name";
    String IS_DEFAULT = "ISDefault";
    String PAY_CLASS = "payclass";
    String TOTAL_FEE = "total_fee";
    String ORDER_NO = "OrderNO";
    String TYPE = "type";
    String CONTENT = "Content";

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
    Observable<BaseResult<AppInfoModel>> getUpdateInfo(@Query(OS) int os);

    @GET("index/CartAdd")
    Observable<BaseResult> addToShoppingCart(@Query(UID) int uid,
                                             @Query(AID) int aid,
                                             @Query(QUANTITY) int quantity,
                                             @Query(CONTENT) String tip);

    @GET("index/CartDel")
    Observable<BaseResult> delFromShoppingCart(@Query(UID) int uid,
                                               @Query(ID) String id);

    @GET("index/cartlist")
    Observable<BaseResult<CartBrandList>> getCartList(@Query(UID) int uid);

    @GET("index/AddressAdd")
    Observable<BaseResult<Address>> modifyOrAddAddress(@Query(UID) int uid,
                                                       @Query(ADDRESS) String address,
                                                       @Query(MOBILE_ADDRESS) String mobile,
                                                       @Query(NAME) String name,
                                                       @Query(IS_DEFAULT) int isDefault,
                                                       @Query(ID) String id);

    @GET("index/AddressAdd")
    Observable<BaseResult<Address>> modifyOrAddAddress(@QueryMap Map<String, String> map);

    @GET("index/AddressList")
    Observable<BaseResult<AddressList>> getAddressList(@Query(UID) int uid);

    @GET("index/AddressDel")
    Observable<BaseResult> delAddress(@Query(ID) String id);

    @GET("index/catQuantity")
    Observable<BaseResult<String>> modifyShoppingCartItem(@Query(ID) String id,
                                                          @Query(QUANTITY) int quantity);

    @GET("index/OrderAdd")
    Observable<BaseResult<AddOrder>> addOrder(@Query(UID) int uid,
                                              @Query(ID) String id,
                                              @Query(QUANTITY) int quantity,
                                              @Query(ADDRESSID) String addressId,
                                              @Query(PAY_CLASS) int payType);

    @GET("index/OrderDel")
    Observable<BaseResult> delOrder(@Query(UID) int uid,
                                    @Query(ID) String id);

    @GET("index/OrderList")
    Observable<BaseResult<OrderModel>> getOrderList(@Query(UID) int uId,
                                                    @Query(PAGE) int page);

    @GET("index/chekorder")
    Observable<BaseResult> checkPayStatus(@Query(ORDER_NO) String orderNo);

    @GET("OrderPay/index")
    Observable<BaseResult<AddOrder>> payFromInvoice(@Query(ID) String id,
                                                    @Query(ADDRESSID) String addressId,
                                                    @Query(PAY_CLASS) int payType);

    @GET("index/showtip")
    Observable<BaseResult<String>> getShowTip(@Query(TYPE) int type);

    @GET("index/rebrand")
    Observable<BaseResult<BrandForFuckList>> getFuckList();

    @GET("index/provincecitylist")
    Observable<BaseResult<List<ProvinceModel>>> getProvinceList();

    @GET("order/Detail")
    Observable<BaseResult<OrderDetail>> getOrderDetail(@Query(ORDER_NO) String orderNo);

}
