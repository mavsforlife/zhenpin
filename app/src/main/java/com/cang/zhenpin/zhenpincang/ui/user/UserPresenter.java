package com.cang.zhenpin.zhenpincang.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.network.download.UpdateHelper;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UserPreferences;
import com.cang.zhenpin.zhenpincang.ui.contact.ContactUsActivity;
import com.cang.zhenpin.zhenpincang.ui.login.LoginActivity;
import com.cang.zhenpin.zhenpincang.ui.register.RegisterActivity;
import com.cang.zhenpin.zhenpincang.util.FileUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2017/12/12.
 * Email: wwmdirk@gmail.com
 */

public class UserPresenter implements UserContract.Presenter {

    private UserContract.View mView;
    private Context mContext;
    private UserPreferences mPreferences;

    public UserPresenter(UserContract.View view, Context context) {
        mView = view;
        mContext = context;
        mPreferences = PreferencesFactory.getUserPref();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void apply() {
        int userType = mPreferences.getUserType();
        if (userType == UserInfo.TYPE_APPLY_ING_INT) {
            ToastUtil.showShort(mContext, "您已经申请过代理，我们将尽快为您处理！");
            ensureUserInfo();
        } else if (userType == UserInfo.TYPE_AGENT_INT) {
            ToastUtil.showShort(mContext, "您已经是代理用户，无需再次申请！");
        } else {
            mContext.startActivity(RegisterActivity.createIntent(mContext));
        }
    }

    @Override
    public void contactUs() {
        mContext.startActivity(ContactUsActivity.createIntent(mContext));
    }

    @Override
    public void checkUpdate() {
        UpdateHelper helper = new UpdateHelper(mContext);
        helper.getUpdateInfo();
    }

    @Override
    public void clearCache() {
        FileUtil.deleteFolderFile(mContext.getExternalCacheDir().getAbsolutePath(), true);
        mView.showTip(R.string.clear_cache_success);
        mView.setFileSize();
    }

    @Override
    public void logOut() {
        new AlertDialog.Builder(mContext)
                .setMessage(R.string.are_you_sure_to_log_out)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesFactory.getUserPref().clear();
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        ((Activity) mContext).finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void ensureUserInfo() {
        final int userType = mPreferences.getUserType();

        String openId = mPreferences.getOpenId();
        String nickName = mPreferences.getUserId();
        String headImgUrl = mPreferences.getHeadImgUrl();
        int sex = mPreferences.getUserSex();
        NetWork.getsBaseApi()
                .wxLogin(openId, nickName, headImgUrl, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<UserInfo>>(mView) {
                    @Override
                    public void onNext(BaseResult<UserInfo> userInfoBaseResult) {
                        super.onNext(userInfoBaseResult);
                        UserInfo info = userInfoBaseResult.getData();
                        if (null != info) {
                            mPreferences.saveUserInfo(userInfoBaseResult.getData());
                            if (userType == info.getMUserType()) return;
                            mView.setUserType();
                            if (info.getMUserType() == UserInfo.TYPE_AGENT_INT) {
                                mView.showTip(R.string.congratulate_to_apply_success);
                            } else if (info.getMUserType() == UserInfo.TYPE_REJECT_INT) {
                                mView.showTip(R.string.apply_reject_and_contact_us);
                            }
                        }

                    }
                });
    }
}
