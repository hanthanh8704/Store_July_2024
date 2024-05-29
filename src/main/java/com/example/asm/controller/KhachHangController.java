package com.example.asm.controller;


import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPham;
import com.example.asm.service.KhachHangService;
import com.example.asm.util.LoginSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class KhachHangController {
    @Autowired
    LoginSession loginSession;
    private final KhachHangService khachHangService;

    public KhachHangController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    @GetMapping("/customer")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<KhachHang> listPage = khachHangService.findPageKhachHang(pageNo, pageSize);
            if (keyword != null) {
                listPage = khachHangService.SearchandPageKhachHang(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("customer", listPage);
            return "khach_hang/index";
        }
    }


    // View-add
    @GetMapping("customer/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("customer", new KhachHang());
            return "khach_hang/create";
        }
    }

    // Add
    @PostMapping("customer/store")
    public String store(
            @Valid KhachHang khachHang,
            BindingResult result,
            Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            if (result.hasErrors()) {
                Map<String, String> fields = new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {
                    fields.put(error.getField(), error.getDefaultMessage());
                }
                model.addAttribute("error", "Vui lòng không bỏ trống");
                model.addAttribute("customer", khachHang);
                return "khach_hang/create";
            } else {
                khachHangService.saveKhachHang(khachHang);
                return "redirect:/admin/customer";
            }
        }
    }

    // View update
    @GetMapping("customer/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<KhachHang> sp = khachHangService.findKhachHangById(id);
            sp.ifPresent(khachHang -> model.addAttribute("customer", khachHang));
            return "khach_hang/edit";
        }
    }

    // Update
    @PostMapping("customer/update/{id}")
    public String update(KhachHang khachHang, @PathVariable Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            khachHangService.saveKhachHang(khachHang);
            return "redirect:/admin/customer";
        }
    }

    // Delete
    @GetMapping("customer/delete/{id}")
    public String deleteKhachHang(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            khachHangService.deleteKhachHang(id);
            return "redirect:/admin/customer";
        }
    }

    // Search
//    @GetMapping("customer/search")
//    public String search(@RequestParam String keyword, Model model) {
//        System.out.println("Bat dau tim kiem");
//        List<KhachHang> list = repository.search(keyword);
//        model.addAttribute("product", list);
//        return "redirect:/admin/customer";
//    }
}
