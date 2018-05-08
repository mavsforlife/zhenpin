package com.cang.zhenpin.zhenpincang.ui.orderdetail;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.OrderDetail;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.ui.orderdetail.model.OrderDetailUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

public class OrderDetailPresenter implements OrderDetailContract.Presenter {

    private String mOrderNo;
    private OrderDetailContract.View mView;

    public OrderDetailPresenter(String orderNo, OrderDetailContract.View view) {
        mOrderNo = orderNo;
        mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void getOrderDetail() {
        NetWork.getsBaseApi()
                .getOrderDetail(mOrderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<OrderDetail>>(mView) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onNext(BaseResult<OrderDetail> orderDetailBaseResult) {
                        super.onNext(orderDetailBaseResult);
                        if (orderDetailBaseResult.getData() == null || orderDetailBaseResult.getData().mDetail == null) {
                            mView.showEmpty();
                            return;
                        }

                        List<Object> list = OrderDetailUtil.transformOrderDetail(orderDetailBaseResult.getData().mDetail);
                        if (list == null) {
                            mView.showEmpty();
                        } else {
                            mView.showContent(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showError();
                    }
                });
    }
}
