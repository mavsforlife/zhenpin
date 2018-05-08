package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.cang.zhenpin.zhenpincang.ui.orderdetail.model.OrderDetailModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class OrderDetail {

    @SerializedName("detail")
    public Detail mDetail;

    @Keep
    public static class Detail{
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

        @SerializedName("list")
        public List<OrderDetailModel> mList;
    }
}
