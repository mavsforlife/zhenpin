package com.cang.zhenpin.zhenpincang.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.base.App;

/**
 * Created by victor on 2017/11/26.
 * Email: wwmdirk@gmail.com
 */

public class DialogUtil {

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.prepare_pic));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static AlertDialog.Builder showShareDialog(Context context) {

        AlertDialog.Builder mShareDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.good_describe_already_copied)
                .setMessage(R.string.can_paste_on_we_chat_and_qq);

        return mShareDialog;
    }

    private static ProgressDialog mProgressDialog;
    public static void showProgressDialog(Context context, String string) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(string);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void showProgressDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }
}
