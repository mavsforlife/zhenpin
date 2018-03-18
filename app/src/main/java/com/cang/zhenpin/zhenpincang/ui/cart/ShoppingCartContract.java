package com.cang.zhenpin.zhenpincang.ui.cart;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;
import com.cang.zhenpin.zhenpincang.model.CartBrand;

import java.util.List;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

public interface ShoppingCartContract {

    interface View extends BaseView {

        void onLoadDataError();

        void onLoadDataSuccess(List<CartBrand> brands, String[] totalPrice);

        void onDelSuccess(List<CartBrand> data);

        void onDelFail();

        void onDelEmpty();

        void onAddEmpty();

        void onGotoAdd();

        void showTipView(String data);
    }

    interface Presenter extends BasePresenter {

        void loadCartData();

        void onDelCart(List<CartBrand> data);

        void onAddOrder(List<CartBrand> data, String[] cartInfo);

        void getShowTip();
    }
}
