package com.cang.zhenpin.zhenpincang.ui.orderdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.victor.loadinglayout.LoadingLayout;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements OrderDetailContract.View{

    LoadingLayout mLoadingLayout;
    RecyclerView mRv;

    OrderDetailAdapter mAdapter;

    OrderDetailPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.order_detail);

        mLoadingLayout = findViewById(R.id.loading_layout);
        mRv = findViewById(R.id.rv_order_detail);
        mRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderDetailAdapter(this);
        mRv.setAdapter(mAdapter);
    }

    private void initData() {
        String orderNo = getIntent().getStringExtra(IntentFlag.ORDER_NO);
        if (TextUtils.isEmpty(orderNo)) {
            mLoadingLayout.showEmpty();
        } else {
            mLoadingLayout.showLoading();
            mPresenter = new OrderDetailPresenter(orderNo, this);
            mPresenter.getOrderDetail();
            mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.getOrderDetail();
                }
            });
        }

    }
    public static Intent createIntent(Context context, String orderNo) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(IntentFlag.ORDER_NO, orderNo);
        return intent;
    }

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(this, tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(this, resId);
    }

    @Override
    public void showContent(List<Object> list) {
        mAdapter.setDatas(list);
        mLoadingLayout.showContent();
    }

    @Override
    public void showError() {
        mLoadingLayout.showError();
    }

    @Override
    public void showEmpty() {
        mLoadingLayout.showEmpty();
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
}
