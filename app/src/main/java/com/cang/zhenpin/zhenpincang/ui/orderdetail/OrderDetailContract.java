package com.cang.zhenpin.zhenpincang.ui.orderdetail;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;

import java.util.List;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

public interface OrderDetailContract {

    interface View extends BaseView {

        void showContent(List<Object> list);

        void showError();

        void showEmpty();
    }

    interface Presenter extends BasePresenter {

        void getOrderDetail();
    }
}
