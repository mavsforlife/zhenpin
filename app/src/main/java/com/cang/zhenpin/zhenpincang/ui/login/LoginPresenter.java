package com.cang.zhenpin.zhenpincang.ui.login;

import android.content.Context;

import com.cang.zhenpin.zhenpincang.BuildConfig;
import com.cang.zhenpin.zhenpincang.base.App;
import com.cang.zhenpin.zhenpincang.event.LoginEvent;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by victor on 2017/11/23.
 * Email: wwmdirk@gmail.com
 */

public class LoginPresenter implements LoginContract.Presenter {

    private Context mContext;
    private LoginContract.View mView;

    public LoginPresenter(Context context, LoginContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void start() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onGoToWeChat() {
        if (BuildConfig.DEBUG) {
            mView.weChatLogin();
        } else {
            sendRequest();
        }
    }

    private void sendRequest() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "zhen_pin_cang_state";
        App.mWxApi.sendReq(req);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        mView.weChatLogin();
    }
}
