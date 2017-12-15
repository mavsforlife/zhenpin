package com.cang.zhenpin.zhenpincang.ui.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.LinearLayout;

import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.model.BrandList;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.util.DownLoadImageUtil;
import com.cang.zhenpin.zhenpincang.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */

public class GoodsListPresenter implements GoodsListContract.Presenter {

    private Context mContext;
    private GoodsListContract.View mView;
    private int mCurrPage;
    private int mPageAll;
    private List<Brand> mList;
    private List<File> mFiles;

    private String mBrandId;
    private boolean mIsAttention;
    private int mUId;

    public GoodsListPresenter(Context context, GoodsListContract.View view,
                              String brandId, boolean isAttention) {
        mContext = context;
        mView = view;
        mCurrPage = 1;
        mBrandId = brandId;
        mIsAttention = isAttention;
        mList = new ArrayList<>();
        mFiles = new ArrayList<>();
        mUId = PreferencesFactory.getUserPref().getId();
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
    public void onLoadData(boolean isRefresh) {
        if (isRefresh) {
            mCurrPage = 1;
        }
        if (mIsAttention) {
            getAttentionList(isRefresh);
        } else {
            getBaseBrandList(isRefresh);
        }
    }

    private void getBaseBrandList(final boolean isRefresh) {
        NetWork.getsBaseApi()
                .brandList(mCurrPage, null, mBrandId, mUId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<BrandList>>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.setIsLoading(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setIsLoading(false);
                        mView.onError();
                    }

                    @Override
                    public void onNext(BaseResult<BrandList> brandListBaseResult) {
                        super.onNext(brandListBaseResult);
                        BrandList data = brandListBaseResult.getData();
                        if (null != data) {
                            mView.setNoticeText(data.getNotice());
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
                            mPageAll = data.getPageAll();
                            mView.hasMoreData(mPageAll > mCurrPage);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mCurrPage++;
                        mView.setIsLoading(false);
                    }
                });
    }

    private void getAttentionList(final boolean isRefresh) {
        NetWork.getsBaseApi()
                .concernList(mUId, mCurrPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResult<BrandList>>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mView.setIsLoading(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.setIsLoading(false);
                        mView.onError();
                    }

                    @Override
                    public void onNext(BaseResult<BrandList> brandListBaseResult) {
                        super.onNext(brandListBaseResult);
                        BrandList data = brandListBaseResult.getData();
                        if (null != data) {
                            mView.setNoticeText(data.getNotice());
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
                            mPageAll = data.getPageAll();
                            mView.hasMoreData(mPageAll > mCurrPage);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mCurrPage++;
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
}
