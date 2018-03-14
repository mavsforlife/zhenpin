package com.cang.zhenpin.zhenpincang.event;

/**
 * Created by victor on 2016/7/1.
 * 发送微信支付response code
 */
public class WxPayEvent {

    public int code;

    public WxPayEvent(int code) {
        this.code = code;
    }

}
