package com.cang.zhenpin.zhenpincang.ui.search;

import android.widget.LinearLayout;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;
import com.cang.zhenpin.zhenpincang.model.Brand;

import java.io.File;
import java.util.List;

/**
 * Created by victor on 2017/12/16.
 * Email: wwmdirk@gmail.com
 */

public interface SearchContract {

    interface View extends BaseView {

        void addBrandList(List<Brand> list, boolean isRefresh);

        void showEmpty();

        void onError();

        void hasMoreData(boolean bool);

        void setIsLoading(boolean isLoading);

        void showLoading();

        void shareTo(List<File> list, String desc);

        void shareFail();

        void showHideProgress(boolean isShow);

        void setUpShotView(List<String> list, List<Integer> params, String desc, String fileName);
    }

    interface Presenter extends BasePresenter {

        void onLoadData(String keyword, boolean isRefresh);

        void onShareNormal(int position);

        void onShareCompose(int position);

        void onShotToFile(LinearLayout ll, String desc, String fileName);

        void refreshUserInfo();
    }
}
