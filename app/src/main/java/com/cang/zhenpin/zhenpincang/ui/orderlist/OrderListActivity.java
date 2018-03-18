package com.cang.zhenpin.zhenpincang.ui.orderlist;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.UpdateOrderListEvent;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.victor.loadinglayout.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class OrderListActivity extends AppCompatActivity implements OrderListContract.View, OrderListAdapter.OrderListListener, SwipeRefreshLayout.OnRefreshListener {

    private LoadingLayout mLoadingLayout;
    private SwipeRefreshLayout mSrl;
    private RecyclerView mRv;

    private LinearLayoutManager mLayoutManager;
    private OrderListAdapter mAdapter;

    private OrderListPresenter mPresenter;

    private boolean mHasMoreData = true; //是否加载更多数据
    private boolean mIsLoading;         //是否在加载中
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.order_list);

        mLoadingLayout = findViewById(R.id.loading_layout);
        mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSrl.isRefreshing()) return;
                mSrl.setEnabled(false);
                mLoadingLayout.showLoading();
                mPresenter.onLoadData(true);
            }
        });
        mSrl = findViewById(R.id.swipe_refresh_layout);
        mSrl.setColorSchemeResources(R.color.colorPrimary);
        mSrl.setOnRefreshListener(this);
        mSrl.setRefreshing(true);
        mRv = findViewById(R.id.rv_order);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(mLayoutManager);
        mRv.addOnScrollListener(mOnScrollListener);
        mAdapter = new OrderListAdapter(this);
        mAdapter.setOrderListListener(this);
        mRv.setAdapter(mAdapter);
        mLoadingLayout.showContent();

        mPresenter = new OrderListPresenter(this, this);
        mPresenter.onLoadData(true);
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

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int totalCount = mLayoutManager.getItemCount();
            if (lastVisibleItem + 1 == totalCount && mHasMoreData && !mIsLoading) {
                mPresenter.onLoadData(false);
            }
        }
    };

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(this, tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(this, resId);
    }

    @Override
    public void onRetry() {
        mPresenter.onLoadData(false);
    }

    @Override
    public void onEmpty() {
        mLoadingLayout.showEmpty();
    }

    @Override
    public void onRefresh() {
        mPresenter.onLoadData(true);
    }

    @Override
    public void addData(List<Object> datas, boolean isRefresh) {
        mSrl.setEnabled(true);
        if (null == datas || datas.size() == 0) {
            return;
        }
        mLoadingLayout.showContent();
        if (isRefresh) {
            mAdapter.setData(datas);
        } else {
            mAdapter.addData(datas);
        }
    }

    @Override
    public void showEmpty() {
        mSrl.setEnabled(true);
        mAdapter.clear();
        mLoadingLayout.showEmpty();
    }

    @Subscribe
    public void handleUpdate(UpdateOrderListEvent event) {
        mSrl.setRefreshing(true);
        mPresenter.onLoadData(true);
    }

    @Override
    public void onError() {
        if (mAdapter.isEmpty()) {
            mLoadingLayout.showError();
            mSrl.setEnabled(true);
        } else {
            mAdapter.showFooterError();
        }
    }

    @Override
    public void hasMoreData(boolean bool) {
        mHasMoreData = bool;
        mAdapter.setHasMoreData(mHasMoreData);
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
        if (!isLoading && mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context , OrderListActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
