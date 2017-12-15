package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2017/12/7.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class WxAccessResult {

    /**
     * access_token : ACCESS_TOKEN
     * expires_in : 7200
     * refresh_token : REFRESH_TOKEN
     * openid : OPENID
     * scope : SCOPE
     * unionid : o6_bmasdasdsad6_2sgVt7hMZOPfL
     */

    @SerializedName("access_token")
    private String mAccessToken;
    @SerializedName("expires_in")
    private int mExpiresIn;
    @SerializedName("refresh_token")
    private String mRefreshToken;
    @SerializedName("openid")
    private String mOpenId;
    @SerializedName("scope")
    private String mScope;
    @SerializedName("unionid")
    private String mUnionId;
    /**
     * errcode : 40029
     * errmsg : invalid code
     */

    @SerializedName("errcode")
    private int mErrCode;
    @SerializedName("errmsg")
    private String mErrMsg;

    public String getMAccessToken() {
        return mAccessToken;
    }

    public void setMAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public int getMExpiresIn() {
        return mExpiresIn;
    }

    public void setMExpiresIn(int mExpiresIn) {
        this.mExpiresIn = mExpiresIn;
    }

    public String getMRefreshToken() {
        return mRefreshToken;
    }

    public void setMRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public String getMOpenId() {
        return mOpenId;
    }

    public void setMOpenId(String mOpenId) {
        this.mOpenId = mOpenId;
    }

    public String getMScope() {
        return mScope;
    }

    public void setMScope(String mScope) {
        this.mScope = mScope;
    }

    public String getMUnionId() {
        return mUnionId;
    }

    public void setMUnionId(String mUnionId) {
        this.mUnionId = mUnionId;
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
        return "WxAccessResult{" +
                "mAccessToken='" + mAccessToken + '\'' +
                ", mExpiresIn=" + mExpiresIn +
                ", mRefreshToken='" + mRefreshToken + '\'' +
                ", mOpenId='" + mOpenId + '\'' +
                ", mScope='" + mScope + '\'' +
                ", mUnionId='" + mUnionId + '\'' +
                ", mErrCode=" + mErrCode +
                ", mErrMsg='" + mErrMsg + '\'' +
                '}';
    }
}
