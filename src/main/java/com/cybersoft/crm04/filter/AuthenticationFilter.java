package com.cybersoft.crm04.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Kiểm tra filter 1 " );
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();


        if(session != null && session.getAttribute("email")!= null && !session.getAttribute("email").equals("")){
            if(session.getAttribute("roleName").equals("ROLE_ADMIN")){

                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                response.sendRedirect("http://localhost:8080/404");
            }

        }else {
            response.sendRedirect("http://localhost:8080/login");
        }

    }
}
