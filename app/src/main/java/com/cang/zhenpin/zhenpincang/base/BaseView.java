package com.cang.zhenpin.zhenpincang.base;

import android.support.annotation.StringRes;

/**
 * Created by victor on 2017/11/22.
 * Email: wwmdirk@gmail.com
 */

public interface BaseView {

    void showTip(String tip);

    void showTip(@StringRes int resId);
}
