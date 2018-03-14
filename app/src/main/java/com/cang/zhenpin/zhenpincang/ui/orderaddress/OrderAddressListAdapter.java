package com.cang.zhenpin.zhenpincang.ui.orderaddress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.listener.OnRvItemClickListener;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.ui.newaddress.NewAddressActivity;

import java.util.List;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class OrderAddressListAdapter extends RecyclerView.Adapter<OrderAddressListAdapter.ViewHolder>{

    private Context mContext;
    private OnRvItemClickListener mOnRvItemClickListener;
    private List<Address> mList;

    public OrderAddressListAdapter(Context context, OnRvItemClickListener onRvItemClickListener) {
        mContext = context;
        mOnRvItemClickListener = onRvItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_order_address_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Address address = mList.get(holder.getAdapterPosition());
        holder.mName.setText(address.getName());
        holder.mPhone.setText(address.getMobile());
        holder.mAddress.setText(address.getAddress());
        if (address.isOrderAddress()) {
            holder.mDefault.setSelected(true);
        } else {
            holder.mDefault.setSelected(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mList.size(); i++) {
                    if (i == holder.getAdapterPosition()) {
                        mList.get(i).setOrderAddress(true);
                    } else {
                        mList.get(i).setOrderAddress(false);
                    }
                }
                notifyDataSetChanged();
                if (mOnRvItemClickListener != null) {
                    mOnRvItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(NewAddressActivity.createIntent(mContext, address,
                        holder.getAdapterPosition(), NewAddressActivity.TYPE_MODIFY_QUIT));
            }
        });
    }

    public void setList(List<Address> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mPhone;
        TextView mAddress;
        TextView mDefault;
        TextView mEdit;

        ViewHolder(View item) {
            super(item);

            mName = item.findViewById(R.id.name);
            mPhone = item.findViewById(R.id.phone);
            mAddress = item.findViewById(R.id.address);
            mDefault = item.findViewById(R.id.setting);
            mEdit = item.findViewById(R.id.edit);

        }
    }

}
