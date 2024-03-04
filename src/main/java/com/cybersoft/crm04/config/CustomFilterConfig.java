package com.cybersoft.crm04.config;

import com.cybersoft.crm04.filter.AdminAndManageFilter;
import com.cybersoft.crm04.filter.AuthenticationFilter;
import com.cybersoft.crm04.filter.CustomFilter;
import com.cybersoft.crm04.filter.PermissionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFilterConfig {

    @Bean
    public FilterRegistrationBean<CustomFilter> CustomFilterConfig(){

        FilterRegistrationBean<CustomFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomFilter());
        registrationBean.addUrlPatterns("/login"); // khi người dùng gọi link là /login mới kích hoạt filter
        registrationBean.setOrder(1);

        return registrationBean;

    }

    @Bean
    public FilterRegistrationBean<PermissionFilter> filterConfig(){

        FilterRegistrationBean<PermissionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new PermissionFilter());
        registrationBean.addUrlPatterns("/index", "/job/*", "/role/*", "/task/*", "/user/*"); // khi người dùng gọi link là /login mới kích hoạt filter
        registrationBean.setOrder(2);

        return registrationBean;

    }

    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterConfig (){

        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/role/*", "/job/add", "/user/add", "/user/update/*", "/user/delete/*"); // khi người dùng gọi link là /role mới kích hoạt filter
        registrationBean.setOrder(3);

        return registrationBean;

    }

    @Bean
    public FilterRegistrationBean<AdminAndManageFilter> adminAndManageFilterConfig (){

        FilterRegistrationBean<AdminAndManageFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminAndManageFilter());
        registrationBean.addUrlPatterns("/user/show","/user/look/*", "/job/update/*", "/job/look/*", "/job/delete/*", "/task/add");
        registrationBean.setOrder(4);

        return registrationBean;

    }

}

/**
 * 1) nếu như đã đăng nhập rồi thì không cần đăng nhập lại đá vè trang chủ không cho vo trang đăng nhập
 * Bước 1: khi đăng nhập thành công thìphair lưu lại thng tin user đã đăng nhập ( Session/Cookie )
 * Bước 2: khi người dùng vào lai jtrang login thì kiểm tra xem Session/Cookie lưu trữ thôngtin người dùng có tồn tại khng
 * Bước 3: Nếu tồn tại chuyển hướng về trang login
 * Bước 4: Nếu không tồn tại thì cho ề trang login
 *
 * 2)Hay làm tính năng phân quyền cho hệ thống CRM
 * - ADMIN : thêm, xóa sửa ROLE
 * - Nhân viên : Xem được thông tin nhân viên
 *
 *
 */
