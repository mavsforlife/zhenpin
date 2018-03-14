package com.cang.zhenpin.zhenpincang.ui.addresslist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.event.UpdateAddressEvent;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.model.AddressList;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.newaddress.NewAddressActivity;
import com.victor.loadinglayout.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddressListActivity extends AppCompatActivity implements View.OnClickListener,
AddressListAdapter.OnAddressChangeListener{

    private TextView mAdd;
    private LoadingLayout mLoadingLayout;
    private RecyclerView mRecyclerView;
    private AddressListAdapter mAdapter;

    private ArrayList<Address> mAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.address_management);

        mAdd = findViewById(R.id.tv_edit);
        mAdd.setText(R.string.add_address);
        mAdd.setVisibility(View.VISIBLE);
        mAdd.setOnClickListener(this);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mRecyclerView = findViewById(R.id.rv_address);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddressListAdapter(this);
        mAdapter.setOnAddressChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);


        mAddresses = getIntent().getParcelableArrayListExtra(IntentFlag.ADDRESS_LIST);

        if (mAddresses == null) {
            getAddress();
        } else {
            mAdapter.setList(mAddresses);
        }
    }

    private void getAddress() {

        mLoadingLayout.showLoading();
        NetWork.getsBaseApi()
                .getAddressList(PreferencesFactory.getUserPref().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<AddressList>>(this) {
                    @Override
                    public void onNext(BaseResult<AddressList> result) {
                        super.onNext(result);
                        if (result == null || result.getData() == null) {
                            mLoadingLayout.showError();
                            return;
                        }

                        updateData(result.getData().getAddresses());

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mLoadingLayout.showError();
                    }
                });
    }

    private void updateData(List<Address> data) {
        if (data == null || data.size() == 0) {
            mLoadingLayout.showEmpty();
            return;
        }
        mAddresses = (ArrayList<Address>) data;
        mAdapter.setList(data);
        mLoadingLayout.showContent();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, AddressListActivity.class);
    }

    public static Intent createIntent(Context context, ArrayList<Address> data) {
        Intent intent = new Intent(context, AddressListActivity.class);
        intent.putParcelableArrayListExtra(IntentFlag.ADDRESS_LIST, data);
        return intent;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_edit) {
            Intent intent = NewAddressActivity.createIntent(this, NewAddressActivity.TYPE_NEW);
            startActivity(intent);
        }
    }

    @Subscribe
    public void onHandleUpdateEvent(UpdateAddressEvent event) {
        if (event.mType == NewAddressActivity.TYPE_NEW) {
            getAddress();
        } else {
            if (event.mPosition >= 0 && event.mPosition < mAdapter.getItemCount()) {
                mAdapter.updateData(event.mPosition, event.mAddress);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onListEmpty() {
        mLoadingLayout.showEmpty();
    }
}
