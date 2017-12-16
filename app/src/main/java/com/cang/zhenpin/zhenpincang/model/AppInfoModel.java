package com.cang.zhenpin.zhenpincang.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2017/12/15.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class AppInfoModel implements Parcelable {

    /**
     * Number : 1
     * Address : http://172.16.0.97:7070/android-v4/5.19.21-live/iBiliPlayer-v4-release-5.19.21-live-b1215.2439.apk
     * Vtype : 0
     * Content : 新版本
     */

    @SerializedName("Number")
    private int verCode;
    @SerializedName("Address")
    private String updateUrl;
    @SerializedName("Vtype")
    private int isForce;//0不强制
    @SerializedName("Content")
    private String desc;

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.verCode);
        dest.writeString(this.updateUrl);
        dest.writeInt(this.isForce);
        dest.writeString(this.desc);
    }

    public AppInfoModel() {
    }

    protected AppInfoModel(Parcel in) {
        this.verCode = in.readInt();
        this.updateUrl = in.readString();
        this.isForce = in.readInt();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<AppInfoModel> CREATOR = new Parcelable.Creator<AppInfoModel>() {
        @Override
        public AppInfoModel createFromParcel(Parcel source) {
            return new AppInfoModel(source);
        }

        @Override
        public AppInfoModel[] newArray(int size) {
            return new AppInfoModel[size];
        }
    };
}
