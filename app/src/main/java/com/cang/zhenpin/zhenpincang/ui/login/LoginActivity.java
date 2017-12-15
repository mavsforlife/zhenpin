package com.cang.zhenpin.zhenpincang.ui.login;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.BuildConfig;
import com.cang.zhenpin.zhenpincang.MainActivity;
import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.PostFactory;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.register.RegisterActivity;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    TextView mWeChat;
    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mWeChat = findViewById(R.id.tv_we_chat);
        mWeChat.setOnClickListener(this);

        mPresenter = new LoginPresenter(this, this);
        mPresenter.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_we_chat) {
            AndPermission.with(this)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .requestCode(100)
                    .callback(mPermissionListener)
                    .start();
        }
    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if(requestCode == 100) {
                mPresenter.onGoToWeChat();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 100) {
                showTip(R.string.no_permission_can_not_login);
                finish();
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
    public void weChatLogin() {
        if (BuildConfig.DEBUG) {
            UserInfo userInfo = PostFactory.fake();
            Log.d("loginActivity", userInfo.toString());
            PreferencesFactory.getUserPref().saveUserInfo(userInfo);
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
