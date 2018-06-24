package com.cang.zhenpin.zhenpincang.ui.confirmorder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.App;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.event.SaveAndUseAddressEvent;
import com.cang.zhenpin.zhenpincang.event.UpdateOrderListEvent;
import com.cang.zhenpin.zhenpincang.event.UpdateShopCartEvent;
import com.cang.zhenpin.zhenpincang.event.WxPayEvent;
import com.cang.zhenpin.zhenpincang.model.AddOrder;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.model.AddressList;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.ApiException;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.orderaddress.OrderAddressListActivity;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.victor.loadinglayout.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PAY_TYPE_WX = 1;
    public static final int PAY_TYPE_ALI = 2;
    public static final int PAY_TYPE_OFFLINE = 3;

    public static final int TYPE_CART = 3;
    public static final int TYPE_ORDER = 4;
    private static final String TAG = ConfirmOrderActivity.class.getSimpleName();

    private String mIds;
    private int mCount;
    private String mAddressId;

    private String mOrderId;
    private String mOrderNo;
    private List<Address> mAddresses;

    private LoadingLayout mLoadingLayout;
    private RecyclerView mRv;
    private RelativeLayout mRlAddress;
    private CheckBox mCbAliPay, mCbWxPay;
    private TextView mName, mPhone, mAddress;
    private TextView mTotalCount;
    private TextView mTvTotalFee, mTvRealFee;
    private TextView mNoAddress;
    private TextView mSubmit;
    private TextView mTip;

    private int mPayType = PAY_TYPE_OFFLINE;
    private int mAddType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        EventBus.getDefault().register(this);
        initView();
        initData();

        getAddress();
        showTipView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.enter_order);

        mRv = findViewById(R.id.rv_pic);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mLoadingLayout = findViewById(R.id.loading_layout);
        mRlAddress = findViewById(R.id.rl_address);
        mRlAddress.setOnClickListener(this);

        mNoAddress = findViewById(R.id.tv_no_address);
        mNoAddress.setOnClickListener(this);

        LinearLayout aliPay = findViewById(R.id.ll_ali_pay);
        aliPay.setOnClickListener(this);
        LinearLayout wxPay = findViewById(R.id.ll_wx_pay);
        wxPay.setOnClickListener(this);

        mCbWxPay = findViewById(R.id.cb_wx_pay);
        mCbAliPay = findViewById(R.id.cb_ali_pay);

        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        mTotalCount = findViewById(R.id.count);
        mTvTotalFee = findViewById(R.id.total_fee);
        mTvRealFee = findViewById(R.id.real_fee);
        mSubmit = findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);

        mTip = findViewById(R.id.tip);
    }

    private void initData() {
        mOrderId = getIntent().getStringExtra(IntentFlag.ORDER_ID);
        mAddType = TextUtils.isEmpty(mOrderId) ? TYPE_CART : TYPE_ORDER;
        mIds = getIntent().getStringExtra(IntentFlag.ORDER_CART_IDS);
        mCount = getIntent().getIntExtra(IntentFlag.ORDER_COUNT, 0);
        String totalFee = getIntent().getStringExtra(IntentFlag.ORDER_TOTAL_FEE);
        ArrayList<String> pics = getIntent().getStringArrayListExtra(IntentFlag.ORDER_PIC);

        OrderDetailPicAdapter adapter = new OrderDetailPicAdapter(this, pics);
        mRv.setAdapter(adapter);

        mTvTotalFee.setText(String.format(Locale.US, getString(R.string.total_fee), totalFee));
        mTvRealFee.setText(String.format(Locale.US, getString(R.string.real_fee), totalFee));

        mTotalCount.setText(String.format(Locale.US, getString(R.string.total_count), mCount));
    }

    public static Intent createIntent(Context context, int count, String totalFee, ArrayList<String> pics, String ids, String orderId) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        intent.putExtra(IntentFlag.ORDER_COUNT, count);
        intent.putExtra(IntentFlag.ORDER_TOTAL_FEE, totalFee);
        intent.putExtra(IntentFlag.ORDER_PIC, pics);
        intent.putExtra(IntentFlag.ORDER_CART_IDS, ids);
        intent.putExtra(IntentFlag.ORDER_ID, orderId);
        return intent;
    }

    private void getAddress() {
        Log.d(TAG, "get address");
        NetWork.getsBaseApi()
                .getAddressList(PreferencesFactory.getUserPref().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<AddressList>>(this) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mLoadingLayout.showLoading();
                    }

                    @Override
                    public void onNext(BaseResult<AddressList> result) {
                        super.onNext(result);
                        mLoadingLayout.showContent();
                        if (result.getData() != null) {
                            mAddresses = result.getData().getAddresses();

                            if (mAddresses == null || mAddresses.size() == 0) {
                                mNoAddress.setVisibility(View.VISIBLE);
                                mRlAddress.setVisibility(View.GONE);
                                return;
                            }

                            mNoAddress.setVisibility(View.GONE);
                            mRlAddress.setVisibility(View.VISIBLE);

                            for (Address address : mAddresses) {
                                if (address.getISDefault() != 0) {
                                    address.setOrderAddress(true);
                                    setAddress(address);
                                    mAddressId = address.getID();
                                    return;
                                }
                            }

                            Address address1 = mAddresses.get(0);
                            address1.setOrderAddress(true);
                            setAddress(address1);
                            mAddressId = address1.getID();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mLoadingLayout.showError();
                    }
                });
    }

    private void setAddress(Address address) {
        mName.setText(address.getName());
        mPhone.setText(address.getMobile());
        mAddress.setText(address.getAddress());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address:
                startActivity(OrderAddressListActivity.createIntent(this, (ArrayList<Address>) mAddresses));
                break;
            case R.id.tv_no_address:
                startActivity(OrderAddressListActivity.createIntent(this, (ArrayList<Address>) mAddresses));
                break;
            case R.id.ll_wx_pay:
                mCbWxPay.setChecked(true);
                mCbAliPay.setChecked(false);
                mPayType = PAY_TYPE_WX;
                break;
            case R.id.ll_ali_pay:
                mCbAliPay.setChecked(true);
                mCbWxPay.setChecked(false);
                mPayType = PAY_TYPE_ALI;
                break;
            case R.id.submit:
                //隐藏微信，支付宝支付，只有货到付款
//                if (mOrderId != null) {
//                    payFromInvoice();
//                } else {
//                    addOrder();
//                }
                addOrder();
                break;
        }
    }

    @Subscribe
    public void handleModifyAddress(SaveAndUseAddressEvent event) {
        mAddresses = event.mAddress;
        if (mAddresses == null) {
            mNoAddress.setVisibility(View.VISIBLE);
            mRlAddress.setVisibility(View.GONE);
        } else {
            mNoAddress.setVisibility(View.GONE);
            mRlAddress.setVisibility(View.VISIBLE);
        }
        for (Address address : mAddresses) {
            if (address.isOrderAddress()) {
                setAddress(address);
                mAddressId = address.getID();
                return;
            }
        }
    }

    private void addOrder() {
        NetWork.getsBaseApi()
                .addOrder(PreferencesFactory.getUserPref().getId(),
                        mIds,
                        mCount,
                        mAddressId,
                        mPayType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);

    }

    private void payFromInvoice() {
        NetWork.getsBaseApi()
                .payFromInvoice(mOrderId, mAddressId, mPayType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

//    private void handlePay(AddOrder data) {
//        if (mPayType == PAY_TYPE_WX) {
//            App.mWxApi.sendReq(PayUtils.wxPay(data));
//        } else if (mPayType == PAY_TYPE_ALI) {
//            PayUtils.getAliPayResult(new WeakReference<>(this), data);
//        }
//    }

    //货到付款，直接成功
    private void handlePayNew(BaseResult<AddOrder> data) {
        ToastUtil.showShort(this, data.getMsg());
        finish();
    }

    private BaseActivityObserver<BaseResult<AddOrder>> mObserver = new BaseActivityObserver<BaseResult<AddOrder>>(this) {

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
            DialogUtil.showProgressDialog(new WeakReference<Context>(ConfirmOrderActivity.this));
        }

        @Override
        public void onNext(BaseResult<AddOrder> result) {
            super.onNext(result);
            DialogUtil.dismissProgressDialog();
            if (result.getData() == null) {
                ToastUtil.showShort(ConfirmOrderActivity.this, R.string.add_order_fail);
                return;
            }
            mOrderNo = result.getData().getOrderNo();

            if (mAddType == TYPE_CART) {
                EventBus.getDefault().post(new UpdateShopCartEvent());
            }
//            handlePay(result.getData());
            handlePayNew(result);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            DialogUtil.dismissProgressDialog();
            if (e instanceof ApiException) {
                ApiException exception = (ApiException) e;
                if (exception.getErrorCode() == 2) {

                    if (mAddType == TYPE_CART) {
                        EventBus.getDefault().post(new UpdateShopCartEvent());
                    } else if (mAddType == TYPE_ORDER) {
                        EventBus.getDefault().post(new UpdateOrderListEvent());
                    }
                }
            }

            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEvnet(WxPayEvent event) {

        switch (event.code) {
            case AddOrder.WX_SUCCESS:
                onCheckPaySuccess();
                break;

            case AddOrder.WX_ERROR:
                ToastUtil.showShort(this, R.string.wx_error);
                break;

            case AddOrder.WX_CANCEL:
                ToastUtil.showShort(this, R.string.wx_cancel_pay);
                break;
        }
    }

    public void onCheckPaySuccess() {
        NetWork.getsBaseApi()
                .checkPayStatus(mOrderNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult>(this) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        DialogUtil.showProgressDialog(new WeakReference<Context>(ConfirmOrderActivity.this),
                                R.string.please_wait_for_check_order_status);
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        super.onNext(baseResult);
                        DialogUtil.dismissProgressDialog();
                        ToastUtil.showShort(ConfirmOrderActivity.this, baseResult.getMsg());
                        if (mAddType == TYPE_ORDER) {
                            EventBus.getDefault().post(new UpdateOrderListEvent());
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mAddType == TYPE_ORDER) {
                            EventBus.getDefault().post(new UpdateOrderListEvent());
                        }
                        DialogUtil.dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showTipView() {
        NetWork.getsBaseApi()
                .getShowTip(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<String>>(this) {

                    @Override
                    public void onNext(BaseResult<String> result) {
                        super.onNext(result);
                        mTip.setText(result.getData());
                    }
                });
    }
}
