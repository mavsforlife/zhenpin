package com.cang.zhenpin.zhenpincang.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.model.UserInfo;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.list.GoodsListAdapter;
import com.cang.zhenpin.zhenpincang.ui.register.RegisterActivity;
import com.cang.zhenpin.zhenpincang.util.DeviceUtil;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ShareUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.cang.zhenpin.zhenpincang.widget.TopDividerItemDecoration;
import com.victor.loadinglayout.LoadingLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class SearchActivity extends AppCompatActivity implements SearchContract.View, GoodsListAdapter.ShareListener, View.OnClickListener {

    private EditText mSearch;
    private TextView mCancel;
    private RecyclerView mRv;
    private LinearLayoutManager mLayoutManager;
    private GoodsListAdapter mAdapter;
    private LoadingLayout mLoadingLayout;
    private SearchPresenter mPresenter;

    private LinearLayout mLlAll;
    private ImageView mIv1, mIv2, mIv3, mIv4;
    private TextView mTv1;
    private LinearLayout mProgress;

    private boolean mHasMoreData = true; //是否加载更多数据
    private boolean mIsLoading;         //是否在加载中
    private Intent mShareIntent;
    private int mScreenWidth;
    private int mPos;
    private String mKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initShotView();
    }

    private void initView() {
        mSearch = findViewById(R.id.et_keyword);
        mSearch.setOnKeyListener(mOnKeyListener);
        mCancel = findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);
        mRv = findViewById(R.id.rv_goods);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mLoadingLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onLoadData(mKeyword, true);
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(mLayoutManager);
        mRv.addItemDecoration(new TopDividerItemDecoration(this,
                LinearLayoutManager.VERTICAL, DeviceUtil.dip2px(this,
                0.5f)));
        mRv.addOnScrollListener(mOnScrollListener);
        mAdapter = new GoodsListAdapter(this, this, false, false);
        mRv.setAdapter(mAdapter);
        mPresenter = new SearchPresenter(this, this);
        mScreenWidth = DeviceUtil.getScreenWidthPixel(SearchActivity.this);
    }

    private void initShotView() {
        mIv1 = findViewById(R.id.iv1);
        mIv2 = findViewById(R.id.iv2);
        mIv3 = findViewById(R.id.iv3);
        mIv4 = findViewById(R.id.iv4);
        mTv1 = findViewById(R.id.tv1);
        mLlAll = findViewById(R.id.ll_all);
        mProgress = findViewById(R.id.ll_progress);
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
                mPresenter.onLoadData(mKeyword, false);
            }
        }
    };

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 先隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                mKeyword = mSearch.getText().toString();
                mPresenter.onLoadData(mKeyword, true);
            }
            return false;
        }
    };

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @Override
    public void showTip(String tip) {
        ToastUtil.showShort(this, tip);
    }

    @Override
    public void showTip(int resId) {
        ToastUtil.showShort(this, resId);
    }

    @Override
    public void addBrandList(List<Brand> list, boolean isRefresh) {
        if (null == list || list.size() == 0) {
            return;
        }
        mLoadingLayout.showContent();
        if (isRefresh) {
            List<Object> newList = new ArrayList<>();
            newList.addAll(list);
            mAdapter.setData(newList);
        } else {
            mAdapter.addData(list);
        }
    }

    @Override
    public void showEmpty() {
        mAdapter.clear();
        mLoadingLayout.showEmpty();
    }

    @Override
    public void onError() {
        if (mAdapter.isEmpty()) {
            mLoadingLayout.showError();
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
    public void setIsLoading(boolean isLoading) {
        mIsLoading = isLoading;
    }

    @Override
    public void showLoading() {
        mLoadingLayout.showLoading();
    }

    @Override
    public void shareTo(final List<File> list, final String desc) {
        if (list == null || list.size() == 0) {
            showTip(getString(R.string.no_pic));
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShareIntent = ShareUtil.BuildShareIntent(SearchActivity.this, list, desc);
                dismissProgressDialog();
                startActivity(mShareIntent);
            }
        });
    }

    @Override
    public void shareFail() {
        runOnUiThread(new Runnable() {
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

    private void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void setUpShotView(final List<String> list, final List<Integer> params, final String desc, final String fileName) {
        runOnUiThread(new Runnable() {
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
                                mScreenWidth = DeviceUtil.getScreenWidthPixel(SearchActivity.this);
                                int height = (int) (((float) params.get(1)) / ((float) params.get(0)) * mScreenWidth * 0.75f);
                                lp.width = mScreenWidth;
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
                                GlideApp.with(SearchActivity.this).load(list.get(0)).centerCrop().into(mIv1);
                                GlideApp.with(SearchActivity.this).load(list.get(1)).centerCrop().into(mIv2);
                                GlideApp.with(SearchActivity.this).load(list.get(2)).centerCrop().into(mIv3);
                                GlideApp.with(SearchActivity.this).load(list.get(3)).centerCrop().into(mIv4);
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
        AlertDialog.Builder shareDialog = DialogUtil.showShareDialog(this);
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
        new AlertDialog.Builder(this)
                .setMessage("只有代理用户才能分享")
                .setPositiveButton("申请代理", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!checkIsApplyed()) {
                            SearchActivity.this.startActivity(RegisterActivity.createIntent(SearchActivity.this));
                        }
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

    private boolean checkIsApplyed() {
        if (PreferencesFactory.getUserPref().getUserType() == UserInfo.TYPE_APPLY_ING_INT) {
            ToastUtil.showShort(this, "您已经申请过代理，请等待后台处理");
            mPresenter.refreshUserInfo();
            return true;
        }
        return false;
    }

    @Override
    public void onRetry() {
        mPresenter.onLoadData(mKeyword, false);
    }

    @Override
    public void onEmpty() {
        mLoadingLayout.showEmpty();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
