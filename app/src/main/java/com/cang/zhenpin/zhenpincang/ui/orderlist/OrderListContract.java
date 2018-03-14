package com.cang.zhenpin.zhenpincang.ui.orderlist;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;

import java.util.List;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public interface OrderListContract {

    interface View extends BaseView {

        void addData(List<Object> datas, boolean isRefresh);

        void showEmpty();

        void onError();

        void hasMoreData(boolean bool);

        void setIsLoading(boolean isLoading);
    }

    interface Presenter extends BasePresenter {

        void onLoadData(boolean isRefresh);

    }
}
