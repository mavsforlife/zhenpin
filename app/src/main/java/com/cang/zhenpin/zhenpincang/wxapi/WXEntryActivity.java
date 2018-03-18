package com.cang.zhenpin.zhenpincang.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cang.zhenpin.zhenpincang.MainActivity;
import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.App;
import com.cang.zhenpin.zhenpincang.event.LoginEvent;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.model.WxAccessResult;
import com.cang.zhenpin.zhenpincang.model.WxUserInfo;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.cang.zhenpin.zhenpincang.base.BaseConstants.AUTHORIZATION_CODE;
import static com.cang.zhenpin.zhenpincang.base.BaseConstants.WX_APP_ID;
import static com.cang.zhenpin.zhenpincang.base.BaseConstants.WX_APP_SECRET;
import static com.cang.zhenpin.zhenpincang.base.BaseConstants.ZH_CN;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity__log";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, "baseResp code is " + baseResp.errCode);
        Log.d(TAG, "baseResp msg is " + baseResp.errStr);
        Log.d(TAG, baseResp.toString());
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if (resp.getType() == RETURN_MSG_TYPE_LOGIN) {
                        String code = resp.code;
                        accessToken(code);
                    }
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtil.showShort(this, resp.errStr);
                    finish();
                    overridePendingTransition(0, 0);
                    break;
            }
        }
    }

    private void accessToken(String code) {
        NetWork.getWxApi()
                .getAccessToken(WX_APP_ID, WX_APP_SECRET, code, AUTHORIZATION_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<WxAccessResult>(this) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        DialogUtil.showProgressDialog(new WeakReference<Context>(WXEntryActivity.this));
                    }

                    @Override
                    public void onNext(WxAccessResult wxAccessResult) {
                        super.onNext(wxAccessResult);
                        Log.d(TAG, wxAccessResult.toString());
                        if (wxAccessResult.getMErrCode() == 0) {
                        getUserInfo(wxAccessResult.getMAccessToken(),
                                wxAccessResult.getMOpenId());
                        } else {
                            finishSelf();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.d(TAG, e.toString());
                        finishSelf();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getUserInfo(String accessToken, String openId) {
        NetWork.getWxApi()
                .getUserInfo(accessToken, openId, ZH_CN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<WxUserInfo>(this) {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WxUserInfo wxUserInfo) {
                        super.onNext(wxUserInfo);
                        Log.d(TAG, wxUserInfo.toString());
                        if (wxUserInfo.getMErrCode() == 0) {
                            login(wxUserInfo.getMOpenId(), wxUserInfo.getMNickName(),
                                    wxUserInfo.getMHeadImgUrl(), wxUserInfo.getMSex());
                        } else {
                            finishSelf();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        finishSelf();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void login(String openId, String nickName, String headImgUrl, int sex) {
        NetWork.getsBaseApi()
                .wxLogin(openId, nickName, headImgUrl, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<UserInfo>>(this) {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<UserInfo> userInfoBaseResult) {
                        super.onNext(userInfoBaseResult);
                        DialogUtil.dismissProgressDialog();
                        PreferencesFactory.getUserPref().saveUserInfo(userInfoBaseResult.getData());
                        startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        EventBus.getDefault().post(new LoginEvent());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        finishSelf();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void finishSelf() {
        DialogUtil.dismissProgressDialog();
        ToastUtil.showShort(this, R.string.login_fail);
        finish();
        overridePendingTransition(0, 0);
    }
}
