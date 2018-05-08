package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class Order {


    /**
     * ID : 187
     * Name : [20180107]超级好面料中长款面包棉服
     * Quantity : 1
     * attrName : XXL码
     * PicPath : https://api.zhenpincang.com/uploads/151677285242772.jpeg
     * Price : 100.0
     */

    @SerializedName("ID")
    private int id;
    @SerializedName("Name")
    private String name;
    @SerializedName("Quantity")
    private int quantity;
    private String attrName;
    @SerializedName("PicPath")
    private String picPath;
    @SerializedName("Price")
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String mOrderNo;
}
