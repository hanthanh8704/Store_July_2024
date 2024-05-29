package com.example.asm.controller;


import com.example.asm.service.HoaDonService;
import com.example.asm.service.KhachHangService;
import com.example.asm.service.NhanVienService;
import com.example.asm.service.SanPhamChiTietService;
import com.example.asm.util.LoginSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class BanHangController {
    @Autowired
    private LoginSession loginSession;
    private final HoaDonService hoaDonService;
    private final SanPhamChiTietService sanPhamChiTietService;
    private final NhanVienService nhanVienService;
    private final KhachHangService khachHangService;

    public BanHangController(HoaDonService hoaDonService, SanPhamChiTietService sanPhamChiTietService, NhanVienService nhanVienService, KhachHangService khachHangService) {
        this.hoaDonService = hoaDonService;
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
    public String getOrders(){
        if (!isLoggedIn()) {
            return "redirect:/login";
        } else {
            
            return "ban_hang/index";
        }
    }





//
//    @PostMapping("/create-order")
//    public String taoHoaDon(HoaDon hoaDon, Model model) {
//        hoaDonRepository.taoHoaDon(hoaDon);
//        return "redirect:/admin/orders";
//    }
//
//    // Hàm này dùng để click hóa đơn detal
//    @GetMapping("/billDetails/{id}")
//    public String getDetailBill(@PathVariable("id") Integer id,
//                                @RequestParam(defaultValue = "1") int page,
//                                @RequestParam(defaultValue = "3") int size,
//                                Model model) {
//        HoaDon hoaDon = hoaDonRepository.findById(id);
//        if (hoaDon == null) {
//            model.addAttribute("error", "Hóa đơn không tồn tại");
//        } else {
//            List<HoaDonChiTiet> chiTietList = hdctRepository.getHDCTByHDID(id);
//            model.addAttribute("hoadon", hoaDon);
//            model.addAttribute("hdct", chiTietList);
//            model.addAttribute("spct",sanPhamRepository.findAll());
//        }
//
//        // Tải lại danh sách sản phẩm chi tiết và hóa đơn
//        List<SanPhamChiTiet> list = this.sanPhamChiTietRepository.findAllPaging(page, size);
//        List<HoaDon> listHDPT = this.hoaDonRepository.findAll();
//        if (list.isEmpty()) {
//            model.addAttribute("error", "Bảng này trống");
//        } else {
//            model.addAttribute("spct", list);
//            model.addAttribute("hoadon", listHDPT);
//
//        }
//
//        model.addAttribute("currentPage", page);
//        model.addAttribute("pageSize", size);
//        model.addAttribute("totalPages", (int)
//                Math.ceil((double) sanPhamChiTietRepository.findAll().size() / size));
//
//        return "ban_hang/index";
//    }
//
//    // Hàm này dùng để tìm kiếm khách hàng bằng sđt
//    @GetMapping("/search")
//    public String searchKH(@RequestParam String sdt, Model model) {
//        KhachHang khachHang = banHangRepository.searchKHbySDT(sdt);
//        if (khachHang == null) {
//            model.addAttribute("error", "Khách hàng không tồn tại");
//        } else {
//            model.addAttribute("khachHang", khachHang);
//        }
//        return "redirect:/admin/orders";
//    }
//
//    // Hàm này dùng để thêm sản phẩm vào giỏ hàng
//    @PostMapping("/add/{id}")
//    public String addToCart(@RequestParam("spctId") int spctId,
//                            @RequestParam("hoaDonID") int hoaDonId,
//                            @RequestParam("quantity") int quantity) {
//        // Kiểm tra số lượng sản phẩm có đủ để thêm vào giỏ hàng hay không
//        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(spctId);
//        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId);
//        if (hoaDon != null && spct != null && spct.getSoLuong() >= quantity && quantity > 0) {
//            // Tạo mới hoá đơn chi tiết
//            HoaDonChiTiet hdct = new HoaDonChiTiet();
//            hdct.setHoaDonID(hoaDon);
//            hdct.setSpctID(spct);
//            hdct.setSoLuong(quantity);
//            hdct.setDonGia(spct.getDonGia());
//            hdct.setTrangThai(0); // Chờ thanh toán
//
//            // Thêm sản phẩm vào giỏ hàng và cập nhật tổng tiền của hóa đơn
//            if (banHangRepository.themSPVaoHD(hdct, spctId, quantity)) {
//                return "redirect:/admin/orders"; // Chuyển hướng người dùng đến trang giỏ hàng sau khi thêm sản phẩm thành công
//            } else {
//                return "redirect:/error"; // Xử lý thông báo lỗi nếu không thể thêm sản phẩm vào giỏ hàng
//            }
//        } else {
//            return "redirect:/error"; // Xử lý thông báo lỗi nếu số lượng hoặc sản phẩm không hợp lệ
//        }
//    }


}
