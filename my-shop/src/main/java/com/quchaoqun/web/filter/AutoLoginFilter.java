package com.quchaoqun.web.filter;


import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AutoLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //对所有访问网站资源的请求进行拦截
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取所有的Cookie
        String username = "";
        String password = "";
        Cookie cookies[] = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("username") && cookie.getValue() != null){
                    username = cookie.getValue();
                }
                if(cookie.getName().equals("password") && cookie.getValue() != null){
                    password = cookie.getValue();
                }
            }
        }

        HttpSession session = request.getSession();

        if(!username.equals("") && !password.equals("") && (session.getAttribute("user") == null || session.getAttribute("user").equals(""))){
            request.getRequestDispatcher("/user/login.do?username="+username+"&pwd="+password).forward(request,response);
        }else {
            filterChain.doFilter(request,response);
        }
    }
}
