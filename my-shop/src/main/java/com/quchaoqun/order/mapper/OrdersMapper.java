package com.quchaoqun.order.mapper;

import com.quchaoqun.order.domain.Order;
import com.quchaoqun.order.domain.Orders;

import java.util.List;

public interface OrdersMapper {
    List<Orders> selectByUid(Integer uid);

    Orders selectByOid(String oid);

    int add(Orders orders);

    List<Order> getAllOrder();
}
