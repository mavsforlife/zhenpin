package com.cang.zhenpin.zhenpincang.ui.list;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.text.TextUtils;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.BaseApi;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import java.lang.ref.WeakReference;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2017/12/11.
 * Email: wwmdirk@gmail.com
 */

public class AttentionHelper {
    private static int mId = PreferencesFactory.getUserPref().getId();

    public static void Attention(final GoodsListAdapter adapter, final Brand brand, final int position, final Context context) {
        if (brand != null) {
            int isAttention = brand.getIsAttention();
            String id = brand.getId();
            final String path = isAttention == Brand.ATTENTION ? BaseApi.CONCERN_CANCEL : BaseApi.CONCERN;
            NetWork.getsBaseApi()
                    .attention(path, mId, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseActivityObserver<BaseResult>(context) {

                        @Override
                        public void onSubscribe(Disposable d) {
                            super.onSubscribe(d);
                            DialogUtil.showProgressDialog(new WeakReference<>(context));
                        }

                        @Override
                        public void onNext(BaseResult baseResult) {
                            super.onNext(baseResult);
                                brand.setIsAttention(TextUtils.equals(path, BaseApi.CONCERN) ?
                                        Brand.ATTENTION :
                                        Brand.NOT_ATTENTION);
                            if (!adapter.isAttention()) {
                                adapter.notifyItemChanged(position, true);
                            } else {
                                if (TextUtils.equals(path, BaseApi.CONCERN_CANCEL)) {
                                    adapter.removeItem(position);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            DialogUtil.dismissProgressDialog();
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            DialogUtil.dismissProgressDialog();
                            ToastUtil.showShort(context,
                                    TextUtils.equals(path, BaseApi.CONCERN) ?
                                            "关注成功" : "取消关注成功");
                        }
                    });
        }
    }
}
