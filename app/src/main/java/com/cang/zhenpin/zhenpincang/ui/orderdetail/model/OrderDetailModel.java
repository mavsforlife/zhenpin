package com.cang.zhenpin.zhenpincang.ui.orderdetail.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class OrderDetailModel {

    /**
     * ID : 478
     * Quantity : 1
     * OrderNO : Z2018041680380
     * AID : 1249
     * Price : 24.00
     * VID : 13099
     * attrName : 100码
     * TotalQuantity : 16
     * TypeClassID : 12
     * Name : 雅尚童装
     * VDesc : 雅尚童装 122号 女童连衣裙 咖啡 尺码 90 100 110 120 130 39含代
     * PicPath : ["https://api.zhenpincang.com/uploads/152438704776651.jpg","https://api.zhenpincang.com/uploads/152438704783784.jpg","https://api.zhenpincang.com/uploads/152438704793526.jpg","https://api.zhenpincang.com/uploads/152438704754263.jpg"]
     * StartDate : 0000-00-00 00:00:00
     * EndDate : 0000-00-00 00:00:00
     * IsDelete : 1
     * ISBuy : 1
     * Goodsnumber : YS122
     * ProtypeID : 3
     * ISstore : 1
     * Vcheck : 00
     */

    @SerializedName("ID")
    public String mID;
    @SerializedName("Quantity")
    public String mQuantity;
    @SerializedName("OrderNO")
    public String mOrderNO;
    @SerializedName("AID")
    public String mAId;
    @SerializedName("Price")
    public String mPrice;
    @SerializedName("VID")
    public String mVId;
    @SerializedName("attrName")
    public String mAttrName;
    @SerializedName("TotalQuantity")
    public String mTotalQuantity;
    @SerializedName("TypeClassID")
    public String mTypeClassID;
    @SerializedName("Name")
    public String mName;
    @SerializedName("VDesc")
    public String mVDesc;
    @SerializedName("StartDate")
    public String mStartDate;
    @SerializedName("EndDate")
    public String mEndDate;
    @SerializedName("IsDelete")
    public String mIsDelete;
    @SerializedName("ISBuy")
    public String mIsBuy;
    @SerializedName("Goodsnumber")
    public String mGoodsNumber;
    @SerializedName("ProtypeID")
    public String mProtypeID;
    @SerializedName("ISstore")
    public String mIsStore;
    @SerializedName("Vcheck")
    public String mVCheck;
    @SerializedName("PicPath")
    public List<String> mPicPath;

    @SerializedName("Content")
    public String mRemarkStr;
}
