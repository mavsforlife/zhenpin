package com.cang.zhenpin.zhenpincang.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

public class EditCountDialog extends AlertDialog {

    protected EditCountDialog(@NonNull Context context) {
        super(context);
    }

    protected EditCountDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EditCountDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

}
