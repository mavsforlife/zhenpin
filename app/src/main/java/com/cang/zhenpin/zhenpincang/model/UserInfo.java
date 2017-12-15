package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2017/12/7.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class UserInfo {

    public static final String TYPE_NOMAL = "普通用户";
    public static final String TYPE_AGENT = "代理用户";
    public static final String TYPE_APPLY_ING = "申请中";
    public static final String TYPE_REJECT = "审核不通过";

    public static final int TYPE_NOMA_INTL = 1;
    public static final int TYPE_AGENT_INT = 2;
    public static final int TYPE_APPLY_ING_INT = 3;
    public static final int TYPE_REJECT_INT = 4;
    /**
     * ID : 1659
     * OpenID : 22222
     * UserID :
     * Password :
     * Token :
     * Headimgurl : http:\www.baidu.com
     * Principal : weixin
     * Sex : 1
     * Email :
     * Mobile :
     * Remark :
     * RegistTime : 2017-12-07 22:37:36
     * IsDelete : 0
     * IsAdmin : 0
     * Usertype : 1
     * AppDate :
     * CheckDate :
     * Weixin_Name :
     * UserCode : 001659
     */

    @SerializedName("ID")
    private int mId;
    @SerializedName("OpenID")
    private String mOpenId;
    @SerializedName("UserID")
    private String mUserId;
    @SerializedName("Password")
    private String mPassword;
    private String Token;
    @SerializedName("Headimgurl")
    private String mHeadImgUrl;
    @SerializedName("Principal")
    private String mPrincipal;
    @SerializedName("Sex")
    private int mSex;
    @SerializedName("Email")
    private String mEmail;
    @SerializedName("Mobile")
    private String mMobile;
    @SerializedName("Remark")
    private String mRemark;
    @SerializedName("RegistTime")
    private String mRegistTime;
    @SerializedName("IsDelete")
    private String mIsDelete;
    @SerializedName("IsAdmin")
    private String mIsAdmin;
    @SerializedName("Usertype")
    private int mUserType;//1.注册用户 2.代理用户 3.审核中 4.申请不通过
    @SerializedName("AppDate")
    private String mAppDate;
    @SerializedName("CheckDate")
    private String mCheckDate;
    @SerializedName("Weixin_Name")
    private String mWeixinName;
    @SerializedName("UserCode")
    private String mUserCode;

    public int getMId() {
        return mId;
    }

    public void setMId(int mId) {
        this.mId = mId;
    }

    public String getMOpenId() {
        return mOpenId;
    }

    public void setMOpenId(String mOpenId) {
        this.mOpenId = mOpenId;
    }

    public String getMUserId() {
        return mUserId;
    }

    public void setMUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getMPassword() {
        return mPassword;
    }

    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getMHeadImgUrl() {
        return mHeadImgUrl;
    }

    public void setMHeadImgUrl(String mHeadImgUrl) {
        this.mHeadImgUrl = mHeadImgUrl;
    }

    public String getMPrincipal() {
        return mPrincipal;
    }

    public void setMPrincipal(String mPrincipal) {
        this.mPrincipal = mPrincipal;
    }

    public int getMSex() {
        return mSex;
    }

    public void setMSex(int mSex) {
        this.mSex = mSex;
    }

    public String getMEmail() {
        return mEmail;
    }

    public void setMEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getMMobile() {
        return mMobile;
    }

    public void setMMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getMRemark() {
        return mRemark;
    }

    public void setMRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public String getMRegistTime() {
        return mRegistTime;
    }

    public void setMRegistTime(String mRegistTime) {
        this.mRegistTime = mRegistTime;
    }

    public String getMIsDelete() {
        return mIsDelete;
    }

    public void setMIsDelete(String mIsDelete) {
        this.mIsDelete = mIsDelete;
    }

    public String getMIsAdmin() {
        return mIsAdmin;
    }

    public void setMIsAdmin(String mIsAdmin) {
        this.mIsAdmin = mIsAdmin;
    }

    public int getMUserType() {
        return mUserType;
    }

    public void setMUsertype(int mUsertype) {
        this.mUserType = mUsertype;
    }

    public String getMAppDate() {
        return mAppDate;
    }

    public void setMAppDate(String mAppDate) {
        this.mAppDate = mAppDate;
    }

    public String getMCheckDate() {
        return mCheckDate;
    }

    public void setMCheckDate(String mCheckDate) {
        this.mCheckDate = mCheckDate;
    }

    public String getMWeixinName() {
        return mWeixinName;
    }

    public void setMWeixinName(String mWeixinName) {
        this.mWeixinName = mWeixinName;
    }

    public String getMUserCode() {
        return mUserCode;
    }

    public void setMUserCode(String mUserCode) {
        this.mUserCode = mUserCode;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "mId=" + mId +
                ", mOpenId='" + mOpenId + '\'' +
                ", mUserId='" + mUserId + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", Token='" + Token + '\'' +
                ", mHeadImgUrl='" + mHeadImgUrl + '\'' +
                ", mPrincipal='" + mPrincipal + '\'' +
                ", mSex=" + mSex +
                ", mEmail='" + mEmail + '\'' +
                ", mMobile='" + mMobile + '\'' +
                ", mRemark='" + mRemark + '\'' +
                ", mRegistTime='" + mRegistTime + '\'' +
                ", mIsDelete='" + mIsDelete + '\'' +
                ", mIsAdmin='" + mIsAdmin + '\'' +
                ", mUserType=" + mUserType +
                ", mAppDate='" + mAppDate + '\'' +
                ", mCheckDate='" + mCheckDate + '\'' +
                ", mWeixinName='" + mWeixinName + '\'' +
                ", mUserCode='" + mUserCode + '\'' +
                '}';
    }
}
