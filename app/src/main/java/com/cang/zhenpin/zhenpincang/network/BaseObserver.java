package com.cang.zhenpin.zhenpincang.network;

import android.support.v4.content.ContextCompat;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.BaseView;

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

public class BaseObserver<T> implements Observer<T> {

    private BaseView mView;

    public BaseObserver(BaseView view) {
        mView = view;
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
            mView.showTip(((ApiException) e).getMsg());
        } else if (e instanceof UnknownHostException) {
            mView.showTip(R.string.please_open_network);
        } else if (e instanceof SocketTimeoutException) {
            mView.showTip(R.string.request_time_out);
        } else if (e instanceof ConnectException) {
            mView.showTip(R.string.connect_fail);
        } else if (e instanceof HttpException) {
            mView.showTip(R.string.request_time_out);
        }
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        if (isCancel()) return;
    }

    protected boolean isCancel() {
        return mView == null;
    }
}
