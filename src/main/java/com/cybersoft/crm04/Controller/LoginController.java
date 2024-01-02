package com.cybersoft.crm04.Controller;

import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
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

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping ("")
    public String login( HttpServletRequest request, Model model){
        String email = "";
        String password = "";
        Cookie[] cookies = request.getCookies();

        for(Cookie cookei : cookies) {

            if(cookei.getName().equals("email")){
                email = cookei.getValue();

            } else if (cookei.getName().equals("password")) {
                password = cookei.getValue();

            }
        }
        model.addAttribute("email", email);
        model.addAttribute("password", password);





        List<UsersEntity> list = usersRepository.findByEmailAndPassword("Nguyevana@gmail.com", "123456");
        for(UsersEntity item : list){
            System.out.println("Kiemtra: " + item.getEmail());
        }
        return "login";
    }
    //Controler: nơi định nghĩa link
    // Model : cho phép trả giá trị từ java ra file HTML (view)
    //View : Chính là file html

    @PostMapping("")
    public String progressLogin(HttpSession httpSession, @RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response, @RequestParam(value = "remember", defaultValue = "false") boolean remember){


        /**
         * Hoàn thiện chức năng login
         * Bước 1: Thế tham số người dùng truyền vào hàm findByEmailAndPassword
         * Bước 2: Kiểm tra xem list có dữ liệu hay không ?
         *  * làm cách nào nhận được tham số?
         *  * làm cách nào có thể gọi được link/login với phương thức post
         *  * làm cách nào để truyền email và password?
         * Bước 3: Nếu có thì trả ra chuyên qua dashboard(lưu tạo link/ dashboard sử dụng page index.html)
         * Bước 4: Nếu thât bại thì xuất thông báo " Đăng nhập thất bại" ra màng hình login
         *  * làm cách nào để trả giá trị biến ra htlm?
         *  * làm cácnh nào để
         * *Lưu ý: phương thức post=> Chỉnh form data bên giao diện login
         *
         */

        List<UsersEntity> listUser = usersRepository.findByEmailAndPassword(email, password);



        if(listUser.size()>0){
            // có giá trị => đăng nhập thành công


            if(remember){
                Cookie saveEmail = new Cookie("email", email);
                response.addCookie(saveEmail);

                Cookie savePassword = new Cookie("password", password);
                response.addCookie(savePassword);
            }


            return "redirect:/index";

        }else {
            // Không có giá trị => đăng nhập thất bại

            // Đẩy giá trị iSuccess ra file html và đặt tên key ( biến) là isSuccess
            model.addAttribute("error", "Login failed");

            return "login";

        }

        /**
         * Khi đăng nhập thành công thì lưu email và mật khẩu va cookie
         * Khi người dùng vô lại link/login thì sẻ điền sẵng email và mật khẩu vào input
         */

    }

}
