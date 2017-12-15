package com.cang.zhenpin.zhenpincang.network;

import android.content.Context;

import com.cang.zhenpin.zhenpincang.R;
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

public class BaseActivitySlienceObserver<T> implements Observer<T> {

    private Context mContext;

    public BaseActivitySlienceObserver(Context context) {
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
