package com.cang.zhenpin.zhenpincang.ui.orderdetail.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

public class OrderDetailHeader {

    /**
     * ID : 478
     * Address : 收件人:陈君<br>详细地址：上海市闵行区普乐路185号<br>联系电话：18756399877
     * Status : 2
     * OrderNO : Z2018041680380
     * AddDate : 2018-04-16 10:19:17
     * Ispay : 1
     * UID : 1736
     * Quantity : 1
     * AID : 1249
     * VID : 13099
     * attrName : 100码
     * TotalQuantity : 16
     * TypeClassID : 12
     * Name : 雅尚童装
     * VDesc : 雅尚童装 122号 女童连衣裙 咖啡 尺码 90 100 110 120 130 39含代
     * PicPath : ["\/uploads\/152438704776651.jpg","\/uploads\/152438704783784.jpg","\/uploads\/152438704793526.jpg","\/uploads\/152438704754263.jpg"]
     * StartDate : 0000-00-00 00:00:00
     * EndDate : 0000-00-00 00:00:00
     * IsDelete : 1
     * ISBuy : 1
     * Price : 24.00
     * Vcheck : 00
     * StatusName : 待付款
     */

    @SerializedName("ID")
    public String mId;
    @SerializedName("Address")
    public String mAddress;
    @SerializedName("Status")
    public String mStatus;
    @SerializedName("OrderNO")
    public String mOrderNO;
    @SerializedName("AddDate")
    public String mAddDate;
    @SerializedName("Ispay")
    public String mIsPay;
    @SerializedName("UID")
    public String mUId;
    @SerializedName("Quantity")
    public String mQuantity;
    @SerializedName("AID")
    public String mAId;
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
    @SerializedName("PicPath")
    public String mPicPath;
    @SerializedName("StartDate")
    public String mStartDate;
    @SerializedName("EndDate")
    public String mEndDate;
    @SerializedName("IsDelete")
    public String mIsDelete;
    @SerializedName("ISBuy")
    public String mIsBuy;
    @SerializedName("Price")
    public String mPrice;
    @SerializedName("Vcheck")
    public String mVCheck;
    @SerializedName("StatusName")
    public String mStatusName;
}
