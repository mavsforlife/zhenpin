package com.cang.zhenpin.zhenpincang.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cang.zhenpin.zhenpincang.base.App;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.model.WxUserInfo;

/**
 * Created by victor on 2016/8/16.
 * 用户信息存储
 */
public class UserPreferences extends BasePreferences {

    private static final String USER_INFO_PREFS = "zhen_pin_cang_user_info";

    private static final String DEFAULT_NULL = "";

    private static final int DEFAULT_ZERO = 0;

    public static final int DEFAULT_ERROR = -1;

    private static final String TOKEN = "token";

    private static final String USER_TYPE = "user_type";

    private static final String HEAD_IMG_URL = "head_img_url";

    private static final String USER_ID = "user_id";

    private static final String OPEN_ID = "open_id";

    private static final String _ID = "_id";

    private static final String USER_PHONE = "user_phone";

    private static final String _SEX = "_sex";

    private static final String USER_EMAIL = "user_email";

    private static final String USER_CODE = "user_code";

    private static final String USER_TYPE_NAME = "user_type_name";

    @Override
    public SharedPreferences getSharePreferences() {
        return App.getsInstance().getSharedPreferences(USER_INFO_PREFS, Context.MODE_PRIVATE);
    }

    public void saveUserInfo(UserInfo info) {
        if (null == info) return;
        putString(TOKEN, info.getToken());
        putInt(USER_TYPE, info.getMUserType());
        putString(HEAD_IMG_URL, info.getMHeadImgUrl());
        putInt(_ID, info.getMId());
        putInt(_SEX, info.getMSex());
        putString(USER_PHONE, info.getMMobile());
        putString(USER_ID, info.getMUserId());
        putString(OPEN_ID, info.getMOpenId());
        putInt(USER_TYPE, info.getMUserType());
        putString(USER_EMAIL, info.getMEmail());
        putString(USER_CODE, info.getMUserCode());
        putString(USER_TYPE_NAME, info.getUserTpyeStr());
    }

    public void saveWxInfo(WxUserInfo info) {

    }

    public void saveToken(String token) {
        putString(TOKEN, token);
    }

    public boolean isTokenValid(String token) {
        return TextUtils.equals(token, getString(TOKEN, DEFAULT_NULL));
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getString(USER_ID, DEFAULT_NULL));
    }

    public String getHeadImgUrl() {
        return getString(HEAD_IMG_URL, DEFAULT_NULL);
    }

    public String getUserId() {
        return getString(USER_ID, DEFAULT_NULL);
    }

    public int getUserSex() {
        return getInt(_SEX,DEFAULT_ZERO);
    }
    public String getUserPhone() {
        return getString(USER_PHONE, DEFAULT_NULL);
    }

    public int getId() {
        return getInt(_ID, -1);
    }

    public String getOpenId() {
        return getString(OPEN_ID, DEFAULT_NULL);
    }

    public void saveUserType(int userType) {
        putInt(USER_TYPE, userType);
    }

    public int getUserType() {
        return getInt(USER_TYPE, DEFAULT_ERROR);
    }

    public String getUserTypeStr() {
        return getString(USER_TYPE_NAME, UserInfo.TYPE_NORMAL);
    }
}
