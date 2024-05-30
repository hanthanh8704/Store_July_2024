package com.example.asm.controller;


import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.service.*;
import com.example.asm.util.LoginSession;
import com.example.asm.util.Timestamp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class BanHangController {
    @Autowired
    private LoginSession loginSession;
    private final HoaDonService hoaDonService;
    private final HoaDonChiTietService hoaDonChiTietService;
    private final SanPhamChiTietService sanPhamChiTietService;
    private final NhanVienService nhanVienService;
    private final KhachHangService khachHangService;

    public BanHangController(HoaDonService hoaDonService, HoaDonChiTietService hoaDonChiTietService, SanPhamChiTietService sanPhamChiTietService, NhanVienService nhanVienService, KhachHangService khachHangService) {
        this.hoaDonService = hoaDonService;
        this.hoaDonChiTietService = hoaDonChiTietService;
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.nhanVienService = nhanVienService;
        this.khachHangService = khachHangService;
    }

    // Kiểm tra đăng nhập
    private boolean isLoggedIn() {
        return LoginSession.isLogin();
    }

    // Hàm này dùng để hiển thị view bán hàng
    @GetMapping("orders")
    public String getOrders(Model model,
                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (!isLoggedIn()) {
            return "redirect:/admin/logon";
        } else {
            int pageSize = 5;
            Page<SanPhamChiTiet> listPage = sanPhamChiTietService
                    .findPageSanPhamChiTiet(pageNo, pageSize);
            BigDecimal totalAmount = hoaDonService.calculateTotalAmount(); // Thêm hàm tính tổng tiền ở đây
            model.addAttribute("hd", hoaDonService.fillAllHoaDon());
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("totalPages", listPage.getTotalPages());
            model.addAttribute("totalItems", listPage.getTotalElements());
            model.addAttribute("totalAmount", totalAmount);
            model.addAttribute("spct", listPage);
            return "ban_hang/index";
        }
    }

    // Hàm này dùng để tìm kiếm khách hàng bằng số điện thoại
    @GetMapping("/search")
    public String searchKH(@RequestParam("sdt") String sdt,
                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                           Model model) {
        KhachHang khachHang = khachHangService.findKhachHangBySdt(sdt);
        if (khachHang == null) {
            model.addAttribute("error", "Khách hàng không tồn tại");
        } else {

            model.addAttribute("khachHang", khachHang);
            Integer idKhachHang = khachHang.getId();
            System.out.println("ID khách hàng :" + idKhachHang);
            KhachHang khTaoHoaDon = khachHangService.findKhachHangById2(idKhachHang);
            System.out.println("Khách hàng tạo HĐ : " + khTaoHoaDon);
        }
        BigDecimal totalAmount = hoaDonService.calculateTotalAmount();
        // Call getOrders manually to set necessary attributes
        int pageSize = 5;
        Page<SanPhamChiTiet> listPage = sanPhamChiTietService.findPageSanPhamChiTiet(pageNo, pageSize);
        model.addAttribute("hd", hoaDonService.fillAllHoaDon());
        model.addAttribute("hdct", hoaDonChiTietService.fillAllHDCT());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", listPage.getTotalPages());
        model.addAttribute("totalItems", listPage.getTotalElements());
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("spct", listPage);

        return "ban_hang/index";
    }


    // Hàm này dùng để tạo hóa đơn mới có trạng thái chờ
    @PostMapping("/create-order")
    public String taoMoiHoaDon(@RequestParam(name = "sdt", required = false) String sdt,
                               @RequestParam(name = "id", required = false) Integer id,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        HoaDon hoaDon = new HoaDon();

        if (id != null) {
            KhachHang khTaoHoaDon = khachHangService.findKhachHangById2(id);
            System.out.println("Khách hàng tạo HĐ: " + khTaoHoaDon);

            if (khTaoHoaDon != null) {
                hoaDon.setKhachHang(khTaoHoaDon);

                // Các thông tin khác của hóa đơn
                hoaDon.setNhanVien(LoginSession.nhanVien);
                hoaDon.setNgayMuaHang(Timestamp.timeNow());
                hoaDon.setTrangThai(0);
            } else {
                // Xử lý khi không tìm thấy khách hàng với ID đã nhập
                redirectAttributes.addFlashAttribute("error", "Khách hàng không tồn tại");
                return "redirect:/admin/orders";
            }
        } else {
            // Trường hợp nhân viên không tìm kiếm theo số điện thoại, tạo hóa đơn với khách hàng có id là 11
            KhachHang khMacDinh = khachHangService.findKhachHangById2(11);
            if (khMacDinh != null) {
                hoaDon.setKhachHang(khMacDinh);
            }

            // Các thông tin khác của hóa đơn
            hoaDon.setNhanVien(LoginSession.nhanVien);
            hoaDon.setNgayMuaHang(Timestamp.timeNow());
            hoaDon.setTrangThai(0);
        }

        hoaDonService.saveHoaDon(hoaDon);
        return "redirect:/admin/orders";
    }


    // Hàm này dùng để xem detail của hóa đơn
    @GetMapping("/detail-order/{id}")
    public String detailHoaDon(@PathVariable("id") Integer id, Model model) {
        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon == null) {
            model.addAttribute("error", "Hóa đơn không tồn tại");
        } else {
            BigDecimal tongTien = hoaDon.getTongTien();
            List<HoaDonChiTiet> hoaDonChiTietList = hoaDonService.getHDCTByHDID(id);

            model.addAttribute("hoaDon", hoaDon);
            model.addAttribute("hdct", hoaDonChiTietList); // Chỉ hiển thị chi tiết hóa đơn của hóa đơn này
            model.addAttribute("khachHang", hoaDon.getKhachHang());
            model.addAttribute("nhanVien", hoaDon.getNhanVien());
            model.addAttribute("tongTien", tongTien);
        }

        // Call getOrders manually to set necessary attributes
        int pageNo = 1; // or another default page number
        int pageSize = 5;
        Page<SanPhamChiTiet> listPage = sanPhamChiTietService.findPageSanPhamChiTiet(pageNo, pageSize);
        model.addAttribute("hd", hoaDonService.fillAllHoaDon());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", listPage.getTotalPages());
        model.addAttribute("totalItems", listPage.getTotalElements());
        model.addAttribute("spct", listPage);

        return "ban_hang/index";
    }


    // Hàm này dùng để thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public String themSanPhamVaoGioHang(@RequestParam("spctId") Integer spctId,
                                        @RequestParam("id") Integer id,
                                        @RequestParam("soLuong") Integer soLuong, Model model) {
        System.out.println("IDSPCT : " + spctId);
        System.out.println("ID : " + id);
        System.out.println("Số Lượng : " + soLuong);

        // Kiểm tra số lượng tồn trong giỏ hàng
        Integer soLuongSPCT = sanPhamChiTietService.getSoLuongTonByIDCTSP(spctId);
        Integer soLuongTrongGio = sanPhamChiTietService.getSoLuongTrongGioHangByIdCTSP(spctId);
        int soLuongTon = soLuongTrongGio + soLuongSPCT;

        // Kiểm tra nếu số lượng nhỏ hơn 1 hoặc lớn hơn tồn kho trong giỏ hàng
        if (soLuong < 1) {
            model.addAttribute("error", "Số lượng phải lớn hơn 0");
        } else if (soLuong > soLuongTon) {
            model.addAttribute("error", "Số lượng tồn không đủ");
        } else {
            // Kiểm tra nếu sản phẩm đã tồn tại trong giỏ hàng
            if (hoaDonService.kiemTraSanPhamTonTaiTrongGioHang(id, spctId)) {
                // Nếu sản phẩm đã tồn tại, cộng dồn số lượng
                hoaDonService.congDonSoLuongSanPhamTrongGioHang(id, spctId, soLuong);
            } else {
                // Nếu sản phẩm chưa tồn tại, thêm mới vào giỏ hàng
                try {
                    hoaDonService.themSanPhamVaoHoaDonChiTiet(id, spctId, soLuong);
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng");
                }
            }
        }

        // Sau khi xử lý, chuyển hướng về trang đơn hàng
        return "redirect:/admin/orders";
    }



    // Hàm này dùng để checkAdd giỏ hàng
