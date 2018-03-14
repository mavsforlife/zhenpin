package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by victor on 2018/3/12.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class OrderParent {

    /**
     * ID : 65
     * OrderNO : Z2018031276350
     * VID : null
     * AID : null
     * UID : 1662
     * Address : 收件人:海贼王详细地址：好好好好好好好好好土象星座联系电话：18812345678
     * AddID : 22
     * Total_fee : 0
     * Status : 1
     * AddDate : 2018-03-12 00:25:47
     * Ispay : 0
     * PayDate : null
     * Payclass : null
     * Goods_count : 0
     * Statusname : 订单确认
     * orderlist : [{"ID":187,"Name":"[20180107]超级好面料中长款面包棉服","Quantity":1,"attrName":"XXL码","PicPath":"https://api.zhenpincang.com/uploads/151677285242772.jpeg","Price":100},{"ID":188,"Name":"[20180108]为不凡而生，男士秋冬长袖毛衫毛衣哈吉斯男装休闲上衣时尚英伦百搭潮流","Quantity":1,"attrName":"L码","PicPath":"https://api.zhenpincang.com/uploads/151677307358526.jpeg","Price":100},{"ID":189,"Name":"[20180109]超级亮眼的糖果色系，卡通图案印花，宝宝们的最爱呦～","Quantity":1,"attrName":"XXL码","PicPath":"https://api.zhenpincang.com/uploads/151677334363426.jpeg","Price":100}]
     */

    @SerializedName("ID")
    private String id;
    @SerializedName("OrderNO")
    private String orderNO;
    @SerializedName("VID")
    private Object vID;
    @SerializedName("AID")
    private Object aID;
    @SerializedName("UID")
    private String uID;
    @SerializedName("Address")
    private String address;
    @SerializedName("AddID")
    private String addID;
    @SerializedName("Total_fee")
    private double totalFee;
    @SerializedName("Status")
    private int status;
    @SerializedName("AddDate")
    private String addDate;
    @SerializedName("Ispay")
    private int ispay;
    @SerializedName("PayDate")
    private Object payDate;
    @SerializedName("Payclass")
    private Object payclass;
    @SerializedName("Goods_count")
    private int goodsCount;
    @SerializedName("Statusname")
    private String statusName;
    @SerializedName("orderlist")
    private List<Order> orderList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public Object getvID() {
        return vID;
    }

    public void setvID(Object vID) {
        this.vID = vID;
    }

    public Object getaID() {
        return aID;
    }

    public void setaID(Object aID) {
        this.aID = aID;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddID() {
        return addID;
    }

    public void setAddID(String addID) {
        this.addID = addID;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(double totalFee) {
        this.totalFee = totalFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public int getIspay() {
        return ispay;
    }

    public void setIspay(int ispay) {
        this.ispay = ispay;
    }

    public Object getPayDate() {
        return payDate;
    }

    public void setPayDate(Object payDate) {
        this.payDate = payDate;
    }

    public Object getPayclass() {
        return payclass;
    }

    public void setPayclass(Object payclass) {
        this.payclass = payclass;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }
}
