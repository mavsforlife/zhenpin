package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2017/12/7.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class WxUserInfo {

    /**
     * openid : OPENID
     * nickname : NICKNAME
     * sex : 1
     * province : PROVINCE
     * city : CITY
     * country : COUNTRY
     * headimgurl : http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0
     * privilege : ["PRIVILEGE1","PRIVILEGE2"]
     * unionid :  o6_bmasdasdsad6_2sgVt7hMZOPfL
     */

    @SerializedName("openid")
    private String mOpenId;
    @SerializedName("nickname")
    private String mNickName;
    @SerializedName("sex")
    private int mSex;
    @SerializedName("province")
    private String mProvince;
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("headimgurl")
    private String mHeadImgUrl;
    @SerializedName("unionid")
    private String mUnionId;
    @SerializedName("privilege")
    private List<String> mPrivilege;
    /**
     * errcode : 40029
     * errmsg : invalid code
     */

    @SerializedName("errcode")
    private int mErrCode;
    @SerializedName("errmsg")
    private String mErrMsg;

    public String getMOpenId() {
        return mOpenId;
    }

    public void setMOpenId(String mOpenId) {
        this.mOpenId = mOpenId;
    }

    public String getMNickName() {
        return mNickName;
    }

    public void setMNickName(String mNickName) {
        this.mNickName = mNickName;
    }

    public int getMSex() {
        return mSex;
    }

    public void setMSex(int mSex) {
        this.mSex = mSex;
    }

    public String getMProvince() {
        return mProvince;
    }

    public void setMProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getMCity() {
        return mCity;
    }

    public void setMCity(String mCity) {
        this.mCity = mCity;
    }

    public String getMCountry() {
        return mCountry;
    }

    public void setMCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getMHeadImgUrl() {
        return mHeadImgUrl;
    }

    public void setMHeadImgUrl(String mHeadImgUrl) {
        this.mHeadImgUrl = mHeadImgUrl;
    }

    public String getMUnionId() {
        return mUnionId;
    }

    public void setMUnionId(String mUnionId) {
        this.mUnionId = mUnionId;
    }

    public List<String> getMPrivilege() {
        return mPrivilege;
    }

    public void setMPrivilege(List<String> mPrivilege) {
        this.mPrivilege = mPrivilege;
    }

    public int getMErrCode() {
        return mErrCode;
    }

    public void setMErrCode(int mErrCode) {
        this.mErrCode = mErrCode;
    }

    public String getMErrMsg() {
        return mErrMsg;
    }

    public void setMErrMsg(String mErrMsg) {
        this.mErrMsg = mErrMsg;
    }

    @Override
    public String toString() {
        return "WxUserInfo{" +
                "mOpenId='" + mOpenId + '\'' +
                ", mNickName='" + mNickName + '\'' +
                ", mSex=" + mSex +
                ", mProvince='" + mProvince + '\'' +
                ", mCity='" + mCity + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mHeadImgUrl='" + mHeadImgUrl + '\'' +
                ", mUnionId='" + mUnionId + '\'' +
                ", mPrivilege=" + mPrivilege +
                ", mErrCode=" + mErrCode +
                ", mErrMsg='" + mErrMsg + '\'' +
                '}';
    }
}
