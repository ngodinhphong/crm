package com.cybersoft.crm04.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        System.out.println("Kiểm tra filter "  + request.getServletPath()); //lấy đươờng dẫn người dùng đang gọi mà kích hoạt login

        HttpSession session = request.getSession();


        // Kiểm tra xem session lưu trữ ở login lúc đăng nhập thành công có tồn tại không
        if(session != null && session.getAttribute("email")!= null && !session.getAttribute("email").equals("")){
            // chuyển hớng về trang chủ
            response.sendRedirect("http://localhost:8080/index");
        }else {
            // cho đi tiếp vào đường dẫn mà client đang gọi hoặc thoát ra khỏi filter và đi tiếp
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    /**
     * Nếu vào link là /role mà cha đăng nhập thì sẻ chuển về trang đăng nhập
     */
}
