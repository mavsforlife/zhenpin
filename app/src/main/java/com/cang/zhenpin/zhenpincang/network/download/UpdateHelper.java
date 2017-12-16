package com.cang.zhenpin.zhenpincang.network.download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;

import com.cang.zhenpin.zhenpincang.BuildConfig;
import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.AppInfoModel;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.BaseActivitySlienceObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UpdatePreferences;
import com.cang.zhenpin.zhenpincang.util.FileUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cang.zhenpin.zhenpincang.base.BaseConstants.APK_NAME;
import static com.cang.zhenpin.zhenpincang.base.BaseConstants.APPLICATION_PACKAGE_ARCHIVE;
import static com.cang.zhenpin.zhenpincang.base.BaseConstants.FILE_PROVIDER_NAME;

/**
 * Created by victor on 2017/12/14.
 * Email: wwmdirk@gmail.com
 */

public class UpdateHelper {

    private Context mContext;
    private boolean mIsForce;
    private String mUrl;
    private String mUpdateMsg;
    private ProgressDialog mDialog;
    private MyHandler mHandler;
    private UpdatePreferences mPreferences;

    public UpdateHelper(Context context) {
        mContext = context;
        mPreferences = PreferencesFactory.getUpdatePref();
    }


    public void getUpdateInfo(final boolean isAuto) {
        NetWork.getsBaseApi()
                .getUpdateInfo(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivitySlienceObserver<BaseResult<AppInfoModel>>(mContext) {
                    @Override
                    public void onNext(BaseResult<AppInfoModel> appInfoModelBaseResult) {
                        super.onNext(appInfoModelBaseResult);
                        AppInfoModel info = appInfoModelBaseResult.getData();
                        if (info != null) {
                            checkUpdate(info, isAuto);
                        }
                    }
                });
    }

    private void checkUpdate(AppInfoModel info, boolean isAuto) {
        mUrl = info.getUpdateUrl();
        mIsForce = info.getIsForce() != 0;
        mUpdateMsg = info.getDesc();
        if (BuildConfig.VERSION_CODE < info.getVerCode()) {
            if (isAuto) {
                if (mPreferences.needShowDialog() || mIsForce) {
                    showUpdateDialog();
                }
            } else {
                showUpdateDialog();
            }
        } else {
            if (!isAuto) {
                ToastUtil.showShort(mContext, R.string.already_update_version);
            }
        }
    }

    private void showUpdateDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("发现新版本")
                .setMessage(mUpdateMsg)
                .setCancelable(!mIsForce)
                .setPositiveButton(R.string.update_right_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        download();
                    }
                })
                .show();
        mPreferences.setLastCheck(SystemClock.elapsedRealtime());
    }
    private void download() {
        showProgressDialog();
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);

                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = download;
                mHandler.sendMessage(msg);
            }
        };

        final File file = new File(mContext.getExternalCacheDir(), APK_NAME);
        new DownGenerator(mUrl, listener)
                .downloadAPK(mUrl, file, new BaseActivityObserver(mContext) {

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        onDownloadComplete(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showShort(mContext, R.string.download_fail);
                        dismissDialog();
                    }

                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                    }
                });
    }

    private void onDownloadComplete(File file) {
        installApk(file);
        dismissDialog();
        finish();
    }

    private void showProgressDialog() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setTitle(mContext.getString(R.string.downloading));
        mDialog.setMessage(mContext.getString(R.string.wait_for_download_complete));
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setProgress(0);
        mDialog.setCancelable(false);
        mDialog.setMax(100);
        mDialog.show();
        mHandler = new MyHandler(mDialog);
    }

    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void finish() {
        if (mIsForce) {
            ((Activity)mContext).finish();
        }
    }
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER_NAME, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, APPLICATION_PACKAGE_ARCHIVE);
        } else {
            intent.setDataAndType(Uri.fromFile(file), APPLICATION_PACKAGE_ARCHIVE);
        }
        mContext.startActivity(intent);
    }

    static class MyHandler extends Handler {
        private final WeakReference<ProgressDialog> ref;

        MyHandler(ProgressDialog dialog) {
            ref = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialog dialog = ref.get();
            if (dialog == null) return;
            if (msg.what == 1) {
                Download download = (Download) msg.obj;
                dialog.setProgressNumberFormat(String.format(Locale.getDefault(),
                        "%1s/%2s",
                        FileUtil.getFormatSize(download.getCurrentFileSize()),
                        FileUtil.getFormatSize(download.getTotalFileSize())));
                dialog.setProgress(download.getProgress());
                if (download.getProgress() == 100) {
                    dialog.setMessage(dialog.getContext().getString(R.string.download_complete));
                }
            }
        }
    }

}
