package com.example.asm.controller;


import com.example.asm.model.SanPham;
import com.example.asm.service.SanPhamService;
import com.example.asm.util.LoginSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class SanPhamController {
    private final SanPhamService sanPhamService;
    @Autowired
    LoginSession loginSession;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    @GetMapping("/product")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<SanPham> listPage = sanPhamService.findPageSanPham(pageNo, pageSize);
            if (keyword != null) {
                listPage = sanPhamService.SearchandPageSanPham(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("product", listPage);
            return "san_pham/index";
        }
    }

    // Add
    @PostMapping("product/store")
    public String store(
            SanPham sanPham,
            Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            sanPhamService.saveSanPham(sanPham);
            return "redirect:/admin/product";
        }
    }

    // View update
    @GetMapping("product/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<SanPham> sp = sanPhamService.findSanPhamById(id);
            sp.ifPresent(sanPham -> model.addAttribute("product", sanPham));
            return "san_pham/edit";
        }
    }

    // Update
    @PostMapping("product/update/{id}")
    public String update(@PathVariable("id") Integer id, SanPham sanPham, Model model) {
        sanPhamService.saveSanPham(sanPham);
        return "redirect:/admin/product";
    }

    // Delete
    @GetMapping("product/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            sanPhamService.deleteSanPham(id);
            return "redirect:/admin/product";
        }
    }
}
