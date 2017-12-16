package com.cang.zhenpin.zhenpincang.ui.brand;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.IntentFlag;
import com.cang.zhenpin.zhenpincang.ui.list.GoodsListFragment;

public class BrandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);

        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.super_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTvBar = findViewById(R.id.tv_bar);
        String brandName = getIntent().getStringExtra(IntentFlag.BRAND_NAME);
        mTvBar.setText(TextUtils.isEmpty(brandName) ?
                getString(R.string.app_name) :
                brandName);
    }

    private void initData() {
        String brandId = getIntent().getStringExtra(IntentFlag.BRAND_ID);

        GoodsListFragment fragment = GoodsListFragment.newInstance(brandId, false);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.container, fragment).commit();
    }

    public static Intent createIntent(Context context, String name, String id) {
        Intent intent = new Intent(context, BrandActivity.class);
        intent.putExtra(IntentFlag.BRAND_NAME, name);
        intent.putExtra(IntentFlag.BRAND_ID, id);
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
}
