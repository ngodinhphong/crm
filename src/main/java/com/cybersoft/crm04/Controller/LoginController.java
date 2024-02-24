package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.Services.LoginService;
import com.cybersoft.crm04.entity.UsersEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Các bước làm một chức năng trong ứng dụng
 * Bước 1: Phân tích yêu cầu chức năng, tức là phân tich chức năng đó mình cần làm gì và kết quả mong muốn là gì
 *
 * Bước 2: Xác định được câu truy vấn (query) giành cho chức năng đó
 *
 * Bước 3: từ câu truy vấn xác định được đường dẫn có nhận tham ố hay không và số lượng tham số là bao nhiêu
 *
 */

//Controler: nơi định nghĩa link
// Model : cho phép trả giá trị từ java ra file HTML (view)
//View : Chính là file html

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping ("")
    public String login( HttpServletRequest request, Model model){
        String email = loginService.saveEmail(request);
        model.addAttribute("email", email);

        String password = loginService.savePassword(request);
        model.addAttribute("password", password);

        return "login";
    }

    @PostMapping("")
    public String progressLogin(HttpSession httpSession, @RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response, @RequestParam(value = "remember", defaultValue = "false") boolean remembers){


        return loginService.performLogin(httpSession, email, password, model, response, remembers);
    }

}
