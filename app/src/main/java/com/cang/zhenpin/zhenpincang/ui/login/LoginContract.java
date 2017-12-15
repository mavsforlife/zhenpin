package com.cang.zhenpin.zhenpincang.ui.login;

import com.cang.zhenpin.zhenpincang.base.BasePresenter;
import com.cang.zhenpin.zhenpincang.base.BaseView;

/**
 * Created by victor on 2017/11/23.
 * Email: wwmdirk@gmail.com
 */

public interface LoginContract {

    interface View extends BaseView {

        void weChatLogin();
    }

    interface Presenter extends BasePresenter {

        void onGoToWeChat();
    }
}
