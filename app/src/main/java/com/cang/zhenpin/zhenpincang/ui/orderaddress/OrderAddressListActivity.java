package com.cang.zhenpin.zhenpincang.ui.orderaddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.event.SaveAndUseAddressEvent;
import com.cang.zhenpin.zhenpincang.event.UpdateAddressEvent;
import com.cang.zhenpin.zhenpincang.listener.OnRvItemClickListener;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.ui.addresslist.AddressListActivity;
import com.cang.zhenpin.zhenpincang.ui.newaddress.NewAddressActivity;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;
import com.cang.zhenpin.zhenpincang.widget.ItemDividerDecoration;
import com.cang.zhenpin.zhenpincang.widget.TopDividerItemDecoration;
import com.victor.loadinglayout.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class OrderAddressListActivity extends AppCompatActivity implements View.OnClickListener,
        OnRvItemClickListener{

    private TextView mAdd;
    private LoadingLayout mLoadingLayout;
    private RecyclerView mRecyclerView;
    private OrderAddressListAdapter mAdapter;

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
        mTvBar.setText(R.string.express_address);

        mAdd = findViewById(R.id.tv_edit);
        mAdd.setText(R.string.add_address);
        mAdd.setVisibility(View.VISIBLE);
        mAdd.setOnClickListener(this);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mRecyclerView = findViewById(R.id.rv_address);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderAddressListAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);


        mAddresses = getIntent().getParcelableArrayListExtra(IntentFlag.ADDRESS_LIST);
        if (mAddresses != null && mAddresses.size() != 0) {
            mAdapter.setList(mAddresses);
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }

    }

    public static Intent createIntent(Context context, ArrayList<Address> data) {
        Intent intent = new Intent(context, OrderAddressListActivity.class);
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
            Intent intent = NewAddressActivity.createIntent(this, NewAddressActivity.TYPE_NEW_QUIT);
            startActivity(intent);
        }
    }

    @Subscribe
    public void onHandleUpdateEvent(UpdateAddressEvent event) {
        if (event.mType == NewAddressActivity.TYPE_NEW_QUIT) {
            event.mAddress.setOrderAddress(true);
            if (mAddresses == null || mAddresses.size() == 0) {
                mAddresses = new ArrayList<>();
                mAddresses.add(event.mAddress);
            } else {
                for (Address address : mAddresses) {
                    address.setOrderAddress(false);
                }
                mAddresses.add(event.mAddress);
            }
        } else if (event.mType == NewAddressActivity.TYPE_MODIFY_QUIT){
            for (Address address : mAddresses) {
                address.setOrderAddress(false);
            }
            event.mAddress.setOrderAddress(true);
            mAddresses.set(event.mPosition, event.mAddress);
        }
        EventBus.getDefault().post(new SaveAndUseAddressEvent(mAddresses));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(int position) {
        EventBus.getDefault().post(new SaveAndUseAddressEvent(mAddresses));
        finish();
    }
}
