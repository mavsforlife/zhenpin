package com.cang.zhenpin.zhenpincang.ui.newaddress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.event.UpdateAddressEvent;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.BaseApi;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.RegexUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewAddressActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TYPE_NEW = 1;
    public static final int TYPE_MODIFY = 2;
    public static final int TYPE_MODIFY_QUIT = 3;
    public static final int TYPE_NEW_QUIT = 4;

    private int mTypeExtra;
    private int mPositionExtra;
    private Address mDataExtra;
    private TextView mAdd;
    private EditText mName, mPhone, mAddress;
    private SwitchCompat mDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        initView();
        initData();
    }
    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.add_address);

        mAdd = findViewById(R.id.tv_edit);
        mAdd.setText(R.string.add);
        mAdd.setVisibility(View.VISIBLE);
        mAdd.setOnClickListener(this);

        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mAddress = findViewById(R.id.address);
        mDefault = findViewById(R.id.setting);

    }

    private void initData() {
        mTypeExtra = getIntent().getIntExtra(IntentFlag.NEW_ADDRESS, 0);
        mDataExtra = getIntent().getParcelableExtra(IntentFlag.MODIFY_ADDRESS);
        mPositionExtra = getIntent().getIntExtra(IntentFlag.MODIFY_POSITION, -1);
        if (mTypeExtra == TYPE_NEW || mTypeExtra == TYPE_NEW_QUIT) {
            mAdd.setText(mTypeExtra == TYPE_NEW ? R.string.add : R.string.save_and_use);
            mName.setText(PreferencesFactory.getUserPref().getUserName());
            mPhone.setText(PreferencesFactory.getUserPref().getUserPhone());

        } else {
            if (mTypeExtra == TYPE_MODIFY) {
                mAdd.setText(R.string.save);
            } else if (mTypeExtra == TYPE_MODIFY_QUIT) {
                mAdd.setText(R.string.save_and_use);
            } else {
                mAdd.setVisibility(View.GONE);
            }
            mName.setText(mDataExtra.getName());
            mPhone.setText(mDataExtra.getMobile());
            mAddress.setText(mDataExtra.getAddress());
        }
        mName.setSelection(mName.getText().length());
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

    public static Intent createIntent(Context context, Address address, int position, int type) {
        Intent intent = new Intent(context, NewAddressActivity.class);
        intent.putExtra(IntentFlag.MODIFY_ADDRESS, address);
        intent.putExtra(IntentFlag.MODIFY_POSITION, position);
        intent.putExtra(IntentFlag.NEW_ADDRESS, type);
        return intent;
    }

    public static Intent createIntent(Context context, int type) {
        Intent intent = new Intent(context, NewAddressActivity.class);
        intent.putExtra(IntentFlag.NEW_ADDRESS, type);
        return intent;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_edit) {
            if (TextUtils.isEmpty(mName.getText())) {
                ToastUtil.showShort(this, "姓名不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mPhone.getText())) {
                ToastUtil.showShort(this, "手机号码不能为空！");
                return;
            }

            if (!RegexUtil.regexPhone(mPhone.getText().toString())) {
                ToastUtil.showShort(this, "请填写正确的手机号！");
                return;
            }

            if (TextUtils.isEmpty(mAddress.getText().toString())) {
                ToastUtil.showShort(this, "地址不能为空！");
                return;
            }

            String name = mName.getText().toString();
            String phone = mPhone.getText().toString();
            String address = mAddress.getText().toString();
            String isDefault = mDefault.isChecked() ? "1" : "0";
            Map<String, String> queryMap = new HashMap<>();
            queryMap.put(BaseApi.UID, String.valueOf(PreferencesFactory.getUserPref().getId()));
            queryMap.put(BaseApi.NAME, name);
            queryMap.put(BaseApi.MOBILE_ADDRESS, phone);
            queryMap.put(BaseApi.ADDRESS, address);
            queryMap.put(BaseApi.IS_DEFAULT, isDefault);

            if (mTypeExtra != TYPE_NEW && mDataExtra != null) {
                queryMap.put(BaseApi.ID, mDataExtra.getID());
            }
            addOrModify(queryMap);
        }
    }

    private ProgressDialog mProgressDialog;
    private void addOrModify(Map<String, String> quereMap) {
        NetWork.getsBaseApi()
                .modifyOrAddAddress(quereMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<Address>>(this) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mProgressDialog = ProgressDialog.show(NewAddressActivity.this, "", getString(R.string.please_wait),
                                true, false);
//                        DialogUtil.showProgressDialog(NewAddressActivity.this,
//                                "请稍候");
                    }

                    @Override
                    public void onNext(BaseResult<Address> result) {
                        super.onNext(result);
                        mProgressDialog.dismiss();
                        EventBus.getDefault().post(new UpdateAddressEvent(mTypeExtra, mPositionExtra, result.getData()));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mProgressDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        DialogUtil.dismissProgressDialog();
        super.onDestroy();
    }
}
