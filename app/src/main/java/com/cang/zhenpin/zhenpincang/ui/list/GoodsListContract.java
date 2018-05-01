package com.cang.zhenpin.zhenpincang.ui.list;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.model.BrandForFuckList;

import java.io.File;
import java.util.List;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */

public interface GoodsListContract {

    interface View extends BaseView {

        void addBrandList(List<Brand> list, boolean isRefresh);

        void showEmpty();

        void onError();

        void hasMoreData(boolean bool);

        void setNoticeText(String notice);

        void setIsLoading(boolean isLoading);

        void shareTo(List<File> list, String desc);

        void shareFail();

        void showHideProgress(boolean isShow);

        void setUpShotView(List<String> list, List<Integer> params, String desc, String fileName);

        void addFuckList(BrandForFuckList fuckList);
    }

    interface Presenter extends BasePresenter {

        void onLoadData(boolean isRefresh);

        void onShareNormal(int position);

        void onShareCompose(int position);

        void onShotToFile(LinearLayout ll, String desc, String fileName);

        void ensureUserInfo();
    }
}
