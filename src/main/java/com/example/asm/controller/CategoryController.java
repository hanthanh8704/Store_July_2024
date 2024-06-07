package com.example.asm.controller;


import com.example.asm.model.Category;
import com.example.asm.model.MauSac;
import com.example.asm.service.CategoryService;
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
public class CategoryController {
    @Autowired
    LoginSession loginSession;
    private final CategoryService mauSacService;

    public CategoryController(CategoryService mauSacService) {
        this.mauSacService = mauSacService;
    }


    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    // Getall
    @GetMapping("/category")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<Category> listPage = mauSacService.findPageSanPham(pageNo, pageSize);
            if (keyword != null) {
                listPage = mauSacService.SearchandPageSanPham(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("category", listPage);
            return "category/index";
        }
    }

    // View add
    @GetMapping("category/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("category", new Category());
            return "category/create";
        }
    }

    // Add
    @PostMapping("category/store")
    public String store(
            Category mauSac,
            Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            // Kiểm tra xem các ô input nếu bỏ trống
            if (mauSac.getMa() == null || mauSac.getMa().isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập mã màu"); // Thêm thông báo lỗi vào model
                return "category/create";
            } else if (mauSac.getTen() == null || mauSac.getTen().isEmpty()) {
                model.addAttribute("error1", "Vui lòng nhập tên màu sắc"); // Thêm thông báo lỗi vào model
                return "category/create";
            }else if (mauSac.getTrangThai() == null) {
                model.addAttribute("error2", "Vui lòng lựa chọn trạng thái"); // Thêm thông báo lỗi vào model
                return "category/create";
            } else {
                mauSacService.saveSanPham(mauSac);
                return "redirect:/admin/category";
            }
        }
    }



    // View update
    @GetMapping("category/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<Category> opt = mauSacService.findSanPhamById(id);
            opt.ifPresent(mauSac -> model.addAttribute("category", mauSac));
            return "category/edit";
        }
    }

    // Update
    @PostMapping("category/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid Category mauSac,
                         BindingResult result,
                         Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            mauSacService.saveSanPham(mauSac);
            return "redirect:/admin/category";
        }
    }


    // Delete
    @GetMapping("category/delete/{id}")
    public String deleteMauSac(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            mauSacService.deleteSanPham(id);
            return "redirect:/admin/category";
        }
    }
}
