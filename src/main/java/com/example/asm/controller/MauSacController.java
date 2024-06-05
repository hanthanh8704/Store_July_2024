package com.example.asm.controller;


import com.example.asm.model.MauSac;
import com.example.asm.service.MauSacService;
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
public class MauSacController {
    @Autowired
    LoginSession loginSession;
    private final MauSacService mauSacService;

    public MauSacController(MauSacService mauSacService) {
        this.mauSacService = mauSacService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    // Getall
    @GetMapping("/color")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<MauSac> listPage = mauSacService.pageMauSac(pageNo, pageSize);
            if (keyword != null) {
                listPage = mauSacService.SearchandPageMauSac(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("color", listPage);
            return "mau_sac/index";
        }
    }

    // View add
    @GetMapping("color/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("color", new MauSac());
            return "mau_sac/create";
        }
    }

    // Add
    @PostMapping("color/store")
    public String store(
            MauSac mauSac,
            Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            // Kiểm tra xem các ô input nếu bỏ trống
            if (mauSac.getMa() == null || mauSac.getMa().isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập mã màu"); // Thêm thông báo lỗi vào model
                return "mau_sac/create";
            } else if (mauSac.getTen() == null || mauSac.getTen().isEmpty()) {
                model.addAttribute("error1", "Vui lòng nhập tên màu sắc"); // Thêm thông báo lỗi vào model
                return "mau_sac/create";
            }else if (mauSac.getTrangThai() == null) {
                model.addAttribute("error2", "Vui lòng lựa chọn trạng thái"); // Thêm thông báo lỗi vào model
                return "mau_sac/create";
            } else {
                mauSacService.saveMauSac(mauSac);
                return "redirect:/admin/color";
            }
        }
    }



    // View update
    @GetMapping("color/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<MauSac> opt = mauSacService.findMauSacById(id);
            opt.ifPresent(mauSac -> model.addAttribute("color", mauSac));
            return "mau_sac/edit";
        }
    }

    // Update
    @PostMapping("color/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid MauSac mauSac,
                         BindingResult result,
                         Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            mauSacService.saveMauSac(mauSac);
            return "redirect:/admin/color";
        }
    }


    // Delete
    @GetMapping("color/delete/{id}")
    public String deleteMauSac(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            mauSacService.deleteMauSac(id);
            return "redirect:/admin/color";
        }
    }

}
