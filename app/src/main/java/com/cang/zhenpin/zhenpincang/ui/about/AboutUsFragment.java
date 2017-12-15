package com.cang.zhenpin.zhenpincang.ui.about;


import android.net.Network;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.victor.loadinglayout.LoadingLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {

    private LoadingLayout mLoadingLayout;
    private TextView mTvAbout;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }
    public AboutUsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);
        mLoadingLayout = v.findViewById(R.id.loading_layout);
        mTvAbout = v.findViewById(R.id.tv_about);
        mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAbout();
            }
        });
        getAbout();
        return v;
    }

    private void getAbout() {
        NetWork.getsBaseApi()
                .aboutUs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<String>>(getActivity()) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        mLoadingLayout.showLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mLoadingLayout.showError();
                    }

                    @Override
                    public void onNext(BaseResult<String> stringBaseResult) {
                        super.onNext(stringBaseResult);
                        mTvAbout.setText(stringBaseResult.getData());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mLoadingLayout.showContent();
                    }
                });
    }
}
