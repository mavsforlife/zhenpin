package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class CartBrandList {

    @SerializedName("countall")
    private int countAll;
    @SerializedName("pagesize")
    private int pageSize;
    @SerializedName("pageshow")
    private String pageShow;
    @SerializedName("pageall")
    private int pageAll;
    @SerializedName("pagenow")
    private int pageNow;
    @SerializedName("brand")
    private List<CartBrand> mBrands;

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageShow() {
        return pageShow;
    }

    public void setPageShow(String pageShow) {
        this.pageShow = pageShow;
    }

    public int getPageAll() {
        return pageAll;
    }

    public void setPageAll(int pageAll) {
        this.pageAll = pageAll;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public List<CartBrand> getBrands() {
        return mBrands;
    }

    public void setBrands(List<CartBrand> brands) {
        mBrands = brands;
    }
}
