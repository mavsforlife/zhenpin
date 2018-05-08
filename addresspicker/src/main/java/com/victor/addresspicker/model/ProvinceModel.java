package com.victor.addresspicker.model;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ProvinceModel extends DataSupport{

    /**
     * id : 3
     * prov_id : 11
     * prov_name : 上海
     * prov_type : 1
     * prov_state : 1
     * Aorder : 1
     */

    @SerializedName("id")
    public long mId;
    @SerializedName("prov_id")
    public long mProvId;
    @SerializedName("prov_name")
    public String mProvName;
    @SerializedName("prov_type")
    public String mProvType;
    @SerializedName("prov_state")
    public String mProvState;
    @SerializedName("Aorder")
    public String mOrder;
    @SerializedName("city")
    public List<CityModel> mCitys;
}