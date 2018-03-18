package com.cang.zhenpin.zhenpincang.ui.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.model.CartBrand;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.ui.cart.util.DecimalUtil;
import com.cang.zhenpin.zhenpincang.ui.cart.widget.AddReduceView;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;
import com.cang.zhenpin.zhenpincang.util.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/3/10.
 * Email: wwmdirk@gmail.com
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    public static final int STATUS_BUY = 0;
    public static final int STATUS_DEL = 1;

    private Context mContext;
    private List<CartBrand> mList;
    private int mStatus;

    public ShoppingCartAdapter(Context context, int status) {
        mContext = context;
        mStatus = status;
    }

    void setData(List<CartBrand> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CartBrand cartBrand = mList.get(holder.getAdapterPosition());
        if (mStatus == STATUS_BUY) {
            holder.mCheckBox.setChecked(cartBrand.isChecked());
        } else {
            holder.mCheckBox.setChecked(cartBrand.isDelChecked());
        }
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus == STATUS_BUY) {
                    cartBrand.setChecked(!cartBrand.isChecked());

                } else {
                    cartBrand.setDelChecked(!cartBrand.isDelChecked());
                }
                if(mUpdatePriceListener != null) {
                    mUpdatePriceListener.updatePrice(mList);
                }
            }
        });
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        GlideApp.with(mContext)
                .load(cartBrand.getMPicPath())
                .into(holder.mPic);
        holder.mContent.setText(cartBrand.getMVDesc());
        String price = DecimalUtil.formatMoney(String.valueOf(cartBrand.getMPrice()));
        holder.mCharge.setText(String.format(Locale.US, mContext.getString(R.string.charge_cart_item), price));
        holder.mSize.setText(String.format(Locale.US, mContext.getString(R.string.size_cart_item), cartBrand.getMAttrName()));
        holder.mAddReduceView.setVisibility(mStatus == STATUS_BUY ?
                View.VISIBLE :
                View.GONE);
        holder.mAddReduceView.setCount(cartBrand.getMQuantity(), Integer.parseInt(cartBrand.getMTotalQuantity()));
        holder.mAddReduceView.setOnCountChangeListener(new AddReduceView.OnCountChangeListener() {
            @Override
            public void onCountChange(int count) {
                if (cartBrand.getMQuantity() != count) {
                    modifyQuantity(count, cartBrand, holder.mAddReduceView);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (null == payloads || payloads.size() < 1) {
            onBindViewHolder(holder, position);
        } else {
            if (payloads.get(0) instanceof Boolean && (Boolean) payloads.get(0)) {
                if (holder != null) {

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void isCheckAll(boolean checked) {
        for (CartBrand cartBrands : mList) {
            if (mStatus == STATUS_BUY) {
                cartBrands.setChecked(checked);
            } else {
                cartBrands.setDelChecked(checked);
            }
        }
        notifyDataSetChanged();
        if(mUpdatePriceListener != null) {
            mUpdatePriceListener.updatePrice(mList);
        }
    }

    public void delData(List<CartBrand> list) {
        mList.removeAll(list);
        notifyDataSetChanged();
        if(mUpdatePriceListener != null) {
            mUpdatePriceListener.updatePrice(mList);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        ImageView mPic;
        TextView mContent;
        TextView mSize;
        TextView mCharge;
        AddReduceView mAddReduceView;

        ViewHolder(View item) {
            super(item);
            mCheckBox = item.findViewById(R.id.checkbox);
            mPic = item.findViewById(R.id.pic);
            mContent = item.findViewById(R.id.content);
            mSize = item.findViewById(R.id.size);
            mCharge = item.findViewById(R.id.charge);
            mAddReduceView = item.findViewById(R.id.add_reduce);
        }
    }

    void setStatus(int status) {
        mStatus = status;
        notifyDataSetChanged();
        if(mUpdatePriceListener != null) {
            mUpdatePriceListener.updatePrice(mList);
        }
    }

    int getStatus() {
        return mStatus;
    }

    List<CartBrand> getData() {
        return mList;
    }

    private void modifyQuantity(final int quantity, final CartBrand cartBrand, final AddReduceView addReduceView) {
        DialogUtil.showProgressDialog(new WeakReference<>(mContext), R.string.please_wait);
        NetWork.getsBaseApi()
                .modifyShoppingCartItem(cartBrand.getMId(), quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<String>>(mContext) {
                    @Override
                    public void onNext(BaseResult<String> result) {
                        super.onNext(result);
                        DialogUtil.dismissProgressDialog();
                        ToastUtil.showShort(mContext, result.getMsg());
                        cartBrand.setMQuantity(quantity);
                        addReduceView.setCount(quantity, Integer.parseInt(cartBrand.getMTotalQuantity()));
                        if (mUpdatePriceListener != null) {
                            mUpdatePriceListener.updatePrice(mList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtil.dismissProgressDialog();
                    }
                });
    }

    public void setUpdatePriceListener(updatePriceListener listener) {
        mUpdatePriceListener = listener;
    }

    private updatePriceListener mUpdatePriceListener;

    interface updatePriceListener {
        void updatePrice(List<CartBrand> list);
    }
}
