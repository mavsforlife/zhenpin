package com.cang.zhenpin.zhenpincang.ui.list;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.event.UpdateShopCartEvent;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.glide.GlideCircleTransform;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.Brand;
import com.cang.zhenpin.zhenpincang.model.BrandForFuck;
import com.cang.zhenpin.zhenpincang.model.BrandForFuckList;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.brand.BrandActivity;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ShareUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;
import com.cang.zhenpin.zhenpincang.widget.EllipsizingTextView;
import com.cang.zhenpin.zhenpincang.widget.ninegridlayout.ImgGridView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2017/11/28.
 * Email: wwmdirk@gmail.com
 */

public class GoodsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_BRAND_FUCK = 2;
    private static final int TYPE_BRAND_FUCK_Line = 3;
    private Context mContext;
    private List<Object> mList;
    private ShareListener mListener;
    private LayoutInflater mLayoutInflater;
    private FooterHolder mFooterHolder;
    private boolean mHasMoreData = true;
    private boolean mIsAttention;
    private boolean mIsBrand;

    public GoodsListAdapter(Context context, ShareListener listener, boolean isAttention, boolean isBrand) {
        mContext = context;
        mListener = listener;
        mList = new ArrayList<>();
        mIsAttention = isAttention;
        mIsBrand = isBrand;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    boolean isAttention() {
        return mIsAttention;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mList == null || mList.size() == 0;
    }

    public void setData(List<Object> list) {
        if (list == null) return;
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<Brand> list) {
        if (list == null) return;
        int lastPos = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(lastPos, list.size());
    }

    public void addFuckBrand(BrandForFuckList fuck) {
        if (fuck == null || fuck.mList == null) {
            return;
        }
        mList.add(1, fuck);
        notifyItemInserted(1);
    }

    void removeItem(int position) {
        if (position < 0 || position >= mList.size()) {
            return;
        }
        if (mList.size() > 1) {
            mList.remove(position);
            notifyItemRemoved(position);
        } else {
            mListener.onEmpty();
            clear();
        }
    }

    public void setAttention(int position) {
        if (position < 0 || position >= mList.size()) {
            return;
        }
        if (mList.get(position) instanceof Brand) {
            Brand brand = (Brand) mList.get(position);
            brand.setIsAttention(brand.getIsAttention() == Brand.ATTENTION ?
                    Brand.NOT_ATTENTION : Brand.ATTENTION);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_brand, parent, false));
        } else if (viewType == TYPE_BRAND_FUCK) {
            return new FuckHolder(mLayoutInflater.inflate(R.layout.item_fuck_brand, parent, false));
        } else if (viewType == TYPE_BRAND_FUCK_Line) {
            return new FuckLine(mLayoutInflater.inflate(R.layout.item_fuck_brand_line, parent, false));
        } else {
            mFooterHolder = new FooterHolder(mLayoutInflater.inflate(R.layout.item_image_grid_footer, parent, false));
            return mFooterHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            if (mList.get(holder.getAdapterPosition()) instanceof Brand) {
                final Brand brand = (Brand) mList.get(holder.getAdapterPosition());
                final ViewHolder h = (ViewHolder) holder;
                h.mTvTitle.setText(brand.getBrandName());
                h.mTvMsg.setText(brand.getDesc().trim(), brand.isCollapsed());
                h.mTvMsg.setListener(new EllipsizingTextView.OnExpandStateChangeListener() {
                    @Override
                    public void onExpandStateChanged(boolean isExpanded) {
                        brand.setCollapsed(isExpanded);
                    }
                });
                if (brand.getPicPath() == null || brand.getPicPath().size() == 0) {
                    h.mImageNineGridView.setVisibility(View.GONE);
                    h.mTvShare.setVisibility(View.GONE);
                } else {
                    h.mImageNineGridView.setVisibility(View.VISIBLE);
                    h.mImageNineGridView.render(brand.getPicPath());
                    h.mTvShare.setVisibility(View.VISIBLE);
                }
                h.mTvShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onShare(brand);
                    }
                });
                h.mIvAttention.setSelected(brand.getIsAttention() == Brand.ATTENTION);
                h.mIvAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AttentionHelper.Attention(GoodsListAdapter.this, brand, h.getAdapterPosition(), mContext);
                    }
                });
                GlideApp.with(mContext)
                        .load(brand.getBrandPic())
                        .centerCrop()
                        .error(R.color.grayLighter)
                        .placeholder(R.color.grayLighter)
                        .into(h.mIvAvatar);
                if (!mIsBrand) {
                    h.mIvAvatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(BrandActivity.createIntent(mContext,
                                    brand.getBrandName(),
                                    brand.getBrandId()));
                        }
                    });
                    h.mTvTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mContext.startActivity(BrandActivity.createIntent(mContext,
                                    brand.getBrandName(),
                                    brand.getBrandId()));
                        }
                    });
                }
                if (brand.getAttribute() != null && brand.getAttribute().size() > 0 && brand.getIsBuy() == 1) {
                    h.mBuyLayout.setVisibility(View.VISIBLE);
                    h.mTags.setAdapter(new TagAdapter<Brand.Attribute>(brand.getAttribute()) {
                        @Override
                        public View getView(FlowLayout parent, int position, Brand.Attribute attribute) {
                            TextView tv = (TextView) mLayoutInflater.inflate(R.layout.item_tag, parent, false);
                            tv.setText(brand.getAttribute().get(position).getMName());
                            tv.setEnabled(brand.getAttribute().get(position).getMQuantity() > 0);
                            return tv;
                        }
                    });
                    final int[] checkPosition = {-1};
                    h.mTags.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            h.mBuy.setTag(brand.getAttribute().get(position));
                            checkPosition[0] = position;
                            return true;
                        }
                    });
                    h.mBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!ShareUtil.isShareEnabled()) {
                                ToastUtil.showShort(mContext, R.string.apply_agency_to_buy);
                                if (checkPosition[0] >= 0 && checkPosition[0] < h.mTags.getChildCount()) {
                                    ((TagView) h.mTags.getChildAt(checkPosition[0])).setChecked(false);
                                }
                                return;
                            }

                            final Brand.Attribute attr = (Brand.Attribute) v.getTag();
                            if (attr == null) {
                                ToastUtil.showShort(mContext, R.string.please_select_size);
                            } else {
                                if (attr.getMQuantity() == 0) {
                                    ToastUtil.showShort(mContext, R.string.attr_sold_out);
                                } else {
                                    showTipDialog(attr, checkPosition, h);
                                }
                            }
                        }
                    });

                } else {
                    h.mBuyLayout.setVisibility(View.GONE);
                }
            }
        } else if (holder instanceof FuckHolder) {
            if (mList.get(holder.getAdapterPosition()) instanceof BrandForFuckList) {
                BrandForFuckList fuck = (BrandForFuckList) mList.get(holder.getAdapterPosition());
                ((FuckHolder) holder).setFuck(fuck);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<
            Object> payloads) {
        if (null == payloads || payloads.size() < 1) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) instanceof Boolean && (Boolean) payloads.get(0)) {
                if (holder instanceof ViewHolder) {
                    if (mList.get(holder.getAdapterPosition()) instanceof Brand) {
                        final Brand brand = (Brand) mList.get(holder.getAdapterPosition());
                        ViewHolder h = (ViewHolder) holder;
                        h.mIvAttention.setSelected(brand.getIsAttention() == Brand.ATTENTION);
                        showAttentionAnimator(h.mIvAttention);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 0 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mList.size()) {
            if (mList.get(position) instanceof Brand) {
                return TYPE_ITEM;
            } else if (mList.get(position) instanceof BrandForFuckList) {
                return TYPE_BRAND_FUCK;
            } else {
                return TYPE_BRAND_FUCK_Line;
            }
        } else {
            return TYPE_FOOTER;
        }
    }

    public void setHasMoreData(final boolean hasMoreData) {
        mHasMoreData = hasMoreData;
        if (mFooterHolder != null) {
            mFooterHolder.mTvRetry.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFooterHolder.mTvNoMore.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
                    mFooterHolder.mProgressBar.setVisibility(hasMoreData ? View.VISIBLE : View.GONE);
                    mFooterHolder.mTvRetry.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    public void showFooterError() {
        if (mFooterHolder != null) {
            mFooterHolder.mTvNoMore.setVisibility(View.GONE);
            mFooterHolder.mProgressBar.setVisibility(View.GONE);
            mFooterHolder.mTvRetry.setVisibility(View.VISIBLE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvTitle, mTvShare;
        private EllipsizingTextView mTvMsg;
        private ImageView mIvAvatar, mIvAttention;
        private ImgGridView mImageNineGridView;
        private LinearLayout mBuyLayout;
        private TextView mBuy;
        private TagFlowLayout mTags;

        ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvMsg = itemView.findViewById(R.id.tv_content);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mImageNineGridView = itemView.findViewById(R.id.img_view);
            mTvShare = itemView.findViewById(R.id.share);
            mIvAttention = itemView.findViewById(R.id.iv_attention);
            mTags = itemView.findViewById(R.id.tag);
            mBuy = itemView.findViewById(R.id.tv_buy);
            mBuyLayout = itemView.findViewById(R.id.layout_buy);
        }
    }

    static class FuckLine extends RecyclerView.ViewHolder {

        public FuckLine(View itemView) {
            super(itemView);
        }
    }

    static class FuckHolder extends RecyclerView.ViewHolder {

        RecyclerView mRecyclerView;
        BrandAdapter mAdapter;

        public FuckHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mAdapter = new BrandAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }

        public void setFuck(BrandForFuckList fuck) {
            if (fuck == null || fuck.mList == null) {
                return;
            }
            mAdapter.setList(fuck.mList);
        }
    }

    static class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.FuckHolder> {

        private List<BrandForFuck> mList;

        public BrandAdapter() {

        }

        public void setList(List<BrandForFuck> list) {
            mList = list;
            notifyDataSetChanged();
        }

        public BrandAdapter(List<BrandForFuck> list) {
            mList = list;
        }

        @Override
        public BrandAdapter.FuckHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FuckHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_list_item,
                    parent, false));
        }

        @Override
        public void onBindViewHolder(BrandAdapter.FuckHolder holder, int position) {
            final BrandForFuck fuck = mList.get(holder.getAdapterPosition());
            holder.mName.setText(fuck.mDesc);
            GlideApp.with(holder.mPic)
                    .load(fuck.mPic)
                    .transform(new GlideCircleTransform())
                    .into(holder.mPic);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(BrandActivity.createIntent(v.getContext(),
                            fuck.mDesc,
                            fuck.mId));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class FuckHolder extends RecyclerView.ViewHolder {

            TextView mName;
            ImageView mPic;

            public FuckHolder(View itemView) {
                super(itemView);
                mName = itemView.findViewById(R.id.name);
                mPic = itemView.findViewById(R.id.pic);
            }
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        TextView mTvNoMore, mTvRetry;
        ProgressBar mProgressBar;

        FooterHolder(View itemView) {
            super(itemView);
            mTvNoMore = itemView.findViewById(R.id.tv_no_more);
            mProgressBar = itemView.findViewById(R.id.progress_bar);
            mTvRetry = itemView.findViewById(R.id.tv_re_try);

            mTvNoMore.setVisibility(mHasMoreData ? View.GONE : View.VISIBLE);
            mProgressBar.setVisibility(mHasMoreData ? View.VISIBLE : View.GONE);
            mTvRetry.setVisibility(View.GONE);

            mTvRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTvRetry.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    mListener.onRetry();
                }
            });
        }
    }

    private void showAttentionAnimator(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation animationEnlarge = new ScaleAnimation(1.0f, 1.05f, 1.0f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationEnlarge.setDuration(100);
        animationSet.addAnimation(animationEnlarge);

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.8f);
        fadeOut.setDuration(100);
        animationSet.addAnimation(fadeOut);

        AlphaAnimation fadeIn = new AlphaAnimation(0.8f, 1.0f);
        fadeIn.setDuration(100);
        fadeIn.setStartOffset(100);
        animationSet.addAnimation(fadeIn);

        ScaleAnimation animationNarrow = new ScaleAnimation(1.05f, 1.0f, 1.05f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animationNarrow.setDuration(100);
        animationNarrow.setStartOffset(100);
        animationSet.addAnimation(animationNarrow);
        view.startAnimation(animationSet);

    }

    public interface ShareListener {

        void onShare(Brand brand);

        void onRetry();

        void onEmpty();
    }

    private void showTipDialog(final Brand.Attribute attr, final int[] positions, final ViewHolder h) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_confirm_add_to_shopping_cart);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView mCancel = dialog.findViewById(R.id.tv_cancel);
        final TextView mConfirm = dialog.findViewById(R.id.tv_confirm);
        final EditText mEt = dialog.findViewById(R.id.et_tip);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEt.getText())) {
                    ToastUtil.showShort(mContext, "请输入备注");
                } else {
                    addToShoppingCart(attr, positions, h, mEt.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void addToShoppingCart(final Brand.Attribute attr, final int[] positions, final ViewHolder h, String tip) {
        NetWork.getsBaseApi()
                .addToShoppingCart(PreferencesFactory.getUserPref().getId(),
                        attr.getMID(), 1, tip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult>(mContext) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        DialogUtil.showProgressDialog(new WeakReference<>(mContext), R.string.please_wait);
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        super.onNext(baseResult);
                        DialogUtil.dismissProgressDialog();
                        ToastUtil.showShort(mContext,
                                String.format(Locale.US, mContext.getString(R.string.get_into_cart), attr.getMName()));
                        int clearPosition = positions[0];
                        if (clearPosition >= 0 && clearPosition < h.mTags.getChildCount()) {
                            ((TagView) h.mTags.getChildAt(clearPosition)).setChecked(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtil.dismissProgressDialog();
                        int clearPosition = positions[0];
                        if (clearPosition >= 0 && clearPosition < h.mTags.getChildCount()) {
                            ((TagView) h.mTags.getChildAt(clearPosition)).setChecked(false);
                        }
                    }
                });
    }
}
