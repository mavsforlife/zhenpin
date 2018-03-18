package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class Brand {

    public static final int NOT_ATTENTION = 0;
    public static final int ATTENTION = 1;
    /**
     * ID : 80
     * TypeClassID : 10
     * Name : 2018年货节
     * Desc : 随着春节的到来年味越来越浓各位小伙伴是不是早已经“无心上班，只想过年”先稳住躁动的心，小臻弱弱的问一句“家里的年货，过年的新衣准备好了吗？—年货节—年终大型仓储特卖会品牌鞋服大联合 为你备足年货
     * PicPath : ["https://api.zhenpincang.com/uploads/151755043394545.jpeg","https://api.zhenpincang.com/uploads/151755055639470.png","https://api.zhenpincang.com/uploads/151755073724941.png"]
     * UploadUser : 0
     * UploadTime : null
     * IsDelete : 0
     * StartDate : 0000-00-00 00:00:00
     * EndDate : 0000-00-00 00:00:00
     * ViewCount : null
     * ADate : 2018-02-02 13:49:35
     * ISBuy : 0
     * Price : 0
     * Check : 00
     * brandname : 臻品购
     * brandid : 10
     * brandpic : https://api.zhenpincang.com//uploads/151755085970938.png
     * InfoType : 1
     * Iscon : 0
     * IsconDesc : 未关注
     * Attribute : []
     */
    /**
     * ID : 6
     * TypeClassID : 2
     * Name : sss
     * Desc : sss
     * PicPath : ["https://api.zhenpincang.com/uploads/151151088158919.jpg","https://api.zhenpincang.com/uploads/151151088136283.jpg","https://api.zhenpincang.com/uploads/151151088176339.jpg"]
     * UploadUser :
     * UploadTime :
     * IsDelete : 0
     * StartDate : 0000-00-00 00:00:00
     * EndDate : 0000-00-00 00:00:00
     * ViewCount : 1
     * ADate : 2017-11-24 16:08:03
     * brandname : 耐克
     * brandid : 2
     * brandpic : https://api.zhenpincang.com/uploads/1.png
     */

    @SerializedName("ID")
    private String id;
    @SerializedName("TypeClassID")
    private String typeClassID;
    @SerializedName("Name")
    private String name;
    @SerializedName("Desc")
    private String desc;
    @SerializedName("UploadUser")
    private String uploadUser;
    @SerializedName("UploadTime")
    private String uploadTime;
    @SerializedName("IsDelete")
    private String isDelete;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("ViewCount")
    private int viewCount;
    @SerializedName("ADate")
    private String aDate;
    @SerializedName("brandname")
    private String brandName;
    @SerializedName("brandid")
    private String brandId;
    @SerializedName("brandpic")
    private String brandPic;
    @SerializedName("PicPath")
    private List<String> picPath;
    @SerializedName("InfoType")
    private int mInfoType; //0-正在进行 1-过期 2-预告
    @SerializedName("Iscon")
    private int mIsAttention;//0-未关注 1-已关注
    private boolean isCollapsed = true;

    @SerializedName("ISBuy")
    private int mIsBuy;
    @SerializedName("Price")
    private double mPrice;
    @SerializedName("Check")
    private String mCheck;
    @SerializedName("IsconDesc")
    private String mIsconDesc;
    @SerializedName("Attribute")
    private List<Attribute> mAttribute;

    @Keep
    public static class Attribute {

        /**
         * ID : 368
         * Name : L码
         * Quantity : 0
         */

        @SerializedName("ID")
        private int mID;
        @SerializedName("Name")
        private String mName;
        @SerializedName("Quantity")
        private int mQuantity;

        public int getMID() {
            return mID;
        }

        public void setMID(int mID) {
            this.mID = mID;
        }

        public String getMName() {
            return mName;
        }

        public void setMName(String mName) {
            this.mName = mName;
        }

        public int getMQuantity() {
            return mQuantity;
        }

        public void setMQuantity(int mQuantity) {
            this.mQuantity = mQuantity;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeClassID() {
        return typeClassID;
    }

    public void setTypeClassID(String typeClassID) {
        this.typeClassID = typeClassID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUploadUser() {
        return uploadUser;
    }

    public void setUploadUser(String uploadUser) {
        this.uploadUser = uploadUser;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getADate() {
        return aDate;
    }

    public void setADate(String aDate) {
        this.aDate = aDate;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandPic() {
        return brandPic;
    }

    public void setBrandPic(String brandPic) {
        this.brandPic = brandPic;
    }

    public List<String> getPicPath() {
        return picPath;
    }

    public void setPicPath(List<String> picPath) {
        this.picPath = picPath;
    }

    public int getInfoType() {
        return mInfoType;
    }

    public void setInfoType(int infoType) {
        mInfoType = infoType;
    }

    public int getIsAttention() {
        return mIsAttention;
    }

    public void setIsAttention(int isAttention) {
        mIsAttention = isAttention;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public void setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
    }

    public int getIsBuy() {
        return mIsBuy;
    }

    public void setIsBuy(int isBuy) {
        mIsBuy = isBuy;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getCheck() {
        return mCheck;
    }

    public void setCheck(String check) {
        mCheck = check;
    }

    public String getIsconDesc() {
        return mIsconDesc;
    }

    public void setIsconDesc(String isconDesc) {
        mIsconDesc = isconDesc;
    }

    public List<Attribute> getAttribute() {
        return mAttribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        mAttribute = attribute;
    }
}
