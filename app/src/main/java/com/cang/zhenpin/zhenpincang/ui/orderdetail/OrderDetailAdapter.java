package com.cang.zhenpin.zhenpincang.ui.orderdetail;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.model.OrderModel;
import com.cang.zhenpin.zhenpincang.ui.cart.util.DecimalUtil;
import com.cang.zhenpin.zhenpincang.ui.orderdetail.model.OrderDetailHeader;
import com.cang.zhenpin.zhenpincang.ui.orderdetail.model.OrderDetailModel;
import com.cang.zhenpin.zhenpincang.ui.orderlist.OrderHeaderProxy;

import org.w3c.dom.ProcessingInstruction;

import java.util.List;
import java.util.Locale;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_DETAIL_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_ITEM_HEADER = 2;

    private List<Object> mList;
    private Context mContext;

    public OrderDetailAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<Object> objects) {
        if (objects == null || objects.isEmpty()) {
            return;
        }
        mList = objects;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DETAIL_HEADER) {
            return new HeaderHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_header, parent, false));
        } else if (viewType == TYPE_ITEM) {
            return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_item, parent, false));
        } else {
            return new ItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            bindHeader((HeaderHolder) holder);
        } else if (holder instanceof ItemHolder) {
            bindItem((ItemHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof OrderDetailHeader) {
            return TYPE_DETAIL_HEADER;
        } else if (mList.get(position) instanceof OrderDetailModel) {
            return TYPE_ITEM;
        } else if (mList.get(position) instanceof String) {
            return TYPE_ITEM_HEADER;
        }
        return super.getItemViewType(position);
    }

    private void bindHeader(HeaderHolder holder) {
        final Object obj = mList.get(holder.getAdapterPosition());
        if (obj instanceof OrderDetailHeader) {
            OrderDetailHeader header = (OrderDetailHeader) obj;
            String orderNo = String.format(Locale.US, mContext.getString(R.string.order_no), header.mOrderNO);
            holder.mTvOrderNo.setText(orderNo);

            String date = String.format(Locale.US, mContext.getString(R.string.add_order_time), header.mAddDate);
            holder.mTvDate.setText(date);

            String price = String.format(Locale.US, mContext.getString(R.string.goods_price), header.mPrice);
            holder.mTvPrice.setText(price);

            String address = header.mAddress.replace("<br>", "\n");
            holder.mTvInfo.setText(address);

            holder.mTvStatus.setText(header.mStatusName);
        }
    }

    private void bindItem(ItemHolder holder) {
        final Object obj = mList.get(holder.getAdapterPosition());
        if (obj instanceof OrderDetailModel) {
            OrderDetailModel model = (OrderDetailModel) obj;

            if (model.mPicPath != null && !model.mPicPath.isEmpty()) {
                GlideApp.with(mContext)
                        .load(model.mPicPath.get(0))
                        .error(R.color.grayLighter)
                        .into(holder.mPic);
            }
            holder.mContent.setText(model.mName);
            String price = String.format(Locale.US, mContext.getString(R.string.pay_price),
                    DecimalUtil.formatMoney(String.valueOf(model.mPrice)));
            SpannableString ss = new SpannableString(price);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.red)),
                    4, price.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            holder.mPrice.setText(ss);
            holder.mSize.setText(String.format(Locale.US, mContext.getString(R.string.size), model.mAttrName));

            holder.mTvRemark.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(model.mRemarkStr)) {
                model.mRemarkStr = mContext.getString(R.string.remark_null);
            }
            holder.mTvRemark.setText(String.format(Locale.US, mContext.getString(R.string.remarks), model.mRemarkStr));
        }
    }

    static class ItemHeaderHolder extends RecyclerView.ViewHolder {

        TextView mOrderNo;

        ItemHeaderHolder(View itemView) {
            super(itemView);
            mOrderNo = itemView.findViewById(R.id.order_no);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        TextView mContent, mSize, mPrice;
        ImageView mPic;

        TextView mTvRemark;
        ItemHolder(View item) {
            super(item);
            mContent = item.findViewById(R.id.content);
            mSize = item.findViewById(R.id.size);
            mPrice = item.findViewById(R.id.pay_price);

            mPic = item.findViewById(R.id.pic);

            mTvRemark = itemView.findViewById(R.id.tv_remark);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {

        TextView mTvOrderNo;
        TextView mTvStatus;
        TextView mTvDate;
        TextView mTvInfo;
        TextView mTvPrice;

        HeaderHolder(View itemView) {
            super(itemView);

            mTvOrderNo = itemView.findViewById(R.id.tv_order_no);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mTvDate = itemView.findViewById(R.id.tv_add_date);
            mTvInfo = itemView.findViewById(R.id.tv_user_info);
            mTvPrice = itemView.findViewById(R.id.tv_order_price);
        }
    }
}
