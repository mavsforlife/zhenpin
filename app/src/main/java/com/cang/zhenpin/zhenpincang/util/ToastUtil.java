package com.cang.zhenpin.zhenpincang.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by victor on 2017/11/26.
 * Email: wwmdirk@gmail.com
 */

public class ToastUtil {

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, @StringRes int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();

    }

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, @StringRes int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_LONG).show();
    }
}
