package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2018/3/19.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class BrandForFuck {

    /**
     * ID : 11
     * Desc : 艾格Etam
     * Ctype : 1
     * Cpic : https://api.zhenpincang.com//uploads/152134729398643.jpg
     * IsDelete : 0
     * Aorder : 0
     * OldID :
     * Vtype : 1
     * Check : 0
     */

    @SerializedName("ID")
    public String mId;
    @SerializedName("Desc")
    public String mDesc;
    @SerializedName("Ctype")
    public String mCType;
    @SerializedName("Cpic")
    public String mPic;
    @SerializedName("IsDelete")
    public int mIsDelete;
    @SerializedName("Aorder")
    public String mOrder;
    @SerializedName("OldID")
    public String mOldID;
    @SerializedName("Vtype")
    public int mVtype;
    @SerializedName("Check")
    public String mCheck;
}