//    @PostMapping("/add")
//    public String themSanPhamVaoGioHang(@RequestParam("spctId") Integer spctId,
//                                        @RequestParam("id") Integer id,
//                                        @RequestParam("soLuong") Integer soLuong, Model model) {
//        System.out.println("IDSPCT : " + spctId);
//        System.out.println("ID : " + id);
//        System.out.println("Số Lượng : " + soLuong);
//        Integer soLuongSPCT = sanPhamChiTietService.getSoLuongTonByIDCTSP(spctId);
//        Integer soLuongTrongGio = sanPhamChiTietService.getSoLuongTrongGioHangByIdCTSP(spctId);
//        try {
//            int soLuongTon = soLuongTrongGio + soLuongSPCT;
//            if (soLuong < 1) {
//                model.addAttribute("error", "Số lượng phải lớn hơn 0");
//            } else if (soLuong > soLuongTon) {
//                model.addAttribute("error", "Số lượng tồn không đủ");
//            } else {
//                hoaDonService.themSanPhamVaoHoaDonChiTiet(id, spctId, soLuong);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng");
//        }
//        return "redirect:/admin/orders";
//    }



    // Hàm này dùng để xóa sản phẩm trong giỏ hàng
    @GetMapping("/deleteSPCT/{id}")
    public String xoaSanPhamTrongGioHang(
            @RequestParam("idCTSP") Integer spctId,
            @RequestParam("idHoaDon") Integer idHoaDon,
            @PathVariable("id") Integer idHDCT,
            Model model
    ) {
        if (idHoaDon == null) {
            model.addAttribute("error", "Bạn chưa lựa chọn hóa đơn id");
        } else {
            System.out.println("Bat dau xoa dong 228");
            System.out.println("ID Chi tiết sản phẩm : " + spctId);
            System.out.println("ID hóa đơn : " + idHoaDon);
            System.out.println("ID hóa đơn chi tiết : " + idHDCT);

            hoaDonService.xoaSanPhamKhoiGioHang(spctId, idHDCT);
        }
        return "redirect:/admin/orders";
    }

    // Hàm này dùng để update sản phẩm trong giỏ hàng
    @PostMapping("/updateSPCT/{id}")
    public String updateSanPhamTrongGioHang(
            @PathVariable("id") Integer idHDCT,
            @RequestParam("idCTSP") Integer spctID,
            @RequestParam("idHoaDon") Integer idHD,
            @RequestParam("soLuongTrongGio") Integer soLuongTrongGioHang,
            @RequestParam("soLuongThayDoi") Integer soLuongThayDoi,
            Model model
    ) {
        if (idHD == null) {
            model.addAttribute("error", "Bạn chưa lựa chọn hóa đơn id");
        } else {
            System.out.println("ID Chi tiết sản phẩm: " + spctID);
            System.out.println("ID hóa đơn: " + idHD);
            System.out.println("ID hóa đơn chi tiết: " + idHDCT);
            System.out.println("Số lượng sản phẩm trong giỏ: " + soLuongTrongGioHang);
            System.out.println("Số lượng sản phẩm thay đổi: " + soLuongThayDoi);

            hoaDonService.capNhatSanPhamTrongGioHang(spctID, idHDCT, soLuongThayDoi, soLuongTrongGioHang);
        }
        return "redirect:/admin/orders";
    }

    // Hàm này dùng để thanh toán hóa đơn
    @GetMapping("/thanhToanHD")
    public String thanhToan(
            @RequestParam(value = "idKH", required = false) Integer idKH,
            @RequestParam(value = "idNV", required = false) Integer idNV,
            @RequestParam(value = "idHoaDon", required = false) Integer idHD,
            Model model
    ) {
        if (idHD == null || idKH == null || idNV == null) {
            model.addAttribute("error", "Vui lòng lựa chọn hoá đơn để bán hàng");
        }
        System.out.println("ID khách hàng: " + idKH);
        System.out.println("ID nhân viên: " + idNV);
        System.out.println("ID hóa đơn: " + idHD);
        hoaDonService.thanhToanHoaDon(idHD, idKH, idNV);
        return "redirect:/admin/orders";
    }

}