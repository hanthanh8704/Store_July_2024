package com.example.asm.controller;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.service.HoaDonService;
import com.example.asm.util.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class HoaDonController {
    @Autowired
    private LoginSession loginSession;
    private final HoaDonService hoaDonService;

    public HoaDonController(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    @GetMapping("/bill")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<HoaDon> listPage = hoaDonService.pageHoaDon(pageNo, pageSize);
            if (keyword != null) {
                listPage = hoaDonService.SearchandPageHoaDon(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("bill", listPage);
            return "hoa_don/index";
        }
    }

    @GetMapping("/hdct/{id}")
    public String getHdctByIdHD(@PathVariable("id") Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<HoaDon> hd = hoaDonService.findHoaDonById(id);
            if (hd.isPresent()) {
                HoaDon hoaDon = hd.get();
                List<HoaDonChiTiet> chiTietList = hoaDonService.getHDCTByHDID(id);
                model.addAttribute("bill", hoaDon);
                model.addAttribute("hdct", chiTietList);
            } else {
                model.addAttribute("error", "Hóa đơn không tồn tại");
            }
            return "hoa_don/index";
        }
    }
}
