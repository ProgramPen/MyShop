package com.quchaoqun.shoppingcart.controller;

import com.quchaoqun.shoppingcart.service.CartService;
import com.quchaoqun.shoppingcart.service.impl.CartServiceImpl;
import com.quchaoqun.shoppingcart.vo.CartsVo;
import com.quchaoqun.util.PrintJson;
import com.quchaoqun.util.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车模块
 */
public class CartServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if("/cart/add.do".equals(path)){
            addCart(req,resp);
        }else if("/cart/selectByUid.do".equals(path)){
            selectByUid(req,resp);
        }else if("/cart/update.do".equals(path)){
            update(req,resp);
        }else if("/cart/delete.do".equals(path)){
            delete(req,resp);
        }else if("/cart/clearByUid.do".equals(path)){
            clearByUid(req,resp);
        }
    }
    //根据用id删除清空用户购物栏
    private void clearByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid= Integer.valueOf(req.getParameter("uid"));
        CartService cartService = (CartService) ServiceFactory.getService(new CartServiceImpl());
        cartService.clearByUid(uid);
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }

    //删除购物车指定商品记录
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取参数信息
        Integer cid = Integer.valueOf(req.getParameter("cid"));
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));

        CartService cartService = (CartService) ServiceFactory.getService(new CartServiceImpl());
        //根据cid删除商品记录
        Map<String,Object> map = cartService.delete(cid,uid,pid);
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }

    //修改购物车商品数量
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer cnum = Integer.valueOf(req.getParameter("cnum"));
        Double price = Double.valueOf(req.getParameter("price"));

        CartService cartService = (CartService) ServiceFactory.getService(new CartServiceImpl());
        Map<String,Object> map = cartService.update(uid,pid,cnum,price);
        req.setAttribute("map",map);
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }

    //根据用户id查询购物车信息
    private void selectByUid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取参数信息
        Integer uid = Integer.valueOf(req.getParameter("uid"));

        //获取购物车逻辑处理层对象
        CartService cartService = (CartService) ServiceFactory.getService(new CartServiceImpl());
        Map<String,Object> map = cartService.selectByUid(uid);
        req.setAttribute("map",map);
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }

    //添加购物车商品信息，并查询修改后用户所有商品
    private void addCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收参数信息pid,uid,price;
        Integer pid = Integer.valueOf(req.getParameter("pid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        Double price = Double.valueOf(req.getParameter("price"));
        //获取购物车逻辑处理层对象
        CartService cartService = (CartService) ServiceFactory.getService(new CartServiceImpl());
        //将处理结果存储到map集合，并保存到request作用域中
        Map<String,Object> map = cartService.addCart(uid,pid,price);
        req.setAttribute("map",map);
        //将请求转发给cart.jsp页面
        req.getRequestDispatcher("/cart.jsp").forward(req,resp);
    }
}
