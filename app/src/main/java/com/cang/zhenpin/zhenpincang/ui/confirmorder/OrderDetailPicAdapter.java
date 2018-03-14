package com.cang.zhenpin.zhenpincang.ui.confirmorder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.glide.GlideApp;

import java.util.List;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class OrderDetailPicAdapter extends RecyclerView.Adapter<OrderDetailPicAdapter.ViewHolder> {

    private List<String> mList;
    private Context mContext;

    public OrderDetailPicAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
//        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = mList.get(holder.getAdapterPosition());
        GlideApp.with(mContext)
                .load(path)
                .into(holder.mIv);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIv;

        public ViewHolder(View item) {
            super(item);
            mIv = item.findViewById(R.id.pic);
        }
    }
}
