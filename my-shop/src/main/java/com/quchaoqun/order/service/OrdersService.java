package com.quchaoqun.order.service;

import com.quchaoqun.order.domain.Orders;

import java.util.List;
import java.util.Map;

public interface OrdersService {
    List<Orders> selectByUid(Integer uid);

    Map<String, Object> selectByOid(String oid);

    Map<String,Object> selectCartByUid(Integer uid);

    boolean add(Orders orders);

    List<Map<String, Object>> getAllOrder();
}
