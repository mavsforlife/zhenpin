package com.cang.zhenpin.zhenpincang.ui.orderlist;

import com.cang.zhenpin.zhenpincang.model.Order;
import com.cang.zhenpin.zhenpincang.model.OrderModel;
import com.cang.zhenpin.zhenpincang.model.OrderParent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 2018/3/12.
 * Email: wwmdirk@gmail.com
 */

public class OrderListUtils {

    public static List<Object> transformOrder(OrderModel orderModel) {

        List<Object> list = new ArrayList<>();
        if (orderModel == null || orderModel.getOrderParents() == null) {
            return list;
        }

        List<OrderParent> orderParents = orderModel.getOrderParents();

        for (OrderParent orderParent : orderParents) {

            if (orderParent.getOrderList() != null && orderParent.getOrderList().size() > 0) {

                list.add(orderParent.getOrderNO());

                OrderProxy orderProxy = new OrderProxy();
                orderProxy.mId = orderParent.getId();
                orderProxy.mStatus = orderParent.getStatus();
                orderProxy.mStatusName = orderParent.getStatusName();
                orderProxy.mTotalFee = orderParent.getTotalFee();
//                orderProxy.mTotlaCount = orderParent.getGoodsCount();
                orderProxy.mTotlaCount = orderParent.getOrderList().size();
                orderProxy.mPics = new ArrayList<>();

                for (Order order : orderParent.getOrderList()) {
                    list.add(order);
                    orderProxy.mPics.add(order.getPicPath());

                }

                list.add(orderProxy);
            }
        }

        return list;
    }
}
