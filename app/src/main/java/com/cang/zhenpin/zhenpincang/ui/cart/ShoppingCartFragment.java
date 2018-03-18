package com.cang.zhenpin.zhenpincang.ui.cart;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.UpdateShopCartEvent;
import com.cang.zhenpin.zhenpincang.model.CartBrand;
import com.cang.zhenpin.zhenpincang.ui.cart.util.DecimalUtil;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.victor.loadinglayout.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.cang.zhenpin.zhenpincang.ui.cart.ShoppingCartAdapter.STATUS_BUY;
import static com.cang.zhenpin.zhenpincang.ui.cart.ShoppingCartAdapter.STATUS_DEL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment implements ShoppingCartContract.View,
        SwipeRefreshLayout.OnRefreshListener, ShoppingCartAdapter.updatePriceListener,
        View.OnClickListener {

    private ShoppingCartAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadingLayout mLoadingLayout;
    private TextView mPrice;
    private TextView mSubmit;
    private CheckBox mCheckBox;

    private TextView mDelete;
    private LinearLayout mContainer;

    private ShoppingCartContract.Presenter mPresenter;

    private String[] mCartInfo;

    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance() {
        return new ShoppingCartFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        mContainer = v.findViewById(R.id.container);
        mRecyclerView = v.findViewById(R.id.rv_cart);
        mRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);
        mLoadingLayout = v.findViewById(R.id.loading_layout);
        mDelete = v.findViewById(R.id.delete);
        mPrice = v.findViewById(R.id.price);
        mSubmit = v.findViewById(R.id.charge);
        mCheckBox = v.findViewById(R.id.check_all);
        mCheckBox.setChecked(true);

        mSubmit.setOnClickListener(this);
        mCheckBox.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ShoppingCartAdapter(getActivity(), STATUS_BUY);
        mAdapter.setUpdatePriceListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new ShoppingCartPresenter(getActivity(), this);
        mLoadingLayout.showLoading();
        mPresenter.loadCartData();
        return v;
    }

    public void onEditClick(boolean isEdit) {
        mAdapter.setStatus(isEdit ? STATUS_DEL : STATUS_BUY);
        if (isEdit) {
            mDelete.setVisibility(View.INVISIBLE);
            mPrice.setVisibility(View.INVISIBLE);
            mSubmit.setText(R.string.delete);
        } else {
            mDelete.setVisibility(View.VISIBLE);
            mPrice.setVisibility(View.VISIBLE);
            mSubmit.setText(R.string.charge);
        }
    }

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(getActivity(), tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(getActivity(), resId);
    }

    @Override
    public void onLoadDataError() {
        mRefreshLayout.setRefreshing(false);
        mLoadingLayout.showError();
    }

    @Override
    public void onLoadDataSuccess(List<CartBrand> brands, String[] data) {
        mRefreshLayout.setRefreshing(false);
        if (brands == null || brands.size() == 0) {
            mLoadingLayout.showEmpty();
        } else {
            mAdapter.setData(brands);
            mLoadingLayout.showContent();
            mCartInfo = data;
            mPrice.setText(data[0]);
            if (mAdapter.getStatus() == STATUS_BUY) {
                mCheckBox.setChecked(TextUtils.equals(data[2], "0"));
            } else {
                mCheckBox.setChecked(TextUtils.equals(data[3], "0"));
            }

            if (mContainer.getChildCount() == 1) {
                mPresenter.getShowTip();
            }
        }
    }

    @Override
    public void onDelSuccess(List<CartBrand> data) {
        DialogUtil.dismissProgressDialog();
        mAdapter.delData(data);
        onEditClick(false);
        if (mOnStatusEndListener != null) {
            mOnStatusEndListener.onEditDone();
        }
    }

    @Override
    public void onDelFail() {
        DialogUtil.dismissProgressDialog();
    }

    @Override
    public void onDelEmpty() {
        DialogUtil.dismissProgressDialog();
        showTip(getString(R.string.select_at_least_one));
    }

    @Override
    public void onGotoAdd() {
        DialogUtil.dismissProgressDialog();
    }

    @Override
    public void showTipView(String data) {
        TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.item_show_tip, mContainer, false);
        tv.setText(data);
        mContainer.addView(tv, 0);
    }

    @Override
    public void onAddEmpty() {
        DialogUtil.dismissProgressDialog();
        showTip(getString(R.string.select_at_least_one));
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadCartData();
    }

    @Override
    public void updatePrice(List<CartBrand> list) {
        if (list == null || list.size() == 0) {
            mLoadingLayout.showEmpty();
            return;
        }
        mCartInfo = DecimalUtil.getTotlaCount(list);
        mPrice.setText(mCartInfo[0]);
        if (mAdapter.getStatus() == STATUS_BUY) {
            mCheckBox.setChecked(TextUtils.equals(mCartInfo[2], "0"));
        } else {
            mCheckBox.setChecked(TextUtils.equals(mCartInfo[3], "0"));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.charge) {

            if (mRefreshLayout.isRefreshing()) {
                ToastUtil.showShort(getActivity(), R.string.please_wait);
                return;
            }

            DialogUtil.showProgressDialog(new WeakReference<Context>(getActivity()), R.string.please_wait);
            if (mAdapter.getStatus() == STATUS_BUY) {
                mPresenter.onAddOrder(mAdapter.getData(), mCartInfo);
            } else {
                mPresenter.onDelCart(mAdapter.getData());
            }
        } else if (v.getId() == R.id.check_all) {
            mAdapter.isCheckAll(mCheckBox.isChecked());
        }
    }

    public OnStatusEndListener mOnStatusEndListener;

    public void setOnStatusEndListener(OnStatusEndListener listener) {
        mOnStatusEndListener = listener;
    }
    public interface OnStatusEndListener {
        void onEditDone();
    }

    @Subscribe
    public void handleUpdate(UpdateShopCartEvent event) {
        mLoadingLayout.showLoading();
        mPresenter.loadCartData();
    }
}
