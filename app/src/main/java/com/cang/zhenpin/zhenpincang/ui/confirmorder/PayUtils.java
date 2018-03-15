package com.cang.zhenpin.zhenpincang.ui.confirmorder;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cang.zhenpin.zhenpincang.model.AddOrder;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class PayUtils {

    public static final String RESULT_STATUS_SUCCESS = "9000";   // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
    public static final String RESULT_STATUS_LOADING = "8000";   // 判断resultStatus 为非"9000"则代表可能支付失败 "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    public static final String RESULT_NOT_INSTALLED = "4000";       // 未安装支付宝

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

    static void getAliPayResult(final WeakReference<ConfirmOrderActivity> ref, final AddOrder data) {

        Observable.just(data.getNoncestr())
                .map(new Function<String, Map<String, String>>() {
                    @Override
                    public Map<String, String> apply(String s) throws Exception {
                        ConfirmOrderActivity act = ref.get();
                        if (act != null) {
                            PayTask aliPay = new PayTask(act);
                            return aliPay.payV2(s, true);
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, String> map) {

                        if (map != null) {

                            ConfirmOrderActivity act = ref.get();

                            if (act != null) {
                                AliPayResult result = new AliPayResult(map);

                                String resultInfo = result.getResult();// 同步返回需要验证的信息
                                String resultStatus = result.getResultStatus();
                                // 判断resultStatus 为9000则代表支付成功
                                if (TextUtils.equals(resultStatus, RESULT_STATUS_SUCCESS) ||
                                        TextUtils.equals(resultStatus, RESULT_STATUS_LOADING)) {
                                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                                    act.onCheckPaySuccess();
                                } else if (TextUtils.equals(resultStatus, RESULT_NOT_INSTALLED)) {
                                    Toast.makeText(act, "你还未安装支付宝！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
