package com.cang.zhenpin.zhenpincang;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.event.RefreshLoginEvent;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.BaseObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.network.download.UpdateHelper;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UserPreferences;
import com.cang.zhenpin.zhenpincang.ui.about.AboutUsFragment;
import com.cang.zhenpin.zhenpincang.ui.cart.ShoppingCartFragment;
import com.cang.zhenpin.zhenpincang.ui.list.GoodsListFragment;
import com.cang.zhenpin.zhenpincang.ui.login.LoginActivity;
import com.cang.zhenpin.zhenpincang.ui.search.SearchActivity;
import com.cang.zhenpin.zhenpincang.ui.user.UserFragment;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShoppingCartFragment.OnStatusEndListener {

    LinearLayout mContainer;
    RadioGroup mRadioGroup;
    RadioButton mHome;
    RadioButton mAttention;
    RadioButton mUser;
    RadioButton mAbout;
    TextView mTvBar;
    ImageView mIvSearch;
    TextView mTvEdit;

    private boolean mIsEdit;

    private List<Fragment> mFragments;
    private int position;   //当前选中的位置
    private Fragment mFragment;

    private UserPreferences mUserPreferences;
    private long mPressTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserPreferences = PreferencesFactory.getUserPref();
        if (!mUserPreferences.isLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        initView();
        initData();
        ensureUserInfo();
        getUpdateInfo();
    }

    private void initView() {
        mContainer = findViewById(R.id.container);
        mRadioGroup = findViewById(R.id.radio_group);
        mHome = findViewById(R.id.rb_home);
        mAttention = findViewById(R.id.rb_atten);
        mUser = findViewById(R.id.rb_user);
        mAbout = findViewById(R.id.rb_about);
        mTvBar = findViewById(R.id.tv_bar);
        mIvSearch = findViewById(R.id.iv_search);
        mIvSearch.setVisibility(View.VISIBLE);
        mIvSearch.setOnClickListener(this);
        mTvEdit = findViewById(R.id.tv_edit);
        mTvEdit.setOnClickListener(this);
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(GoodsListFragment.newInstance());
        mFragments.add(GoodsListFragment.newInstance(null, true));
        ShoppingCartFragment scf = ShoppingCartFragment.newInstance();
        scf.setOnStatusEndListener(this);
        mFragments.add(scf);
        mFragments.add(UserFragment.newInstance());

        mRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mRadioGroup.check(R.id.rb_home);
    }

    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    position = 0;
                    setRightIcon(false);
                    setTitle(R.string.app_name);
                    break;

                case R.id.rb_atten:
                    position = 1;
                    setRightIcon(false);
                    setTitle(R.string.attention);
                    break;

                case R.id.rb_user:
                    position = 2;
                    setRightIcon(true);
                    setTitle(R.string.shopping_cart);
                    break;

                case R.id.rb_about:
                    position = 3;
                    Fragment fragment = mFragments.get(3);
                    if (fragment != null && fragment instanceof UserFragment) {
                        UserFragment userFragment = (UserFragment) fragment;
                        userFragment.setFileSize();
                    }
                    setRightIcon(false);
                    setTitle(R.string.mine);
                    break;
            }
            Fragment currentFragment = mFragments.get(position);
            replaceFragment(mFragment, currentFragment);
        }
    };

    private void replaceFragment(Fragment lastFragment, Fragment currentFragment) {
        if (lastFragment == currentFragment) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        mFragment = currentFragment;
        //隐藏刚显示的Fragment
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        /**
         * 显示 或者 添加当前要显示的Fragment
         *
         * 如果当前要显示的Fragment没添加过 则 添加
         * 如果当前要显示的Fragment被添加过 则 隐藏
         */
        if (!currentFragment.isAdded()) {
            transaction.add(R.id.container, currentFragment).commit();
        } else {
            transaction.show(currentFragment).commit();
            if (currentFragment instanceof ShoppingCartFragment) {
                ShoppingCartFragment scf = (ShoppingCartFragment) currentFragment;
                scf.onRefresh();
            }
        }
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public void setTitle(@StringRes int idRes) {
        mTvBar.setText(idRes);
    }

    private void setRightIcon(boolean isShowEdit) {
        mIvSearch.setVisibility(isShowEdit ? View.GONE : View.VISIBLE);
        mTvEdit.setVisibility(isShowEdit ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_search) {
            startActivity(SearchActivity.createIntent(this));
        } else if (v.getId() == R.id.tv_edit) {
            Fragment fragment = mFragments.get(2);
            if (fragment != null && fragment instanceof ShoppingCartFragment) {
                ShoppingCartFragment scf = (ShoppingCartFragment) fragment;
                mIsEdit = !mIsEdit;
                scf.onEditClick(mIsEdit);
                mTvEdit.setText(mIsEdit ? R.string.sure : R.string.edit);
            }
        }
    }

    private void ensureUserInfo() {
        final int userType = mUserPreferences.getUserType();

        String openId = mUserPreferences.getOpenId();
        String nickName = mUserPreferences.getUserId();
        String headImgUrl = mUserPreferences.getHeadImgUrl();
        int sex = mUserPreferences.getUserSex();
        NetWork.getsBaseApi()
                .wxLogin(openId, nickName, headImgUrl, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<UserInfo>>(this) {
                    @Override
                    public void onNext(BaseResult<UserInfo> userInfoBaseResult) {
                        super.onNext(userInfoBaseResult);
                        UserInfo info = userInfoBaseResult.getData();
                        if (null != info) {
                            mUserPreferences.saveUserInfo(userInfoBaseResult.getData());
                            EventBus.getDefault().post(new RefreshLoginEvent());
                            if (userType == UserPreferences.DEFAULT_ERROR || userType == info.getMUserType()) return;
                            if (info.getMUserType() == UserInfo.TYPE_AGENT_INT) {
                                ToastUtil.showShort(MainActivity.this, R.string.congratulate_to_apply_success);
                            } else if (info.getMUserType() == UserInfo.TYPE_REJECT_INT) {
                                ToastUtil.showShort(MainActivity.this, R.string.apply_reject_and_contact_us);
                            }
                        }

                    }
                });
    }

    private void getUpdateInfo() {
        UpdateHelper updateHelper = new UpdateHelper(this);
        updateHelper.getUpdateInfo(true);
    }

    @Override
    public void onBackPressed() {
        if ((SystemClock.elapsedRealtime() - mPressTime) > 2000L) {
            ToastUtil.showShort(this, R.string.next_press_quit);
            mPressTime = SystemClock.elapsedRealtime();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onEditDone() {
        mIsEdit = false;
        mTvEdit.setText(R.string.edit);
    }
}
