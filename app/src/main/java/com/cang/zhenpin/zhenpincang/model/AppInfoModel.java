package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2017/12/15.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class AppInfoModel {

    /**
     * Number : 1
     * Address : http://172.16.0.97:7070/android-v4/5.19.21-live/iBiliPlayer-v4-release-5.19.21-live-b1215.2439.apk
     * Vtype : 0
     */

    @SerializedName("Number")
    private int verCode;
    @SerializedName("Address")
    private String updateUrl;
    @SerializedName("Vtype")
    private int isForce;//0不强制

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public int getIsForce() {
        return isForce;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }
}
