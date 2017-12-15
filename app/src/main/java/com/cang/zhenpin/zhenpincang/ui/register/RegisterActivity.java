package com.cang.zhenpin.zhenpincang.ui.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.cang.zhenpin.zhenpincang.MainActivity;
import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.RefreshLoginEvent;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UserPreferences;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.RegexUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mSubmit;
    private UserPreferences mUserPreferences;
    private String mName, mMobile, mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        mUserPreferences = PreferencesFactory.getUserPref();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        EditText etName = findViewById(R.id.et_name);
        etName.addTextChangedListener(mNameWatcher);
        EditText etMobile = findViewById(R.id.et_mobile);
        etMobile.addTextChangedListener(mMobileWatcher);
        EditText etMail = findViewById(R.id.et_mail);
        etMail.addTextChangedListener(mMailWatcher);
        mSubmit = findViewById(R.id.ll_submit);
        mSubmit.setEnabled(false);
        mSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_submit) {
            if (!RegexUtil.regexPhone(mMobile)) {
                ToastUtil.showShort(this, R.string.please_enter_correct_mobile);
                return;
            }

            if (!RegexUtil.regexEmail(mEmail)) {
                ToastUtil.showShort(this, R.string.please_enter_correct_email);
                return;
            }
            showEnsureDialog();
        }
    }

    TextWatcher mNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mName = s.toString();
            checkEnabled();
        }
    };

    TextWatcher mMobileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mMobile = s.toString();
            checkEnabled();
        }
    };

    TextWatcher mMailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mEmail = s.toString();
            checkEnabled();
        }
    };

    private void checkEnabled() {
        boolean isEnabled = !TextUtils.isEmpty(mName)
                && !TextUtils.isEmpty(mMobile)
                && !TextUtils.isEmpty(mEmail);
        mSubmit.setEnabled(isEnabled);
    }

    private void showEnsureDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.please_ensure_your_message)
                .setMessage(String.format(Locale.getDefault(),
                        getString(R.string.ensure_user_info), mName, mMobile, mEmail))
                .setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestVip();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_ensure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void requestVip() {
        NetWork.getsBaseApi()
                .requestVip(mUserPreferences.getOpenId(), mName, mEmail, mMobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<UserInfo>>(this) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        DialogUtil.showProgressDialog(RegisterActivity.this);
                    }

                    @Override
                    public void onNext(BaseResult<UserInfo> userInfoBaseResult) {
                        super.onNext(userInfoBaseResult);
                        ToastUtil.showShort(RegisterActivity.this,
                                TextUtils.isEmpty(userInfoBaseResult.getMsg()) ?
                                        getString(R.string.please_wait_for_check) :
                                        userInfoBaseResult.getMsg());
                        UserInfo info = userInfoBaseResult.getData();
                        if (info != null) {
                            mUserPreferences.saveUserInfo(info);
                            EventBus.getDefault().post(new RefreshLoginEvent());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtil.dismissProgressDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        DialogUtil.dismissProgressDialog();
                        finish();
                    }
                });
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

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }
}
