package com.cang.zhenpin.zhenpincang.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.cang.zhenpin.zhenpincang.base.App;


/**
 * Created by victor on 2016/8/3.
 * 存放检测更新的时间
 */
public class UpdatePreferences extends BasePreferences {

    private static final String UPDATE_PREF = "updatePref";

    public static final long CHECK_VALID_TIME = 24 * 60 * 60 * 1000L;
//    public static final long CHECK_VALID_TIME = 60 * 60 * 1000L;

    public static final String LAST_CHECK_TIME = "lastCheck";

    @Override
    public SharedPreferences getSharePreferences() {
        return App.getsInstance().getSharedPreferences(UPDATE_PREF, Context.MODE_PRIVATE);
    }

    public void setLastCheck(long timeMillis) {
        putLong(LAST_CHECK_TIME, timeMillis);
    }

    public long getLastCheck() {
        return getLong(LAST_CHECK_TIME, 0L);
    }
}
