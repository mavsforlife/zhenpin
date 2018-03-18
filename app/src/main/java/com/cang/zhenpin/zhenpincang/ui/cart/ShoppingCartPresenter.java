package com.cang.zhenpin.zhenpincang.ui.cart;

import android.content.Context;
import android.text.TextUtils;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.CartBrand;
import com.cang.zhenpin.zhenpincang.model.CartBrandList;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.cart.util.DecimalUtil;
import com.cang.zhenpin.zhenpincang.ui.confirmorder.ConfirmOrderActivity;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

public class ShoppingCartPresenter implements ShoppingCartContract.Presenter {

    private ShoppingCartContract.View mView;
    private Context mContext;

    public ShoppingCartPresenter(Context context, ShoppingCartContract.View view) {
        mContext = context;
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
    public void loadCartData() {
        NetWork.getsBaseApi()
                .getCartList(PreferencesFactory.getUserPref().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<CartBrandList>>(mView) {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onLoadDataError();
                    }

                    @Override
                    public void onNext(BaseResult<CartBrandList> data) {
                        super.onNext(data);
                        if (data.getData() != null) {
                            mView.onLoadDataSuccess(data.getData().getBrands(), getTotalPrice(data.getData().getBrands()));
                        } else {
                            mView.onLoadDataError();
                        }
                    }

                });
    }

    @Override
    public void onDelCart(List<CartBrand> data) {
        if (data == null || data.size() == 0) {
            mView.onAddEmpty();
            return;
        }

        final List<CartBrand> delData = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (CartBrand cartBrand : data) {
            if (cartBrand.isDelChecked()) {
                sb.append(cartBrand.getMId())
                        .append(",");
                delData.add(cartBrand);
            }
        }
        if (TextUtils.isEmpty(sb)) {
            mView.onDelEmpty();
            return;
        }
        sb.deleteCharAt(sb.length() - 1);

        NetWork.getsBaseApi()
                .delFromShoppingCart(PreferencesFactory.getUserPref().getId(),
                        sb.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult>(mView) {
                    @Override
                    public void onNext(BaseResult baseResult) {
                        super.onNext(baseResult);
                        mView.onDelSuccess(delData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onDelFail();
                    }
                });

    }

    @Override
    public void onAddOrder(List<CartBrand> data, String[] cartInfo) {
        if (data == null || data.size() == 0) {
            mView.onDelEmpty();
            return;
        }

        int count = 0;
        final List<String> pics = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (CartBrand cartBrand : data) {
            if (cartBrand.isChecked()) {
                sb.append(cartBrand.getMId())
                        .append(",");
                pics.add(cartBrand.getMPicPath());
                count++;
            }
        }
        if (TextUtils.isEmpty(sb)) {
            mView.onAddEmpty();
            return;
        }

        sb.deleteCharAt(sb.length() - 1);

        DialogUtil.dismissProgressDialog();
        mContext.startActivity(ConfirmOrderActivity.createIntent(mContext,
                count,
                cartInfo[0],
                (ArrayList<String>) pics,
                sb.toString(),
                null));

    }

    @Override
    public void getShowTip() {
        NetWork.getsBaseApi()
                .getShowTip(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<String>>(mView) {

                    @Override
                    public void onNext(BaseResult<String> result) {
                        super.onNext(result);
                        mView.showTipView(result.getData());
                    }
                });
    }

    private String[] getTotalPrice(List<CartBrand> list) {
        return DecimalUtil.getTotlaCount(list);
    }
}
