package com.cybersoft.crm04.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AdminAndManageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if(session.getAttribute("roleName").equals("ROLE_ADMIN") || session.getAttribute("roleName").equals("ROLE_MANAGE")){

            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            response.sendRedirect("http://localhost:8080/404");
        }
    }
}
