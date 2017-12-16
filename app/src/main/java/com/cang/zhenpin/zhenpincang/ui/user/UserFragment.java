package com.cang.zhenpin.zhenpincang.ui.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.RefreshLoginEvent;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.pref.UserPreferences;
import com.cang.zhenpin.zhenpincang.util.FileUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements UserContract.View, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView mIvAvatar;
    private TextView mTvNick, mTvPhone, mTvId;
    private LinearLayout mLlApply, mLlStatus, mLlContact, mLlCheck, mLlClear, mLlLogOut;
    private TextView mTvStatus, mTvFileSize;

    private UserPreferences mUserPreferences;
    private UserPresenter mPresenter;

    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mUserPreferences = PreferencesFactory.getUserPref();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        initView(v);
        initData();
        mPresenter = new UserPresenter(this, getActivity());
        return v;
    }

    private void initView(View v) {
        mIvAvatar = v.findViewById(R.id.iv_avatar);
        mTvNick = v.findViewById(R.id.tv_nick);
        mTvPhone = v.findViewById(R.id.tv_phone);
        mTvId = v.findViewById(R.id.tv_id);

        mLlApply = v.findViewById(R.id.ll_apply);
        mLlApply.setOnClickListener(this);
        mLlStatus = v.findViewById(R.id.ll_status);
//        mLlStatus.setOnClickListener(this);
        mTvStatus = v.findViewById(R.id.tv_status);
        mLlContact = v.findViewById(R.id.ll_contact_us);
        mLlContact.setOnClickListener(this);
        mLlCheck = v.findViewById(R.id.ll_check_update);
        mLlCheck.setOnClickListener(this);
        mLlClear = v.findViewById(R.id.ll_clear_cache);
        mLlClear.setOnClickListener(this);
        mTvFileSize = v.findViewById(R.id.tv_file_size);
        mLlLogOut = v.findViewById(R.id.ll_log_out);
        mLlLogOut.setOnClickListener(this);
    }

    private void initData() {
        setUserInfo();
        setFileSize();
        setUserType();
    }

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(getActivity(), tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(getActivity(), resId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_apply:
                mPresenter.apply();
                break;
            case R.id.ll_contact_us:
                mPresenter.contactUs();
                break;
            case R.id.ll_check_update:
                mPresenter.checkUpdate();
                break;
            case R.id.ll_clear_cache:
                mPresenter.clearCache();
                break;
            case R.id.ll_log_out:
                mPresenter.logOut();
                break;
        }
    }

    @Override
    public void setFileSize() {
        if (mTvFileSize != null) {
            mTvFileSize.setText(FileUtil.getFormatSize(FileUtil.getFolderSize(getActivity().getExternalCacheDir())));
        }
    }

    @Override
    public void setUserType() {
        mTvStatus.setText(mUserPreferences.getUserTypeStr());
    }

    private void setUserInfo() {
        GlideApp.with(getActivity())
                .load(mUserPreferences.getHeadImgUrl())
                .error(R.color.gray)
                .placeholder(R.color.gray)
                .into(mIvAvatar);
        mTvNick.setText(String.format(Locale.getDefault(), getString(R.string.user_nick), mUserPreferences.getUserId()));
        mTvId.setText(String.format(Locale.getDefault(), getString(R.string.user_id), String.valueOf(mUserPreferences.getId())));
        mTvPhone.setText(String.format(Locale.getDefault(), getString(R.string.user_phone), mUserPreferences.getUserPhone()));
    }

    @Subscribe
    public void onRefreshUserType(RefreshLoginEvent event) {
        setUserType();
        setUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
