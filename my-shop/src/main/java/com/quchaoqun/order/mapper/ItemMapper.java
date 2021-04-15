package com.quchaoqun.order.mapper;

import com.quchaoqun.order.domain.Item;

import java.util.List;

public interface ItemMapper {
    List<Item> selectByOid(String oid);

    int add(Item item);
}
