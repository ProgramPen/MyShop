package com.quchaoqun.shoppingcart.service;


import java.util.Map;

public interface CartService {
    Map<String,Object> addCart(int uid, int pid, double price);

    Map<String, Object> selectByUid(Integer uid);

    Map<String, Object> update(Integer uid, Integer pid, Integer cnum, Double price);

    Map<String, Object> delete(Integer cid,Integer uid,Integer pid);

    void clearByUid(Integer uid);
}
