package com.cang.zhenpin.zhenpincang.network;

/**
 * Created by victor on 2017/11/20.
 * Email: wwmdirk@gmail.com
 */

public class ApiException extends RuntimeException {
    private int errorCode;
    private String msg;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
