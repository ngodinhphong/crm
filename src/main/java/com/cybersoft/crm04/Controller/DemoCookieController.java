package com.cybersoft.crm04.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/cookie")
public class DemoCookieController {

    @GetMapping("")
    public String createCookie(HttpServletResponse response, HttpServletRequest request){
        //Tạo cookie
//        Cookie cookie = new Cookie("hello", "Emlacookiene");
//        Cookie cookie1 = new Cookie("username", URLEncoder.encode("Nguễn Văn A", StandardCharsets.UTF_8));
//
//        //Server bắt client tạo cookie
//        response.addCookie(cookie);
//        response.addCookie(cookie1);

        // Lấy toàn bộ cookie client truyền lên
        Cookie[] cookies = request.getCookies();

        for(Cookie cookei : cookies){
            // lâ tên cookie đang duyệt đến
            String name = cookei.getName();

            // Lấy gi trị cookie đang duyệt đến
            String value = cookei.getValue();

            System.out.println("kiem tra name " + name);
            System.out.println("kiem tra value " + value);
        }

        return "login";
    }

}
