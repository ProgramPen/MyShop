package com.quchaoqun.order.service.impl;

import com.quchaoqun.order.domain.Item;
import com.quchaoqun.order.domain.Order;
import com.quchaoqun.order.domain.Orders;
import com.quchaoqun.order.mapper.ItemMapper;
import com.quchaoqun.order.mapper.OrdersMapper;
import com.quchaoqun.order.service.OrdersService;
import com.quchaoqun.shoppingcart.mapper.CartMapper;
import com.quchaoqun.shoppingcart.vo.CartsVo;
import com.quchaoqun.usermanager.domain.Address;
import com.quchaoqun.usermanager.mapper.AddressMapper;
import com.quchaoqun.usermanager.mapper.UserMapper;
import com.quchaoqun.util.SqlSessionUtil;
import com.quchaoqun.workbeach.domain.Product;
import com.quchaoqun.workbeach.mapper.ProductMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersServiceImpl implements OrdersService {
    //获取订单dao对象
    private OrdersMapper ordersMapper = SqlSessionUtil.getSqlSession().getMapper(OrdersMapper.class);
    //获取ItemDao对象
    private ItemMapper itemMapper = SqlSessionUtil.getSqlSession().getMapper(ItemMapper.class);
    //获取产品dao对象
    private ProductMapper productMapper = SqlSessionUtil.getSqlSession().getMapper(ProductMapper.class);
    //获取购物车模块CartDAO对象
    private CartMapper cartMapper = SqlSessionUtil.getSqlSession().getMapper(CartMapper.class);
    //获取用户管理模块中地址DAO对象
    private AddressMapper addressMapper = SqlSessionUtil.getSqlSession().getMapper(AddressMapper.class);
    //获取用户管理模块的UserDao对象
    private UserMapper userMapper = SqlSessionUtil.getSqlSession().getMapper(UserMapper.class);

    @Override
    public List<Orders> selectByUid(Integer uid) {
        List<Orders> ordersList = ordersMapper.selectByUid(uid);
        return ordersList;
    }

    @Override
    public Map<String,Object> selectByOid(String oid) {
        //根据订单生成pid查询产品信息
        List<Item> items = itemMapper.selectByOid(oid);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(Item item : items){
            Product product = productMapper.findDetailByPid(item.getPid());
            Map<String, Object> map = new HashMap<>();
            map.put("item",item);
            map.put("product",product);
            mapList.add(map);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list",mapList);
        Orders orders = ordersMapper.selectByOid(oid);
        map.put("order",orders);

        return map;
    }

    @Override
    public Map<String,Object> selectCartByUid(Integer uid) {
        List<CartsVo> cartsVoList = cartMapper.selectCartByUid(uid);
        List<Address> addresses = addressMapper.selectAddress(uid);
        Map<String,Object> map = new HashMap<>();
        map.put("cartList",cartsVoList);
        map.put("addressList",addresses);
        return map;
    }

    @Override
    public boolean add(Orders orders) {
        //根据uid获取购物车对象信息
        List<CartsVo> cartsVoList = cartMapper.selectCartByUid(orders.getUid());
        //根据uid删除购物车对象信息
        cartMapper.clearByUid(orders.getUid());
        if(cartsVoList.size() ==0){
            return false;
        }else {
            //添加订单信息
            int count1 = ordersMapper.add(orders);
            int count2 =0;
            //获取购物车的信息，并向item表新添数据
            for(CartsVo cartsVo : cartsVoList){
                Item item = new Item();
                item.setOid(orders.getOid());
                item.setPid(cartsVo.getPid());
                item.setCount((int)cartsVo.getCcount());
                item.setNum(cartsVo.getCnum());
                count2 += itemMapper.add(item);
            }
            if(count1 ==1 && count2 != 0){
                return true;
            }
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAllOrder() {
        //获取所有的订单对象
        List<Map<String, Object>> mapList = new ArrayList<>();

        List<Order> ordersList = ordersMapper.getAllOrder();

        for(Order order : ordersList){
            Map<String,Object> map = new HashMap<>();
            map.put("order",order);
            map.put("username",userMapper.getUserName(order.getUid()));
            mapList.add(map);
        }
        return mapList;
    }
}
