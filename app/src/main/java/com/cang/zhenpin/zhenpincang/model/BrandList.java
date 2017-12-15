package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */
@Keep
public class BrandList {
    /**
     * notice : 这是条公告内容
     * countall : 6
     * pagesize : 10
     * pageshow :
     * pageall : 1
     * pagenow : 1
     */

    private String notice;
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
    private List<Brand> mBrands;
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

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

    public List<Brand> getBrands() {
        return mBrands;
    }

    public void setBrands(List<Brand> brands) {
        mBrands = brands;
    }
}
