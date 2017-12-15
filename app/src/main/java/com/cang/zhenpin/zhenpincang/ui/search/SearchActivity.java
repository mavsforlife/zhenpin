package com.cang.zhenpin.zhenpincang.ui.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cang.zhenpin.zhenpincang.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }
}
