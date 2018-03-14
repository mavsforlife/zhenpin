package com.cang.zhenpin.zhenpincang.ui.addresslist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cang.zhenpin.zhenpincang.R;
import com.cang.zhenpin.zhenpincang.model.Address;
import com.cang.zhenpin.zhenpincang.model.BaseResult;
import com.cang.zhenpin.zhenpincang.network.BaseActivityObserver;
import com.cang.zhenpin.zhenpincang.network.NetWork;
import com.cang.zhenpin.zhenpincang.pref.PreferencesFactory;
import com.cang.zhenpin.zhenpincang.ui.newaddress.NewAddressActivity;
import com.cang.zhenpin.zhenpincang.util.DialogUtil;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private Context mContext;
    private List<Address> mList;

    public AddressListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_address_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Address address = mList.get(holder.getAdapterPosition());
        holder.mName.setText(address.getName());
        holder.mPhone.setText(address.getMobile());
        holder.mAddress.setText(address.getAddress());
        if (address.getISDefault() == 0) {
            holder.mDefault.setText(R.string.set_default);
            holder.mDefault.setSelected(false);
        } else {
            holder.mDefault.setText(R.string.default_address);
            holder.mDefault.setSelected(true);
        }
        holder.mDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getISDefault() != 0) {
                    return;
                }
                setDefault(address, holder.getAdapterPosition());
            }
        });
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(NewAddressActivity.createIntent(mContext, address,
                        holder.getAdapterPosition(), NewAddressActivity.TYPE_MODIFY));
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(address.getID(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setList(List<Address> data) {
        mList = data;
        if (mList.get(0).getISDefault() == 0) {
            for (int i = 0; i < mList.size(); i++) {
                Address address = mList.get(i);
                if (address.getISDefault() != 0) {
                    Collections.swap(mList, 0, i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(int position, Address address) {
        mList.set(position, address);
        if (address.getISDefault() != 0) {
            mList.get(0).setISDefault(0);
            Collections.swap(mList, position, 0);
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mPhone;
        TextView mAddress;
        TextView mDefault;
        TextView mEdit;
        TextView mDelete;

        public ViewHolder(View item) {
            super(item);

            mName = item.findViewById(R.id.name);
            mPhone = item.findViewById(R.id.phone);
            mAddress = item.findViewById(R.id.address);
            mDefault = item.findViewById(R.id.setting);
            mEdit = item.findViewById(R.id.edit);
            mDelete = item.findViewById(R.id.delete);

        }
    }

    private void showConfirmDialog(final String id, final int position) {
        new AlertDialog.Builder(mContext)
                .setMessage(R.string.confirm_delete_address)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAddress(id, position);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteAddress(String id, final int position) {
        NetWork.getsBaseApi()
                .delAddress(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult>(mContext) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        DialogUtil.showProgressDialog(mContext, "请稍候");
                    }

                    @Override
                    public void onNext(BaseResult baseResult) {
                        super.onNext(baseResult);
                        DialogUtil.dismissProgressDialog();
                        mList.remove(position);
                        notifyItemRemoved(position);
                        if (mList.size() == 0 && mOnAddressChangeListener != null) {
                            mOnAddressChangeListener.onListEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtil.dismissProgressDialog();
                    }
                });
    }

    private void setDefault(Address address, final int position) {
        NetWork.getsBaseApi()
                .modifyOrAddAddress(PreferencesFactory.getUserPref().getId(),
                        address.getAddress(),
                        address.getMobile(),
                        address.getName(),
                        1,
                        address.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseActivityObserver<BaseResult<Address>>(mContext) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        DialogUtil.showProgressDialog(mContext, "请稍候");
                    }

                    @Override
                    public void onNext(BaseResult<Address> result) {
                        super.onNext(result);
                        DialogUtil.dismissProgressDialog();
                        mList.get(0).setISDefault(0);
                        mList.get(position).setISDefault(1);
                        Collections.swap(mList, position, 0);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtil.dismissProgressDialog();
                    }
                });
    }

    private OnAddressChangeListener mOnAddressChangeListener;

    public void setOnAddressChangeListener(OnAddressChangeListener listener) {
        mOnAddressChangeListener = listener;
    }

    public interface OnAddressChangeListener {

        void onListEmpty();
    }
}
