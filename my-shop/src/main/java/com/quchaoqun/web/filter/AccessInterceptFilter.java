package com.quchaoqun.web.filter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AccessInterceptFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //对所有访问网站资源的请求进行拦截
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        //当用户访问非主页面，登录页面，和注册页面时，直接跳转到登录页面
        if(request.getServletPath().contains("login") || request.getServletPath().contains("index") ||
                request.getServletPath().contains("register") || request.getServletPath().equals("/")||
                session.getAttribute("user") != null) {
            filterChain.doFilter(request,response);
        }else {
            response.sendRedirect("/login.jsp");
        }


    }
}
