package com.cang.zhenpin.zhenpincang.event;

import com.cang.zhenpin.zhenpincang.model.Address;

import java.util.List;

/**
 * Created by victor on 2018/3/11.
 * Email: wwmdirk@gmail.com
 */

public class SaveAndUseAddressEvent {

    public List<Address> mAddress;

    public SaveAndUseAddressEvent(List<Address> address) {
        mAddress = address;
    }
}
