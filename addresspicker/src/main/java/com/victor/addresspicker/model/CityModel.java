package com.victor.addresspicker.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class CityModel {

    /**
     * CityID : 42
     * CityName : 黄浦区
     * CityFee : 5
     */

    private ProvinceModel mProvinceModel;

    @SerializedName("CityID")
    public int mCityId;
    @SerializedName("CityName")
    public String mCityName;
    @SerializedName("CityFee")
    public int mCityFee;

    public long mProvinceId;
}