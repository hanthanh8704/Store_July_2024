package com.example.asm.controller;


import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.service.*;
import com.example.asm.util.ExcelConfig;
import com.example.asm.util.LoginSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class SanPhamChiTietController {
    @Autowired
    LoginSession loginSession;

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    private final SanPhamChiTietService sanPhamChiTietService;
    private final SanPhamService sanPhamService;
    private final KichThuocService kichThuocService;
    private final MauSacService mauSacService;
    private final StorageService storageService;
    private final CategoryService categoryService;

    public SanPhamChiTietController(SanPhamChiTietService sanPhamChiTietService, SanPhamService sanPhamService, KichThuocService kichThuocService, MauSacService mauSacService, StorageService storageService, CategoryService categoryService) {
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.sanPhamService = sanPhamService;
        this.kichThuocService = kichThuocService;
        this.mauSacService = mauSacService;
        this.storageService = storageService;
        this.categoryService = categoryService;
    }

    //Get all
    @GetMapping("/spct")
    public String getAll(
            Model model,
            @Param("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 4;
            Page<SanPhamChiTiet> listPage;
            if (keyword != null && !keyword.isEmpty()) {
                listPage = sanPhamChiTietService.SearchandPageSanPhamChiTiet(pageNo, pageSize, keyword);
                model.addAttribute("keyword", keyword);
            } else {
                listPage = sanPhamChiTietService.findPageSanPhamChiTiet(pageNo, pageSize);
            }

            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("productDetails", listPage.getContent());
            return "san_pham_chi_tiet/index";
        }
    }


    // View add
    @GetMapping("spct/create")
    public String create(Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            model.addAttribute("productDetails", new SanPhamChiTiet());
            model.addAttribute("product", sanPhamService.getAllSanPham());
            model.addAttribute("color", mauSacService.getAllMauSac());
            model.addAttribute("size", kichThuocService.getAllKichThuoc());
            model.addAttribute("category", categoryService.getAllSanPham());
            return "san_pham_chi_tiet/create";
        }
    }

    // Add
    @PostMapping("spct/store")
    public String store(@ModelAttribute("productDetails") @Valid SanPhamChiTiet spct,
                        BindingResult result,
                        Model model, @RequestParam("file") MultipartFile file) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            if (result.hasErrors()) {
                System.out.println("Bat dau check validate");
                model.addAttribute("product", sanPhamService.getAllSanPham());
                model.addAttribute("color", mauSacService.getAllMauSac());
                model.addAttribute("size", kichThuocService.getAllKichThuoc());
                model.addAttribute("category", categoryService.getAllSanPham());
                model.addAttribute("productDetails", spct); // Retain the form data
                return "san_pham_chi_tiet/create";
            } else {
                System.out.println("Them moi");
                storageService.store(file);
                String fileName = file.getOriginalFilename();
                spct.setImage(fileName);
                if (sanPhamChiTietService.saveSanPhamChiTiet(spct)) {
                    return "redirect:/admin/spct";
                } else {
//                this.sanPhamChiTietService.saveSanPhamChiTiet(spct);
                    return "san_pham_chi_tiet/create";
                }
            }
        }
    }


    // View edit
    @GetMapping("spct/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            Optional<SanPhamChiTiet> spct = sanPhamChiTietService.findSanPhamChiTietById(id);
            if (spct.isPresent()) {
                model.addAttribute("sanPhamChiTiet", spct.get()); // Đặt attribute với key đúng
                model.addAttribute("product", sanPhamService.getAllSanPham());
                model.addAttribute("color", mauSacService.getAllMauSac());
                model.addAttribute("category", categoryService.getAllSanPham());
                model.addAttribute("size", kichThuocService.getAllKichThuoc());
                return "san_pham_chi_tiet/edit";
            } else {
                // Xử lý trường hợp không tìm thấy sản phẩm chi tiết
                return "redirect:/admin/spct"; // Redirect hoặc thông báo lỗi tuỳ vào yêu cầu của bạn
            }
        }
    }

    //
    // Update
    @PostMapping("spct/update/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid SanPhamChiTiet sanPham,
                         BindingResult result,
                         Model model) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            if (result.hasErrors()) {
                return "san_pham_chi_tiet/edit";
            }
            sanPham.setId(id);
            this.sanPhamChiTietService.saveSanPhamChiTiet(sanPham);
            return "redirect:/admin/spct";
        }
    }


    // Delete
    @GetMapping("spct/delete/{id}")
    public String deleteSanPham(@PathVariable Integer id) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            sanPhamChiTietService.deleteSanPhamChiTiet(id);
            return "redirect:/admin/spct";
        }
    }

    @Autowired
    private ExcelConfig excelConfig;

    @GetMapping("spct/exportExcel")
    public void generateExportExcel(HttpServletResponse response) throws Exception {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment ; filename=productdetail.xls";

        response.setHeader(headerKey, headerValue);

        excelConfig.exportExcelCTSP(response);

        response.flushBuffer();
    }

    @PostMapping("spct/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        boolean importResult = excelConfig.importExcelSPCT(file);
        if (importResult) {
            model.addAttribute("spct", importResult);
            System.out.println("Import successful");
            return "redirect:/admin/spct";
        }
        return null;
    }
}
