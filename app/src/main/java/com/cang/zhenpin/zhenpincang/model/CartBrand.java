package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2018/3/4.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class CartBrand {

    /**
     * ID : 97
     * UID : 1662
     * Quantity : 1
     * AID : 370
     * AddDate : 2018-03-04 12:44:04
     * VID : 67
     * AttrName : XXL码
     * TotalQuantity : 100
     * TypeClassID : 8
     * Name : [20180108]为不凡而生，男士秋冬长袖毛衫毛衣哈吉斯男装休闲上衣时尚英伦百搭潮流
     * VDesc : [20180108]为不凡而生，男士秋冬长袖毛衫毛衣哈吉斯男装休闲上衣时尚英伦百搭潮流，T猫价2200超级无敌舒服的一款圆领毛衣，毛衣最最重要的就是手感和舒适度，所以选用羊毛混纺材质 手感超级好，重点是很亲肤不会扎人，非常柔软！因为用的料够好所以绝对没有异味！绝对不是市面上一般的能比的，高品质的质量拿到手你就知道它有多值！！！绝对超出你的想象！！！ 这种看似简单的款式 超级百搭，不论单穿还是内搭 都非常棒！所以自留了 、这种款不怕多 好穿又好看 比一般的卫衣打底衫实在好看！秋冬必备单品！！！弹性大
     * PicPath : https://api.zhenpincang.com/uploads/151677307358526.jpeg
     * StartDate :
     * EndDate :
     * IsDelete : 0
     * ISBuy : 1
     * Price : 100
     */

    @SerializedName("ID")
    private String mId;
    @SerializedName("UID")
    private String mUId;
    @SerializedName("Quantity")
    private int mQuantity;
    @SerializedName("AID")
    private String mAId;
    @SerializedName("AddDate")
    private String mAddDate;
    @SerializedName("VID")
    private String mVID;
    @SerializedName("AttrName")
    private String mAttrName;
    @SerializedName("TotalQuantity")
    private String mTotalQuantity;
    @SerializedName("TypeClassID")
    private String mTypeClassID;
    @SerializedName("Name")
    private String mName;
    @SerializedName("VDesc")
    private String mVDesc;
    @SerializedName("PicPath")
    private String mPicPath;
    @SerializedName("StartDate")
    private String mStartDate;
    @SerializedName("EndDate")
    private String mEndDate;
    @SerializedName("IsDelete")
    private String mIsDelete;
    @SerializedName("ISBuy")
    private String mISBuy;
    @SerializedName("Price")
    private float mPrice;

    private boolean mIsChecked = true;
    private boolean mDelChecked = false;

    public String getMId() {
        return mId;
    }

    public void setMId(String mId) {
        this.mId = mId;
    }

    public String getMUId() {
        return mUId;
    }

    public void setMUId(String mUId) {
        this.mUId = mUId;
    }

    public int getMQuantity() {
        return mQuantity;
    }

    public void setMQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getMAId() {
        return mAId;
    }

    public void setMAId(String mAId) {
        this.mAId = mAId;
    }

    public String getMAddDate() {
        return mAddDate;
    }

    public void setMAddDate(String mAddDate) {
        this.mAddDate = mAddDate;
    }

    public String getMVID() {
        return mVID;
    }

    public void setMVID(String mVID) {
        this.mVID = mVID;
    }

    public String getMAttrName() {
        return mAttrName;
    }

    public void setMAttrName(String mAttrName) {
        this.mAttrName = mAttrName;
    }

    public String getMTotalQuantity() {
        return mTotalQuantity;
    }

    public void setMTotalQuantity(String mTotalQuantity) {
        this.mTotalQuantity = mTotalQuantity;
    }

    public String getMTypeClassID() {
        return mTypeClassID;
    }

    public void setMTypeClassID(String mTypeClassID) {
        this.mTypeClassID = mTypeClassID;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getMVDesc() {
        return mVDesc;
    }

    public void setMVDesc(String mVDesc) {
        this.mVDesc = mVDesc;
    }

    public String getMPicPath() {
        return mPicPath;
    }

    public void setMPicPath(String mPicPath) {
        this.mPicPath = mPicPath;
    }

    public String getMStartDate() {
        return mStartDate;
    }

    public void setMStartDate(String mStartDate) {
        this.mStartDate = mStartDate;
    }

    public String getMEndDate() {
        return mEndDate;
    }

    public void setMEndDate(String mEndDate) {
        this.mEndDate = mEndDate;
    }

    public String getMIsDelete() {
        return mIsDelete;
    }

    public void setMIsDelete(String mIsDelete) {
        this.mIsDelete = mIsDelete;
    }

    public String getMISBuy() {
        return mISBuy;
    }

    public void setMISBuy(String mISBuy) {
        this.mISBuy = mISBuy;
    }

    public float getMPrice() {
        return mPrice;
    }

    public void setMPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public boolean isDelChecked() {
        return mDelChecked;
    }

    public void setDelChecked(boolean delChecked) {
        mDelChecked = delChecked;
    }
}
