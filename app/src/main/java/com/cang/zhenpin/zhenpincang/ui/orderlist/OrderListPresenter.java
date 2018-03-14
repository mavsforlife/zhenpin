package com.cang.zhenpin.zhenpincang.ui.orderlist;

import android.content.Context;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.OrderModel;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class OrderListPresenter implements OrderListContract.Presenter {

    private OrderListContract.View mView;
    private Context mContext;

    private int mCurrPage;
    private int mPageAll;

    public OrderListPresenter(Context context, OrderListContract.View view) {
        mContext = context;
        mView = view;
        mCurrPage = 1;
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
    public void onLoadData(boolean isRefresh) {
        if (isRefresh) {
            mCurrPage = 1;
            mPageAll = 0;
        }
        getOrderList(isRefresh);
    }

    private void getOrderList(final boolean isRefresh) {

        NetWork.getsBaseApi()
                .getOrderList(PreferencesFactory.getUserPref().getId(),
                        mCurrPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<OrderModel>>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.setIsLoading(true);
                    }

                    @Override
                    public void onNext(BaseResult<OrderModel> result) {
                        super.onNext(result);

                        if (result.getData() == null) {
                            if (isRefresh) {
                                mView.showEmpty();
                            } else {
                                mView.onError();
                            }
                            return;
                        }

                        OrderModel model = result.getData();
                        mCurrPage = model.getPageNow();
                        mPageAll = model.getPageAll();
                        mView.hasMoreData(mPageAll > mCurrPage);
                        List<Object> list = OrderListUtils.transformOrder(model);
                        if (list.size() > 0) {
                            mView.addData(list, isRefresh);
                        } else {
                            if (isRefresh) {
                                mView.showEmpty();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onError();
                        mView.setIsLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mCurrPage++;
                        mView.setIsLoading(false);
                    }
                });
    }
}
