package com.cang.zhenpin.zhenpincang.ui.list;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.ui.register.RegisterActivity;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ShareUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.cang.zhenpin.zhenpincang.widget.TopDividerItemDecoration;
import com.victor.loadinglayout.LoadingLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoodsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoodsListFragment extends Fragment implements GoodsListContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        GoodsListAdapter.ShareListener {

    private SwipeRefreshLayout mSrl;
    private RecyclerView mRv;
    private LoadingLayout mLoadingLayout;
    private TextView mTvNotice;
    private AppBarLayout mAppBar;
    private GoodsListAdapter mAdapter;
    private GoodsListContract.Presenter mPresenter;
    private LinearLayoutManager mLayoutManager;

    private LinearLayout mLlAll;
    private ImageView mIv1, mIv2, mIv3, mIv4;
    private TextView mTv1;
    private LinearLayout mProgress;

    private static final String BRAND_ID = "param1";
    private static final String IS_ATTENTIION = "is_attention";

    private String mBrandId;
    private boolean mIsAttention;

    private boolean mHasMoreData = true; //是否加载更多数据
    private boolean mIsLoading;         //是否在加载中
    private Intent mShareIntent;
    private int mPos;

    public GoodsListFragment() {
        // Required empty public constructor
    }

    public static GoodsListFragment newInstance() {
        GoodsListFragment fragment = new GoodsListFragment();
        return fragment;
    }

    public static GoodsListFragment newInstance(String brandId, boolean isAttention) {
        GoodsListFragment fragment = new GoodsListFragment();
        Bundle args = new Bundle();
        args.putString(BRAND_ID, brandId);
        args.putBoolean(IS_ATTENTIION, isAttention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBrandId = getArguments().getString(BRAND_ID);
            mIsAttention = getArguments().getBoolean(IS_ATTENTIION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_list, container, false);
        mAppBar = v.findViewById(R.id.app_bar);
        mRv = v.findViewById(R.id.rv_goods);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(mLayoutManager);
        mRv.addItemDecoration(new TopDividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL, DeviceUtil.dip2px(getActivity(),
                0.5f)));
        mRv.addOnScrollListener(mOnScrollListener);
        mAdapter = new GoodsListAdapter(getActivity(), this, mIsAttention, !TextUtils.isEmpty(mBrandId));
        mRv.setAdapter(mAdapter);
        mSrl = v.findViewById(R.id.swipe_refresh_layout);
        mSrl.setColorSchemeResources(R.color.colorPrimary);
        mSrl.setOnRefreshListener(this);
        mSrl.setRefreshing(true);
        mTvNotice = v.findViewById(R.id.tv_notice);
        mLoadingLayout = v.findViewById(R.id.loading_layout);
        mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSrl.isRefreshing()) return;
                mSrl.setEnabled(false);
                mLoadingLayout.showLoading();
                mPresenter.onLoadData(true);
            }
        });
        mLoadingLayout.showContent();
        mPresenter = new GoodsListPresenter(getActivity(), this,
                mBrandId, mIsAttention);
        mPresenter.onLoadData(true);
        initShotView(v);
        return v;
    }

    private void initShotView(View v) {
        mIv1 = v.findViewById(R.id.iv1);
        mIv2 = v.findViewById(R.id.iv2);
        mIv3 = v.findViewById(R.id.iv3);
        mIv4 = v.findViewById(R.id.iv4);
        mTv1 = v.findViewById(R.id.tv1);
        mLlAll = v.findViewById(R.id.ll_all);
        mProgress = v.findViewById(R.id.ll_progress);
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int totalCount = mLayoutManager.getItemCount();
            if (lastVisibleItem + 1 == totalCount && mHasMoreData && !mIsLoading) {
                mPresenter.onLoadData(false);
            }
        }
    };

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(getActivity(), tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(getActivity(), resId);
    }

    @Override
    public void onRefresh() {
        mPresenter.onLoadData(true);
    }

    @Override
    public void addBrandList(List<Brand> list, boolean isRefresh) {
        mSrl.setEnabled(true);
        if (null == list || list.size() == 0) {
            return;
        }
        mLoadingLayout.showContent();
        if (isRefresh) {
            mAdapter.setData(list);
        } else {
            mAdapter.addData(list);
        }
    }

    @Override
    public void showEmpty() {
        mSrl.setEnabled(true);
        mAdapter.clear();
        mLoadingLayout.showEmpty();
    }

    @Override
    public void onError() {
        if (mAdapter.isEmpty()) {
            mLoadingLayout.showError();
            mSrl.setEnabled(true);
        } else {
            mAdapter.showFooterError();
        }
    }

    @Override
    public void hasMoreData(boolean bool) {
        mHasMoreData = bool;
        mAdapter.setHasMoreData(mHasMoreData);
    }

    @Override
    public void setNoticeText(String notice) {
        if (TextUtils.isEmpty(notice) || !TextUtils.isEmpty(mBrandId)) {
            mAppBar.setVisibility(View.GONE);
        } else {
            mTvNotice.setText(String.format(Locale.getDefault(), getString(R.string.notice_content), notice));
            mAppBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
        if (!isLoading && mSrl.isRefreshing()) {
            mSrl.setRefreshing(false);
        }
    }

    @Override
    public void shareTo(final List<File> list, final String desc) {
        if (list == null || list.size() == 0) {
            showTip(getString(R.string.no_pic));
            return;
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShareIntent = ShareUtil.BuildShareIntent(getActivity(), list, desc);
                dismissProgressDialog();
                startActivity(mShareIntent);
            }
        });

    }

    @Override
    public void shareFail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                showTip(getString(R.string.save_pic_fail_please_retry));
            }
        });
    }

    @Override
    public void showHideProgress(boolean isShow) {
        if (isShow) {
            showProgressDialog();
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void setUpShotView(final List<String> list, final List<Integer> params, final String desc, final String fileName) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (list == null || list.size() != 4) {
                    showTip("没有图片，分享失败");
                    return;
                }
                io.reactivex.Observable.just(mLlAll)
                        .map(new Function<LinearLayout, LinearLayout>() {
                            @Override
                            public LinearLayout apply(LinearLayout linearLayout) throws Exception {
                                ViewGroup.LayoutParams lp = mLlAll.getLayoutParams();
                                int width = DeviceUtil.getScreenWidthPixel(getActivity());
                                int height = (int) (((float) params.get(1)) / ((float) params.get(0)) * width * 0.75f);
                                lp.width = width;
                                lp.height = height;
                                mLlAll.setLayoutParams(lp);
                                return mLlAll;
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LinearLayout>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LinearLayout linearLayout) {
                                GlideApp.with(getActivity()).load(list.get(0)).centerCrop().into(mIv1);
                                GlideApp.with(getActivity()).load(list.get(1)).centerCrop().into(mIv2);
                                GlideApp.with(getActivity()).load(list.get(2)).centerCrop().into(mIv3);
                                GlideApp.with(getActivity()).load(list.get(3)).centerCrop().into(mIv4);
                                mTv1.setText(desc);
                            }

                            @Override
                            public void onError(Throwable e) {
                                shareFail();
                            }

                            @Override
                            public void onComplete() {
                                mLlAll.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPresenter.onShotToFile(mLlAll, desc, fileName);
                                    }
                                }, 1000);
                            }
                        });
            }
        });
    }

    private void showShareDialog() {
        AlertDialog.Builder shareDialog = DialogUtil.showShareDialog(getActivity());
        shareDialog
                .setNegativeButton(R.string.normal_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onShareNormal(mPos);
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.compose_share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.onShareCompose(mPos);
                        dialog.dismiss();

                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onShare(int position) {
        if (!ShareUtil.isShareEnabled()) {
            showShareTip();
            return;
        }
        showShareDialog();
        mPos = position;
    }

    private void showShareTip() {
        new AlertDialog.Builder(getActivity())
                .setMessage("只有代理用户才能分享")
                .setPositiveButton("申请代理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().startActivity(RegisterActivity.createIntent(getActivity()));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onRetry() {
        mPresenter.onLoadData(false);
    }

    @Override
    public void onEmpty() {
        mLoadingLayout.showEmpty();
    }

    private void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
