package com.quchaoqun.shoppingcart.service.impl;

import com.quchaoqun.shoppingcart.mapper.CartMapper;
import com.quchaoqun.shoppingcart.service.CartService;
import com.quchaoqun.shoppingcart.vo.CartsVo;
import com.quchaoqun.util.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartServiceImpl implements CartService {
    //获取CartMapper对象
    private CartMapper cartMapper = SqlSessionUtil.getSqlSession().getMapper(CartMapper.class);
    @Override
    public Map<String,Object> addCart(int uid , int pid , double price) {
        //先根据用户id查询购物车中是否已经存在商品，有的话在原来基础上加一，没有就新添数据
        int count = cartMapper.selectCart(uid,pid);
        int result = 0;
        if (count == 1){
            result = cartMapper.updateCart(uid,pid,price);
        }else {
            result = cartMapper.addCart(uid,pid,price);
        }

        //查询用户商品信息
        List<CartsVo> carts = cartMapper.selectCartByUid(uid);
        String flag = (result == 1 ? "加入成功":"加入失败");
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("list",carts);
        return map;
    }

    @Override
    public Map<String, Object> selectByUid(Integer uid) {
        List<CartsVo> cartsVos = cartMapper.selectCartByUid(uid);
        Map<String,Object> map = new HashMap<>();
        map.put("list",cartsVos);
        return map;
    }

    @Override
    public Map<String, Object> update(Integer uid, Integer pid, Integer cnum, Double price) {
        int count = cartMapper.update(uid,pid,cnum,price);

        List<CartsVo> cartsVos = cartMapper.selectCartByUid(uid);
        Map<String,Object> map = new HashMap<>();
        map.put("list",cartsVos);
        return map;
    }

    @Override
    public Map<String, Object> delete(Integer cid,Integer uid,Integer pid) {
        int count = cartMapper.delete(cid);
        boolean flag = false;
        Map<String,Object> map = new HashMap<>();
        if(count ==1){
            flag = true;
        }
        List<CartsVo> cartsVos = cartMapper.selectCartByUid(uid);
        map.put("success",flag);
        map.put("list",cartsVos);
        return map;
    }

    @Override
    public void clearByUid(Integer uid) {
        cartMapper.clearByUid(uid);
    }
}
