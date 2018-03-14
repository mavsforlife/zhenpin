package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2018/3/12.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class OrderModel {


    /**
     * countall : 2
     * pagesize : 10
     * pageall : 0
     * pagenow : 1
     */

    @SerializedName("countall")
    private int countAll;
    @SerializedName("pagesize")
    private int pageSize;
    @SerializedName("pageall")
    private int pageAll;
    @SerializedName("pagenow")
    private int pageNow;
    @SerializedName("brand")
    private List<OrderParent> orderParents;

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

    public List<OrderParent> getOrderParents() {
        return orderParents;
    }

    public void setOrderParents(List<OrderParent> orderParents) {
        this.orderParents = orderParents;
    }
}
