package com.quchaoqun.order.controller;

import com.quchaoqun.order.domain.Orders;
import com.quchaoqun.order.service.OrdersService;
import com.quchaoqun.order.service.impl.OrdersServiceImpl;
import com.quchaoqun.util.RandomUtils;
import com.quchaoqun.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class OrdersServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if("/order/selectByUid.do".equals(path)){
            selectByUid(req,resp);
        }else if("/order/selectByOid.do".equals(path)){
            selectByOid(req,resp);
        }else if("/order/selectCartByUid.do".equals(path)){
            selectCartByUid(req,resp);
        }else if("/order/add.do".equals(path)){
            add(req,resp);
        }else if("/admin/getAllOrder.do".equals(path)){
            getAllOrder(req,resp);
        }
    }

    //获取所有订单消息
    private void getAllOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrdersService ordersService = (OrdersService) ServiceFactory.getService(new OrdersServiceImpl());
        List<Map<String,Object>> mapList = ordersService.getAllOrder();

        req.setAttribute("orderList",mapList);
        req.getRequestDispatcher("/admin/showAllOrder.jsp").forward(req,resp);
    }

    //添加订单
    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        Integer aid = Integer.valueOf(req.getParameter("aid"));
        Integer sum = Integer.valueOf(req.getParameter("sum"));
        String oid = RandomUtils.createOrderId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(new Date());
        Orders orders = new Orders();
        orders.setOid(oid);
        orders.setUid(uid);
        orders.setAid(aid);
        orders.setOcount(sum);
        orders.setTime(time);
        orders.setState(1);

        OrdersService ordersService = (OrdersService) ServiceFactory.getService(new OrdersServiceImpl());
        boolean flag = ordersService.add(orders);
        if(flag){
            resp.sendRedirect("/orderSuccess.jsp");
        }else {
            resp.sendRedirect("/order/selectCartByUid.do?uid="+uid);
        }

    }

    //根据uid查询所有购物车信息
    private void selectCartByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));

        OrdersService ordersService = (OrdersService) ServiceFactory.getService(new OrdersServiceImpl());

        Map<String,Object> map = ordersService.selectCartByUid(uid);

        req.setAttribute("map",map);
        req.getRequestDispatcher("/order.jsp").forward(req,resp);
    }

    //根据订单id查询订单详细信息
    private void selectByOid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String oid = req.getParameter("oid");
        OrdersService ordersService = (OrdersService) ServiceFactory.getService(new OrdersServiceImpl());
        Map<String,Object> map = ordersService.selectByOid(oid);
        req.setAttribute("map",map);
        req.getRequestDispatcher("/orderDetail.jsp").forward(req,resp);
    }

    //根据用户id查询所有订单商品
    private void selectByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        OrdersService ordersService = (OrdersService) ServiceFactory.getService(new OrdersServiceImpl());
        List<Orders> orders = ordersService.selectByUid(uid);
        req.setAttribute("ordersList",orders);
        req.getRequestDispatcher("/orderList.jsp").forward(req,resp);
    }
}
