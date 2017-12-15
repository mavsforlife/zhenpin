package com.cang.zhenpin.zhenpincang.util;

import android.os.Build;

/**
 * Created by victor on 2017/11/23.
 * Email: wwmdirk@gmail.com
 */

public class OSVersionUtil {

    public static boolean isVersionOrHigher(int sdkVer) {
        return Build.VERSION.SDK_INT >= sdkVer;
    }

    public static boolean isVersion(int sdkVer) {
        return Build.VERSION.SDK_INT == sdkVer;
    }
}
