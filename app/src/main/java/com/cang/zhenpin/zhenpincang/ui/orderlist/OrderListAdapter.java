package com.cang.zhenpin.zhenpincang.ui.orderlist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.Order;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.cart.util.DecimalUtil;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ORDER_HEADER = 1;
    private static final int TYPE_ORDER_ITEM = 2;
    private static final int TYPE_ORDER_FOOTER = 3;
    private static final int TYPE_FOOTER = 4;

    private Context mContext;
    private List<Object> mDatas;

    private OrderListListener mListener;
    private boolean mHasMoreData;
    private Footer mFooter;
    private LayoutInflater mLayoutInflater;

    OrderListAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ORDER_HEADER) {
            return new OrderHeader(mLayoutInflater.inflate(R.layout.item_order_header, parent, false));
        } else if (viewType == TYPE_ORDER_ITEM) {
            return new ItemViewHolder(mLayoutInflater.inflate(R.layout.item_order_item, parent, false));
        } else if (viewType == TYPE_ORDER_FOOTER) {
            return new OrderFooter(mLayoutInflater.inflate(R.layout.item_order_footer, parent, false));
        } else {
            mFooter = new Footer(mLayoutInflater.inflate(R.layout.item_image_grid_footer, parent, false));
            return mFooter;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderHeader) {
            bindOrderHeader((OrderHeader) holder);
        } else if (holder instanceof ItemViewHolder) {
            bindOrderItem((ItemViewHolder) holder);
        } else if (holder instanceof OrderFooter) {
            bindOrderFooter((OrderFooter) holder);
        }
    }

    private void bindOrderHeader(OrderHeader holder) {
        Object obj = mDatas.get(holder.getAdapterPosition());
        if (obj instanceof String) {
            holder.mOrderNo.setText(String.format(Locale.US, mContext.getString(R.string.order_no), obj.toString()));
        }
    }

    private void bindOrderItem(ItemViewHolder holder) {
        Object object = mDatas.get(holder.getAdapterPosition());
        if (object instanceof Order) {
            Order order = (Order) object;

            GlideApp.with(mContext)
                    .load(order.getPicPath())
                    .error(R.color.grayLighter)
                    .into(holder.mPic);
            holder.mContent.setText(order.getName());
            String price = String.format(Locale.US, mContext.getString(R.string.pay_price),
                    DecimalUtil.formatMoney(String.valueOf(order.getPrice())));
            SpannableString ss = new SpannableString(price);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.red)),
                    4, price.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.mPrice.setText(ss);
            holder.mSize.setText(String.format(Locale.US, mContext.getString(R.string.size), order.getAttrName()));
        }
    }

    private void bindOrderFooter(final OrderFooter holder) {
        Object obj = mDatas.get(holder.getAdapterPosition());
        if (obj instanceof OrderProxy) {
            final OrderProxy orderProxy = (OrderProxy) obj;

            String formatFee = DecimalUtil.formatMoney(String.valueOf(orderProxy.mTotalFee));
            String info = String.format(Locale.US, mContext.getString(R.string.order_info),
                    orderProxy.mTotlaCount, formatFee);
            holder.mInfo.setText(info);

            int status = orderProxy.mStatus;
            if (status == 1) {
                holder.mCancel.setText(R.string.cancel_order);
                holder.mCancel.setTag(orderProxy);
                final int totalCount = orderProxy.mTotlaCount + 2;
                holder.mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int pos = holder.getAdapterPosition();
                        NetWork.getsBaseApi()
                                .delOrder(PreferencesFactory.getUserPref().getId(), orderProxy.mId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new BaseActivityObserver<BaseResult>(mContext) {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        DialogUtil.showProgressDialog(mContext, R.string.please_wait);
                                    }

                                    @Override
                                    public void onNext(BaseResult baseResult) {
                                        super.onNext(baseResult);
                                        DialogUtil.dismissProgressDialog();
                                        List<Object> tempList = new ArrayList<>();
                                        for (int i = pos;
                                             i >  pos - totalCount;
                                             i--) {
                                            tempList.add(mDatas.get(i));
                                        }

                                        removeList(tempList, pos - totalCount + 1,
                                                totalCount);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        DialogUtil.dismissProgressDialog();
                                        super.onError(e);
                                    }
                                });

                    }
                });

                holder.mPay.setVisibility(View.VISIBLE);
                holder.mPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                holder.mCancel.setText(orderProxy.mStatusName);
                holder.mCancel.setOnClickListener(null);
                holder.mPay.setVisibility(View.GONE);
            }
        }
    }

    public void setData(List<Object> list) {
        if (list == null) return;
        mDatas = list;
        notifyDataSetChanged();
    }

    void addData(List<Object> list) {
        if (list == null) return;
        int lastPos = mDatas.size();
        mDatas.addAll(list);
        notifyItemRangeInserted(lastPos, list.size());
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    private void removeList(List<Object> list, int fromIndex, int toIndex) {
        mDatas.removeAll(list);
        if (mDatas.size() == 0) {
            mListener.onEmpty();
        } else {
            notifyItemRangeRemoved(fromIndex, toIndex);
        }
    }

    @Override
    public int getItemCount() {
        return (mDatas == null || mDatas.size() == 0) ? 0 : mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDatas.size()) {
            return TYPE_FOOTER;
        } else if (mDatas.get(position) instanceof String) {
            return TYPE_ORDER_HEADER;
        } else if (mDatas.get(position) instanceof Order) {
            return TYPE_ORDER_ITEM;
        } else {
            return TYPE_ORDER_FOOTER;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView mContent, mSize, mPrice;
        ImageView mPic;

        ItemViewHolder(View item) {
            super(item);
            mContent = item.findViewById(R.id.content);
            mSize = item.findViewById(R.id.size);
            mPrice = item.findViewById(R.id.pay_price);

            mPic = item.findViewById(R.id.pic);
        }
    }

    static class OrderHeader extends RecyclerView.ViewHolder {

        TextView mOrderNo;

        OrderHeader(View itemView) {
            super(itemView);
            mOrderNo = itemView.findViewById(R.id.order_no);
        }
    }

    static class OrderFooter extends RecyclerView.ViewHolder {

        TextView mInfo;
        TextView mCancel;
        TextView mPay;

        OrderFooter(View itemView) {
            super(itemView);
            mInfo = itemView.findViewById(R.id.info);
            mCancel = itemView.findViewById(R.id.order_cancel);
            mPay = itemView.findViewById(R.id.order_buy);
        }
    }

    class Footer extends RecyclerView.ViewHolder {

        TextView mTvNoMore, mTvRetry;
        ProgressBar mProgressBar;

        Footer(View itemView) {
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

    void setOrderListListener(OrderListListener listener) {
        mListener = listener;
    }

    public void setHasMoreData(final boolean hasMoreData) {
        mHasMoreData = hasMoreData;
        if (mFooter != null) {
            mFooter.mTvRetry.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFooter.mTvNoMore.setVisibility(hasMoreData ? View.GONE : View.VISIBLE);
                    mFooter.mProgressBar.setVisibility(hasMoreData ? View.VISIBLE : View.GONE);
                    mFooter.mTvRetry.setVisibility(View.GONE);
                }
            }, 500);
        }
    }

    public void showFooterError() {
        if (mFooter != null) {
            mFooter.mTvNoMore.setVisibility(View.GONE);
            mFooter.mProgressBar.setVisibility(View.GONE);
            mFooter.mTvRetry.setVisibility(View.VISIBLE);
        }
    }

    public interface OrderListListener {

        void onRetry();

        void onEmpty();
    }

}
