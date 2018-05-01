package com.cang.zhenpin.zhenpincang.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.RefreshLoginEvent;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.model.BrandList;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UserPreferences;
import com.cang.zhenpin.zhenpincang.util.DownLoadImageUtil;
import com.cang.zhenpin.zhenpincang.util.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2017/12/16.
 * Email: wwmdirk@gmail.com
 */

public class SearchPresenter implements SearchContract.Presenter {

    private Context mContext;
    private SearchContract.View mView;
    private int mCurrentPage;
    private int mUid;
    private int mCurrPage;
    private ArrayList<Brand> mList;
    private ArrayList<File> mFiles;

    public SearchPresenter(Context context, SearchContract.View view) {
        mContext = context;
        mView = view;
        mUid = PreferencesFactory.getUserPref().getId();
        mList = new ArrayList<>();
        mFiles = new ArrayList<>();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void onLoadData(String keyword, final boolean isRefresh) {
        if (TextUtils.isEmpty(keyword)) {
            mView.showTip("关键字不可为空！");
            return;
        }
        if (isRefresh) {
            mCurrentPage = 1;
            mList.clear();
        }
        NetWork.getsBaseApi()
                .brandList(mCurrentPage, keyword, null, mUid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<BrandList>>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.setIsLoading(true);
                        if (isRefresh) {
                            mView.showLoading();
                        }
                    }

                    @Override
                    public void onNext(BaseResult<BrandList> brandListBaseResult) {
                        super.onNext(brandListBaseResult);
                        BrandList data = brandListBaseResult.getData();
                        if (null != data) {
                            List<Brand> list = data.getBrands();
                            if (list != null && list.size() > 0) {
                                mList.addAll(list);
                                mView.addBrandList(list, isRefresh);
                            } else {
                                if (isRefresh) {
                                    mView.showEmpty();
                                }
                            }
                            mCurrPage = data.getPageNow();
                            int pageAll = data.getPageAll();
                            mView.hasMoreData(pageAll > mCurrPage);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setIsLoading(false);
                        mView.onError();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.setIsLoading(false);
                    }
                });
    }

    @Override
    public void onShareNormal(int position) {
        final Brand brand = mList.get(position);
        Observable.fromIterable(brand.getPicPath())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String path) throws Exception {
                        String fileName = brand.getId() + "_" + StringUtil.getImgName(path);
                        return DownLoadImageUtil.savePicture(mContext, fileName, path);
                    }
                })
                .subscribe(new BaseObserver<File>(mView) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mFiles.clear();
                        mView.showHideProgress(true);
                    }

                    @Override
                    public void onNext(File file) {
                        super.onNext(file);
                        mFiles.add(file);
                        Log.d("save", "图片已成功保存到" + file.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.shareFail();
                        e.printStackTrace();
                        Log.d("save", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.shareTo(mFiles, brand.getDesc());
                    }
                });
    }

    @Override
    public void onShareCompose(int position) {
        final Brand brand = mList.get(position);
        final List<String> urlList;
        if (brand.getPicPath().size() >= 4) {
            urlList = brand.getPicPath().subList(0, 4);
        } else {
            mView.showTip("图片不足4张，分享失败");
            return;
        }

        final String fileName = "compose_" + brand.getId() + ".jpg";
        if (DownLoadImageUtil.checkFileExist(mContext, fileName)) {
            mFiles.clear();
            mFiles.add(new File(mContext.getExternalCacheDir() + DownLoadImageUtil.IMAGE_PATH + fileName));
            mView.shareTo(mFiles, brand.getDesc());
            return;
        }
        final List<Integer> params = new ArrayList<>();
        Observable.just(urlList.get(0))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<String, List<Integer>>() {
                    @Override
                    public List<Integer> apply(String s) throws Exception {
                        Bitmap bitmap = DownLoadImageUtil.getBitmap(mContext, s);
                        params.add(bitmap.getWidth());
                        params.add(bitmap.getHeight());
                        return params;
                    }
                })
                .subscribe(new BaseObserver<List<Integer>>(mView) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mFiles.clear();
                        mView.showHideProgress(true);
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        super.onNext(integers);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.shareFail();
                        e.printStackTrace();
                        Log.d("save", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.setUpShotView(urlList, params, brand.getDesc(), fileName);
                    }
                });
    }

    @Override
    public void onShotToFile(final LinearLayout ll, final String desc, final String fileName) {
        Observable.just(ll)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<LinearLayout, File>() {
                    @Override
                    public File apply(LinearLayout linearLayout) throws Exception {
                        return DownLoadImageUtil.getCacheBitmapFromView(ll, fileName);
                    }
                })
                .subscribe(new BaseObserver<File>(mView) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onNext(File file) {
                        super.onNext(file);
                        mFiles.add(file);
                        Log.d("save", "图片已成功保存到" + file.getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.shareFail();
                        e.printStackTrace();
                        Log.d("save", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.shareTo(mFiles, desc);
                    }
                });
    }

    @Override
    public void refreshUserInfo() {
        final UserPreferences preferences = PreferencesFactory.getUserPref();
        final int userType = preferences.getUserType();

        String openId = preferences.getOpenId();
        String nickName = preferences.getUserId();
        String headImgUrl = preferences.getHeadImgUrl();
        int sex = preferences.getUserSex();
        NetWork.getsBaseApi()
                .wxLogin(openId, nickName, headImgUrl, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<UserInfo>>(mView) {
                    @Override
                    public void onNext(BaseResult<UserInfo> userInfoBaseResult) {
                        super.onNext(userInfoBaseResult);
                        UserInfo info = userInfoBaseResult.getData();
                        if (null != info) {
                            preferences.saveUserInfo(userInfoBaseResult.getData());
                            if (userType == info.getMUserType()) return;
                            EventBus.getDefault().post(new RefreshLoginEvent());
                            if (info.getMUserType() == UserInfo.TYPE_AGENT_INT) {
                                mView.showTip(R.string.congratulate_to_apply_success);
                            } else if (info.getMUserType() == UserInfo.TYPE_REJECT_INT) {
                                mView.showTip(R.string.apply_reject_and_contact_us);
                            }
                        }

                    }
                });
    }
}
