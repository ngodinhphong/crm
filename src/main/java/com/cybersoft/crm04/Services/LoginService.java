package com.cybersoft.crm04.Services;

import com.cybersoft.crm04.entity.UsersEntity;
import com.cybersoft.crm04.repository.UsersRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    public String saveEmail(HttpServletRequest request){
        String email = "";
        Cookie[] cookies = request.getCookies();

        for(Cookie cookei : cookies) {

            if(cookei.getName().equals("email")) {
                email = cookei.getValue();
            }
        }
        return email;
    }

    public String savePassword(HttpServletRequest request){
        String password = "";
        Cookie[] cookies = request.getCookies();

        for(Cookie cookei : cookies) {
            if (cookei.getName().equals("password")) {
                password = cookei.getValue();
            }
        }
        return password;
    }

    public String performLogin(HttpSession httpSession, String email, String password, Model model, HttpServletResponse response, boolean remember) {

        List<UsersEntity> listUser = usersRepository.findByEmailAndPassword(email, password);
        String roleName = "";
        for (UsersEntity usersEntity : listUser) {
            if (usersEntity.getEmail().equals(email)) {
                roleName = usersEntity.getRolesEntity().getName();
                break;
            }
        }
        if (!listUser.isEmpty()) {
            // có giá trị => đăng nhập thành công

            httpSession.setAttribute("email", email);
            httpSession.setMaxInactiveInterval(8 * 60 * 60);

            httpSession.setAttribute("roleName", roleName);
            httpSession.setMaxInactiveInterval(8 * 60 * 60);

            if(remember){
                Cookie saveEmail = new Cookie("email", email);
                response.addCookie(saveEmail);

                Cookie savePassword = new Cookie("password", password);
                response.addCookie(savePassword);
            }

            return "redirect:/index";

        } else {
            // Không có giá trị => đăng nhập thất bại
            model.addAttribute("error", "Đăng nhập thất bại");

            return "login";
        }
    }
}

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
