package com.quchaoqun.usermanager.controller;

import com.quchaoqun.usermanager.domain.Address;
import com.quchaoqun.usermanager.domain.User;
import com.quchaoqun.usermanager.exception.UserLoginException;
import com.quchaoqun.usermanager.service.UserService;
import com.quchaoqun.usermanager.service.impl.UserServiceImpl;
import com.quchaoqun.util.*;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet
public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的uri,并根据uri进行调用处理方法处理请求
        String path = req.getServletPath();
        if("/user/login.do".equals(path)){
            Login(req,resp);
        }else if("/user/checkUser".equals(path)){
            check(req,resp);
        }else if("/user/register.do".equals(path)){
            registry(req,resp);
        }else if("/user/selectAddress.do".equals(path)){
            selectAddress(req,resp);
        }else if("/user/updateAddress.do".equals(path)){
            updateAddress(req,resp);
        }else if("/user/deleteAddress.do".equals(path)){
            deleteAddress(req,resp);
        }else  if("/user/updateState.do".equals(path)){
            updateState(req,resp);
        }else if("/user/addAddress.do".equals(path)){
            addAddress(req,resp);
        }else if("/user/logout.do".equals(path)){
            logout(req,resp);
        }else if("/admin/adminlogin.do".equals(path)){
            adminLogin(req,resp);
        }else if("/admin/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if("/admin/getUserByNameAndSex.do".equals(path)){
            getUserByNameAndSex(req,resp);
        }else if("/admin/deleteUserByUid.do".equals(path)){
            deleteUserByUid(req,resp);
        }
    }

    //根据uid删除用户
    private void deleteUserByUid(HttpServletRequest req, HttpServletResponse resp) {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.deleteUserByUid(uid);
        PrintJson.printJsonFlag(resp,flag);
    }

    //根据姓名或性别查询用户
    private void getUserByNameAndSex(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        String sex = req.getParameter("sex");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> users = userService.getUserByNameAndSex(username,sex);
        PrintJson.printJsonObj(resp,users);
    }

    //获取所有用户
    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        Integer state = Integer.valueOf(req.getParameter("state"));
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list = userService.getUserList(state);
        PrintJson.printJsonObj(resp,list);
    }

    //管理员登录
    private void adminLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
        String username = req.getParameter("username");
        String password = MD5Utils.md5(req.getParameter("password"));
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        User user = null;
        try {
            user = userService.login(username,password);
            if(user !=null && user.getRole() ==1){
                req.getSession().setAttribute("user",user);
                req.getRequestDispatcher("/admin/admin.jsp").forward(req,resp);
            }

        } catch (UserLoginException e) {
            e.printStackTrace();
            req.getRequestDispatcher("/admin/login.jsp?msg=登录失败！").forward(req,resp);
        }

    }


    //根据uid给用户添加新的收货地址
    private void addAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String detail = req.getParameter("detail");

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = false;
        if(detail != "" && phone !="" && name != ""){
            flag =true;
            Address address = new Address();
            address.setUid(uid);
            address.setName(name);
            address.setPhone(phone);
            address.setDetail(detail);
            userService.addAddress(address);
        }
        req.setAttribute("success",flag);
        req.getRequestDispatcher("/self_info.jsp").forward(req,resp);
    }

    //根据aid修改默认地址
    private void updateState(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer aid = Integer.valueOf(req.getParameter("aid"));
        Integer uid = Integer.valueOf(req.getParameter("uid"));

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.updateState(aid,uid);
        req.setAttribute("success",flag);
        req.getRequestDispatcher("/self_info.jsp").forward(req,resp);
    }

    //根据aid删除用户地址信息
    private void deleteAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer aid = Integer.valueOf(req.getParameter("aid"));
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.deleteAddress(aid);
        req.setAttribute("success",flag);
        req.getRequestDispatcher("/self_info.jsp").forward(req,resp);
    }

    //根据地址id来修改地址信息
    private void updateAddress(HttpServletRequest req, HttpServletResponse resp) {
        Integer aid = Integer.valueOf(req.getParameter("aid"));
        String aname = req.getParameter("aname");
        String aphone = req.getParameter("aphone");
        String adetail = req.getParameter("adetail");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.updateAddress(aid,aname,aphone,adetail);
        PrintJson.printJsonFlag(resp,flag);
    }

    //用户收货地址查询
    private void selectAddress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer uid = Integer.valueOf(req.getParameter("uid"));
        //动态获取UserService对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<Address> addresses = userService.selectAddress(uid);
        req.setAttribute("list",addresses);
        req.getRequestDispatcher("/self_info.jsp").forward(req,resp);
    }

    //登出
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session =req.getSession();
        User user = (User) (session.getAttribute("user"));
        int role = user.getRole();
        session.setAttribute("user",null);
        Cookie cookies[] = req.getCookies();
        for(Cookie cookie :cookies){
            if(cookie.getName().equals("username")){
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
            if(cookie.getName().equals("password")){
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
        }
        if (role == 0){
            resp.sendRedirect("/index.jsp");
        }else if(role == 1){
            resp.sendRedirect("/admin/login.jsp");
        }

    }

    //登录验证
    public void Login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String pwd = req.getParameter("pwd");
        String password = MD5Utils.md5(pwd);
        String auto = req.getParameter("auto");
        //动态获取UserService对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        User user =null;
        Map map = new HashMap();
        try {
            user = userService.login(username,password);
            //给每个登录的用户，创建session域对象存储user对象
            HttpSession session = req.getSession();
            session.setAttribute("user",user);
            if(auto != null && auto != ""){
                Cookie card1 = new Cookie("username",username);
                Cookie card2 = new Cookie("password",pwd);
                card1.setMaxAge(60*60*24*14);
                card2.setMaxAge(60*60*24*14);
                resp.addCookie(card1);
                resp.addCookie(card2);
            }
            map.put("success",true);
        } catch (UserLoginException e) {
            map.put("excMsg",e.getMessage());
            map.put("success",false);
        }
        PrintJson.printJsonObj(resp,map);
    }
    //用户注册信息用户名查询
    public void check(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数信息
        String username = req.getParameter("username");
        //调用service查看用户名是否已经存在
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        int count = userService.check(username);
        PrintJson.printJsonObj(resp,count);
    }

    //用户信息注册
    private void registry(HttpServletRequest req, HttpServletResponse resp) {
        //获取参数信息
        User user = new User();
        user.setName(req.getParameter("username"));
        user.setPassword(MD5Utils.md5(req.getParameter("password")));
        user.setEmail(req.getParameter("email"));
        user.setSex(req.getParameter("sex"));
        user.setStatus(1);
        user.setCode(Base64Utils.encode(RandomUtils.getTime()));
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        boolean flag = userService.registry(user);
        PrintJson.printJsonFlag(resp,flag);
    }



}
