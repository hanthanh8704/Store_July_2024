package com.example.asm.controller;

import com.example.asm.model.NhanVien;
import com.example.asm.repository.LoginRepository;
import com.example.asm.repository.NhanVienRepository;
import com.example.asm.service.LoginService;
import com.example.asm.util.LoginSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LoginService loginService;

    public AdminController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String index() {
        return "redirect:/admin/";
    }

    @GetMapping("/")
    public String homeAdmin() {
        if (LoginSession.isLogin()){
            return "hihi";
        }else {
            return "redirect:/admin/logon";
        }
    }

    @GetMapping("/logon")
    public String loginForm(Model model) {
        System.out.println("Bắt đầu login");
        model.addAttribute("nhanVien", new NhanVien());
        return "login";
    }

    @Autowired
    private HttpSession session;

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute NhanVien nhanVien, Model model) {
        NhanVien foundNhanVien = loginService.findByUsernameAndPassword(nhanVien.getUsername(), nhanVien.getPassword());
        if (foundNhanVien != null) {
            LoginSession.setNhanVien(foundNhanVien);
            if (LoginSession.isAdmin()) {
                session.setAttribute("nhanVien", LoginSession.nhanVien);
                return "redirect:/admin/";
            } else {
                session.setAttribute("nhanVien", LoginSession.nhanVien);
                return "redirect:/admin/product";
            }
        } else {
            model.addAttribute("error", "Đăng nhập thất bại. Vui lòng kiểm tra lại tài khoản và mật khẩu.");
            return "login";
        }
    }

}
