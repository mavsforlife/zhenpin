package com.cang.zhenpin.zhenpincang.ui.confirmorder;

import com.cang.zhenpin.zhenpincang.model.AddOrder;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class PayUtils {

    static PayReq wxPay(AddOrder data) {
        PayReq req = new PayReq();
        req.appId = data.getAppid();
        req.nonceStr = data.getNoncestr();
        req.packageValue = data.getPackageName();
        req.partnerId = data.getPartnerId();
        req.prepayId = data.getPrepayId();
        req.timeStamp = String.valueOf(data.getTimeStamp());
        req.sign = data.getSign();
        return req;
    }
}
