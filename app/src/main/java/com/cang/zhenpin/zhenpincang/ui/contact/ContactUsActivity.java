package com.cang.zhenpin.zhenpincang.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.ui.register.RegisterActivity;
import com.victor.loadinglayout.LoadingLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactUsActivity extends AppCompatActivity {

    private LoadingLayout mLoadingLayout;
    private TextView mTvContent;
    private TextView mTvBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initView();
        getContact();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTvBar = findViewById(R.id.tv_bar);
        mTvBar.setText(R.string.contact_us);
        mTvContent = findViewById(R.id.tv_content);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContact();
            }
        });
    }

    private void getContact() {
        NetWork.getsBaseApi()
                .contactUs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<String>>(this) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mLoadingLayout.showLoading();
                    }

                    @Override
                    public void onNext(BaseResult<String> stringBaseResult) {
                        super.onNext(stringBaseResult);
                        mTvContent.setText(stringBaseResult.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mLoadingLayout.showError();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mLoadingLayout.showContent();
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
        Intent intent = new Intent(context, ContactUsActivity.class);
        return intent;
    }
}
