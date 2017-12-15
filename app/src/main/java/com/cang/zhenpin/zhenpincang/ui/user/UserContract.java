package com.cang.zhenpin.zhenpincang.ui.user;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;

/**
 * Created by victor on 2017/12/12.
 * Email: wwmdirk@gmail.com
 */

public interface UserContract {

    interface View extends BaseView {

        void setFileSize();

        void setUserType();
    }

    interface Presenter extends BasePresenter {

        void apply();

        void contactUs();

        void checkUpdate();

        void clearCache();

        void logOut();
    }
}
