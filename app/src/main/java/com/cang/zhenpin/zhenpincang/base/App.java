package com.cang.zhenpin.zhenpincang.base;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * Created by victor on 2017/11/29.
 * Email: wwmdirk@gmail.com
 */

public class App extends Application {
    private static volatile App sInstance = null;
    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        registerToWX();

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static App getsInstance() {
        return sInstance;
    }

    private void registerToWX() {
        mWxApi = WXAPIFactory.createWXAPI(this, BaseConstants.WX_APP_ID, true);
        mWxApi.registerApp(BaseConstants.WX_APP_ID);
    }
}
