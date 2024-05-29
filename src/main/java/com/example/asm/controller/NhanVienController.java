package com.example.asm.controller;


import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import com.example.asm.service.NhanVienService;
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
public class NhanVienController {
    @Autowired
    LoginSession loginSession;
    private final NhanVienService nhanVienService;

    public NhanVienController(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }


    // Getall
    @GetMapping("/employee")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<NhanVien> listPage = nhanVienService.findNhanVien(pageNo, pageSize);
            if (keyword != null) {
                listPage = nhanVienService.SearchandPageNhanVien(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("employee", listPage);
            return "nhan_vien/index";
        }
    }

    // View-add
    @GetMapping("employee/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("employee", new NhanVien());
            return "nhan_vien/create";
        }
    }

    // Add
    @PostMapping("employee/store")
    public String store(
            @Valid NhanVien nhanVien,
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
                model.addAttribute("employee", nhanVien);
                return "nhan_vien/create";
            } else {
                nhanVienService.saveNhanVien(nhanVien);
                return "redirect:/admin/employee";
            }
        }
    }


    // View update
    @GetMapping("employee/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<NhanVien> sp = nhanVienService.findNhanVien(id);
            sp.ifPresent(nhanVien -> model.addAttribute("employee", nhanVien));
            return "nhan_vien/edit";
        }
    }

    // Update
    @PostMapping("employee/update/{id}")
    public String update(NhanVien nhanVien, @PathVariable Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            nhanVienService.saveNhanVien(nhanVien);
            return "redirect:/admin/employee";
        }
    }

    // Delete
    @GetMapping("employee/delete/{id}")
    public String deleteNhanVien(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            nhanVienService.deleteNhanVien(id);
            return "redirect:/admin/employee";
        }
    }

//    // Search
//    @GetMapping("employee/search")
//    public String search(@RequestParam String keyword, Model model) {
//        System.out.println("Bat dau tim kiem");
//        List<NhanVien> list = repository.search(keyword);
//        model.addAttribute("product", list);
//        return "redirect:/admin/employee";
//    }
}
