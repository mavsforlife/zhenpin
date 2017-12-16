package com.cang.zhenpin.zhenpincang.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.cang.zhenpin.zhenpincang.BuildConfig;

import java.lang.reflect.Method;

/**
 * Created by victor on 2017/11/22.
 * Email: wwmdirk@gmail.com
 */

public class DeviceUtil {

    /**
     * @param context the context
     * @return screen width in pixel
     */
    public static int getScreenWidthPixel(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * @param context the context
     * @return screen height in pixel
     */
    public static int getScreenHeightPixel(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (OSVersionUtil.isVersionOrHigher(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            display.getRealMetrics(outMetrics);
        } else {
            display.getMetrics(outMetrics);
        }

        return outMetrics;
    }

    /**
     * @param context the context
     * @return the status bar height in pixel
     */
    public static int getStatusBarHeightPixel(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * @return the dpi
     */
    public static int getDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static String getScale(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;
        if (densityDpi <= DisplayMetrics.DENSITY_HIGH) {
            return "hdpi";
        } else if (densityDpi <= DisplayMetrics.DENSITY_XHIGH) {
            return "xhdpi";
        } else {
            return "xxhdpi";
        }
    }
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getNaviBarHeightPixel(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean hasNaviBar (Context context) {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }

        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag (see static block)
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    public static int getNavigationBarHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics defaultDisplayMetrics = new DisplayMetrics(); // 获取可用的高度
        windowManager.getDefaultDisplay().getMetrics(defaultDisplayMetrics);
        int usableHeight = defaultDisplayMetrics.heightPixels;
        DisplayMetrics realDisplayMetrics = new DisplayMetrics(); // 获取实际的高度
        if (OSVersionUtil.isVersionOrHigher(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
            windowManager.getDefaultDisplay().getRealMetrics(realDisplayMetrics);
        } else {
            windowManager.getDefaultDisplay().getMetrics(realDisplayMetrics);
        }
        int realHeight = realDisplayMetrics.heightPixels;
        return realHeight > usableHeight ? realHeight - usableHeight : 0;
    }

    /**
     * 是否有网
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回版本号
     * 对应build.gradle中的versionCode
     * @return 返回版本号
     */
    public static String getVersionCode() {
        return String.valueOf(BuildConfig.VERSION_CODE);
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取设备的唯一标识，deviceId
     *
     * @param context
     * @return
     */
//    public static String getDeviceId(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String deviceId = tm.getDeviceId();
//        if (deviceId == null) {
//            return -;
//        } else {
//            return deviceId;
//        }
//    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public static int getBuildLevel() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本（4.4、5.0、5.1 ...）
     *
     * @return
     */
    public static String getBuildVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
