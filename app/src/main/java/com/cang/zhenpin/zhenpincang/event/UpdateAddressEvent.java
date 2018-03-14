package com.cang.zhenpin.zhenpincang.event;

import com.cang.zhenpin.zhenpincang.model.Address;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class UpdateAddressEvent {

    public int mType;
    public int mPosition;
    public Address mAddress;

    public UpdateAddressEvent(int type, int position, Address address) {
        mType = type;
        mPosition = position;
        mAddress = address;
    }
}
