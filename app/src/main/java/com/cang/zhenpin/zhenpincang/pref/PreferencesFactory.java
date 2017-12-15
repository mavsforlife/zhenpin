
package com.cang.zhenpin.zhenpincang.pref;


public class PreferencesFactory {

    private static UserPreferences sUserPref;
    private static UpdatePreferences sUpdatePref;


    public static UserPreferences getUserPref() {
        if (null == sUserPref) {
            sUserPref = new UserPreferences();
        }
        return sUserPref;
    }

    public static UpdatePreferences getUpdatePref() {
        if (null == sUpdatePref) {
            sUpdatePref = new UpdatePreferences();
        }
        return sUpdatePref;
    }

}
