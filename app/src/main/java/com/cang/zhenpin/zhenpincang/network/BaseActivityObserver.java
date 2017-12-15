package com.cang.zhenpin.zhenpincang.network;

import android.content.Context;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.BaseView;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by victor on 2017/11/20.
 * Email: wwmdirk@gmail.com
 */

public class BaseActivityObserver<T> implements Observer<T> {

    private Context mContext;

    public BaseActivityObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (isCancel()) return;
    }

    @Override
    public void onError(Throwable e) {
        if (isCancel()) return;
        if (e instanceof ApiException) {
            ToastUtil.showShort(mContext, ((ApiException) e).getMsg());
        } else if (e instanceof UnknownHostException) {
            ToastUtil.showShort(mContext, R.string.please_open_network);
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.showShort(mContext, R.string.request_time_out);
        } else if (e instanceof ConnectException) {
            ToastUtil.showShort(mContext, R.string.connect_fail);
        } else if (e instanceof HttpException) {
            ToastUtil.showShort(mContext, R.string.request_time_out);
        }
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        if (isCancel()) return;
    }

    protected boolean isCancel() {
        return mContext == null;
    }
}
