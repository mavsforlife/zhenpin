package com.cang.zhenpin.zhenpincang.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

@Keep
public class AddOrder {

    public static final int WX_SUCCESS = 0; //成功
    public static final int WX_ERROR = -1; //错误
    public static final int WX_CANCEL = -2; //取消

    /**
     * appid : wxe267c92ee0174373
     * noncestr : vopFFMcYf6IWJBD0TAMTuhi6E0NJXSN6
     * package : Sign=WXPay
     * partnerid : 1493807652
     * prepayid : wx2018031112244681183822640992732285
     * timestamp : 1520742285
     * sign : 98A6DD66219EC3B0CD737EF74008FC1F
     * OrderNO : Z2018031151507
     */

    private String appid;
    private String noncestr;
    @SerializedName("package")
    private String packageName;
    @SerializedName("partnerid")
    private String partnerId;
    @SerializedName("prepayid")
    private String prepayId;
    @SerializedName("timestamp")
    private int timeStamp;
    private String sign;
    @SerializedName("OrderNO")
    private String orderNo;

    @SerializedName("order_str")
    private String mSignOrderStr;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSignOrderStr() {
        return mSignOrderStr;
    }

    public void setSignOrderStr(String signOrderStr) {
        mSignOrderStr = signOrderStr;
    }
}
