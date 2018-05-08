package com.cang.zhenpin.zhenpincang.ui.orderdetail.model;

import com.cang.zhenpin.zhenpincang.model.OrderDetail;
import com.cang.zhenpin.zhenpincang.ui.orderdetail.OrderDetailContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2018/5/8.
 * Email: wwmdirk@gmail.com
 */

public class OrderDetailUtil {
    
    public static List<Object> transformOrderDetail(OrderDetail.Detail orderDetail) {
        if (null == orderDetail 
                || orderDetail.mList == null 
                || orderDetail.mList.isEmpty()) {
            return null;
        }
        
        List<Object> list = new ArrayList<>();

        OrderDetailHeader header = new OrderDetailHeader();
        header.mId = orderDetail.mId;
        header.mAddress = orderDetail.mAddress;
        header.mStatus = orderDetail.mStatus;
        header.mOrderNO = orderDetail.mOrderNO;
        header.mAddDate = orderDetail.mAddDate;
        header.mIsPay = orderDetail.mIsPay;
        header.mUId = orderDetail.mUId;
        header.mQuantity = orderDetail.mQuantity;
        header.mAId = orderDetail.mAId;
        header.mVId = orderDetail.mVId;
        header.mAttrName = orderDetail.mAttrName;
        header.mTotalQuantity = orderDetail.mTotalQuantity;
        header.mTypeClassID = orderDetail.mTypeClassID;
        header.mName = orderDetail.mName;
        header.mVDesc = orderDetail.mVDesc;
        header.mPicPath = orderDetail.mPicPath;
        header.mStartDate = orderDetail.mStartDate;
        header.mEndDate = orderDetail.mEndDate;
        header.mIsDelete = orderDetail.mIsDelete;
        header.mIsBuy = orderDetail.mIsBuy;
        header.mPrice = orderDetail.mPrice;
        header.mVCheck = orderDetail.mVCheck;
        header.mStatusName = orderDetail.mStatusName;
        list.add(header);

        list.addAll(orderDetail.mList);
        return list;
    }
}
