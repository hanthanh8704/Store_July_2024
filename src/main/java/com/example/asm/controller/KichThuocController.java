package com.example.asm.controller;

import com.example.asm.model.KichThuoc;
import com.example.asm.model.SanPham;
import com.example.asm.service.KichThuocService;
import com.example.asm.util.LoginSession;
import org.hibernate.engine.jdbc.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class KichThuocController {
    @Autowired
    LoginSession loginSession;
    private final KichThuocService kichThuocService;

    public KichThuocController(KichThuocService kichThuocService) {
        this.kichThuocService = kichThuocService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    // Getall
    @GetMapping("/size")
    public String getIndex(Model model,
                           @Param("keyword") String keyword,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<KichThuoc> listPage = kichThuocService.pageKichThuoc(pageNo, pageSize);
            if (keyword != null) {
                listPage = kichThuocService.SearchandPageKichThuoc(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("size", listPage);
            return "kich_thuoc/index";
        }
    }

    // View add
    @GetMapping("size/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("size", new Size());
            return "kich_thuoc/create";
        }
    }


    // Add
    @PostMapping("size/store")
    public String store(KichThuoc kichThuoc, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            // Kiểm tra xem các ô input nếu bỏ trống
            if (kichThuoc.getMa() == null || kichThuoc.getMa().isEmpty()) {
                model.addAttribute("error", "Vui lòng nhập mã"); // Thêm thông báo lỗi vào model
                return "kich_thuoc/create";
            } else if (kichThuoc.getTen() == null || kichThuoc.getTen().isEmpty()) {
                model.addAttribute("error1", "Vui lòng nhập tên"); // Thêm thông báo lỗi vào model
                return "kich_thuoc/create";
            }else if (kichThuoc.getTrangThai() == null) {
                model.addAttribute("error2", "Vui lòng lựa chọn trạng thái"); // Thêm thông báo lỗi vào model
                return "kich_thuoc/create";
            } else {
                kichThuocService.saveKichThuoc(kichThuoc);
                return "redirect:/admin/size";
            }
        }
    }

    // View update
    @GetMapping("size/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<KichThuoc> kt = kichThuocService.findKichThuoc(id);
            kt.ifPresent(kichThuoc -> model.addAttribute("size", kichThuoc));
            return "kich_thuoc/edit";
        }
    }

    // Update
    @PostMapping("size/update/{id}")
    public String update(KichThuoc kichThuoc, @PathVariable Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            kichThuocService.saveKichThuoc(kichThuoc);
            return "redirect:/admin/size";
        }
    }


    @GetMapping("size/delete/{id}")
    public String deleteKichThuoc(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            kichThuocService.deleteKichThuoc(id);
            return "redirect:/admin/size";
        }
    }

    // Search
//    @GetMapping("size/search")
//    public String search(@RequestParam String keyword, Model model) {
//        System.out.println("Bat dau tim kiem");
//        List<KichThuoc> list = repository.search(keyword);
//        model.addAttribute("product", list);
//        return "redirect:/admin/product";
//    }
}
