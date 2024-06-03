package com.example.asm.controller;


import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.service.*;
import com.example.asm.util.LoginSession;
import com.example.asm.util.PDFGenerator;
import com.example.asm.util.Timestamp;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private final PDFGenerator pdfGenerator;

    public BanHangController(HoaDonService hoaDonService, HoaDonChiTietService hoaDonChiTietService, SanPhamChiTietService sanPhamChiTietService, NhanVienService nhanVienService, KhachHangService khachHangService, PDFGenerator pdfGenerator) {
        this.hoaDonService = hoaDonService;
        this.hoaDonChiTietService = hoaDonChiTietService;
        this.sanPhamChiTietService = sanPhamChiTietService;
        this.nhanVienService = nhanVienService;
        this.khachHangService = khachHangService;
        this.pdfGenerator = pdfGenerator;
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
                                        @RequestParam("soLuong") Integer soLuong, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("IDSPCT : " + spctId);
        System.out.println("ID : " + id);
        System.out.println("Số Lượng : " + soLuong);

        // Kiểm tra số lượng tồn trong giỏ hàng
        Integer soLuongSPCT = sanPhamChiTietService.getSoLuongTonByIDCTSP(spctId);
        Integer soLuongTrongGio = sanPhamChiTietService.getSoLuongTrongGioHangByIdCTSP(spctId);
        int soLuongTon = soLuongTrongGio + soLuongSPCT;

        // Kiểm tra nếu số lượng nhỏ hơn 1 hoặc lớn hơn tồn kho trong giỏ hàng
        if (soLuong < 0) {
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
        return saveForm(id);
    }




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
        return saveForm(idHoaDon);
    }

    // Hàm này dùng để update sản phẩm trong giỏ hàng
//    @PostMapping("/updateSPCT/{idCTSP}")
//    public String updateSanPhamTrongGioHang(
//            @RequestParam("id") Integer idHDCT,
//            @PathVariable("idCTSP") Integer spctID,
//            @RequestParam("idHoaDon") Integer idHD,
//            @RequestParam("soLuongTrongGio") Integer soLuongTrongGioHang,
//            @RequestParam("soLuongThayDoi") Integer soLuongThayDoi,
//            Model model
//    ) {
//        if (idHD == null) {
//            model.addAttribute("error", "Bạn chưa lựa chọn hóa đơn id");
//        } else {
//            System.out.println("ID Chi tiết sản phẩm: " + spctID);
//            System.out.println("ID hóa đơn: " + idHD);
//            System.out.println("ID hóa đơn chi tiết: " + idHDCT);
//            System.out.println("Số lượng sản phẩm trong giỏ: " + soLuongTrongGioHang);
//            System.out.println("Số lượng sản phẩm thay đổi: " + soLuongThayDoi);
//
//            hoaDonService.capNhatSanPhamTrongGioHang(spctID, idHDCT, soLuongThayDoi, soLuongTrongGioHang);
//        }
//        return saveForm(idHD);
//    }

    // Hàm này dùng để update sản phẩm trong giỏ hàng
    @PostMapping("/updateSPCT/{idCTSP}")
    public String updateSanPhamTrongGioHang(
            @RequestParam("id") Integer idHDCT,
            @PathVariable("idCTSP") Integer spctID,
            @RequestParam("idHoaDon") Integer idHD,
            @RequestParam("soLuongTrongGio") Integer soLuongTrongGioHang,
            @RequestParam("soLuongThayDoi") String soLuongThayDoiStr,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (idHD == null) {
            redirectAttributes.addFlashAttribute("error2", "Bạn chưa lựa chọn hóa đơn id");
            return "redirect:/admin/orders";
        } else {
            Integer soLuongThayDoi = null;
            try {
                soLuongThayDoi = Integer.parseInt(soLuongThayDoiStr);

                if (soLuongThayDoi < 0) {
                    redirectAttributes.addFlashAttribute("error2", "Số lượng thay đổi phải là số và không được âm");
                    saveForm(idHD);
                }

                if (soLuongThayDoi == 0) {
                    redirectAttributes.addFlashAttribute("error2", "Số lượng thay đổi không thể bằng 0");
                    saveForm(idHD);
                }

                // Kiểm tra số lượng tồn trong bảng SanPhamChiTiet
                Integer soLuongTonSPCT = sanPhamChiTietService.getSoLuongTonByIDCTSP(spctID);
                if (soLuongTonSPCT == null) {
                    redirectAttributes.addFlashAttribute("error2", "Không tìm thấy sản phẩm chi tiết với ID: " + spctID);
                    saveForm(idHD);
                }

                // Kiểm tra nếu số lượng thay đổi lớn hơn số lượng tồn
                if (soLuongThayDoi > soLuongTonSPCT) {
                    redirectAttributes.addFlashAttribute("error2", "Số lượng thay đổi lớn hơn số lượng tồn");
                    saveForm(idHD);
                }

                System.out.println("ID Chi tiết sản phẩm: " + spctID);
                System.out.println("ID hóa đơn: " + idHD);
                System.out.println("ID hóa đơn chi tiết: " + idHDCT);
                System.out.println("Số lượng sản phẩm trong giỏ: " + soLuongTrongGioHang);
                System.out.println("Số lượng sản phẩm thay đổi: " + soLuongThayDoi);

                hoaDonService.capNhatSanPhamTrongGioHang(spctID, idHDCT, soLuongThayDoi, soLuongTrongGioHang);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("error2", "Số lượng thay đổi phải là số hợp lệ");
                return "redirect:/admin/orders";
            }
        }
        return "redirect:/admin/orders";
    }


    // Hàm này dùng để thanh toán hóa đơn
//    @GetMapping("/thanhToanHD")
//    public String thanhToan(
//            @RequestParam(value = "idKH", required = false) Integer idKH,
//            @RequestParam(value = "idNV", required = false) Integer idNV,
//            @RequestParam(value = "idHoaDon", required = false) Integer idHD,
//            Model model
//    ) {
//        if (idHD == null || idKH == null || idNV == null) {
//            model.addAttribute("error", "Vui lòng lựa chọn hoá đơn để bán hàng");
//        }
//
//        System.out.println("ID khách hàng: " + idKH);
//        System.out.println("ID nhân viên: " + idNV);
//        System.out.println("ID hóa đơn: " + idHD);
//        hoaDonService.thanhToanHoaDon(idHD, idKH, idNV);
//        return "redirect:/admin/orders";
//    }

    // Hàm này dùng để thanh toán hóa đơn
    // Hàm này dùng để thanh toán hóa đơn
    @GetMapping("/thanhToanHD")
    public String thanhToan(
            @RequestParam(value = "idKH", required = false) Integer idKH,
            @RequestParam(value = "idNV", required = false) Integer idNV,
            @RequestParam(value = "idHoaDon", required = false) Integer idHD,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (idHD == null || idKH == null || idNV == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng lựa chọn hoá đơn để bán hàng");
            return "redirect:/admin/orders";
        }

        // Lấy tổng tiền của hóa đơn
        BigDecimal tongTien = hoaDonService.getTongTienByIdHoaDon(idHD);

        // Kiểm tra nếu tổng tiền bằng null hoặc bằng 0
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) == 0) {
            redirectAttributes.addFlashAttribute("error", "Tổng tiền của hoá đơn phải lớn hơn 0 để thanh toán");
            return "redirect:/admin/orders";
        }

        System.out.println("ID khách hàng: " + idKH);
        System.out.println("ID nhân viên: " + idNV);
        System.out.println("ID hóa đơn: " + idHD);
        hoaDonService.thanhToanHoaDon(idHD, idKH, idNV);
        return "redirect:/admin/orders";
    }


    // Hàm này dùng để gen file pdf
    @GetMapping("/billPdf/{id}")
    public void exportToPDF(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        HoaDon bill = hoaDonService.findById(id);

        if (bill == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bill status for PDF generation.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Tạo tiêu đề
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph("The HabitShop", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Thông tin hóa đơn
            Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String nowS = formatter.format(new Date());

            document.add(new Paragraph("Ma hoa đon: " + bill.getId(), infoFont));
            document.add(new Paragraph("Ten khach hang: " + bill.getKhachHang().getTen(), infoFont));
            document.add(new Paragraph("Ngay tao hoa đon: " + bill.getNgayMuaHang(), infoFont));
            document.add(new Paragraph("Ngay xuat hoa đon: " + nowS, infoFont));
            document.add(new Paragraph("Ma nguoi tao hoa đon: " + bill.getNhanVien().getMaNV(), infoFont));
            document.add(new Paragraph("Ten nguoi xuat hoa đon: " + bill.getNhanVien().getTen(), infoFont));
            document.add(new Paragraph("Tong tien của hoa đon: " + bill.getTongTien(), infoFont));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Danh sach san pham đa mua"));
            document.add(new Paragraph("\n"));

            // Tạo bảng sản phẩm
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Tiêu đề bảng
            Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.DARK_GRAY);
            addTableHeader(table, headerFont);

            // Dữ liệu bảng
            Font dataFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            List<HoaDonChiTiet> listHDCT = hoaDonService.getHDCTByHDID(id);
            addTableData(table, listHDCT, dataFont);

            // Thêm bảng vào document
            document.add(table);

            // Thêm tổng tiền vào cuối bảng
            PdfPCell totalCell = new PdfPCell(new Phrase("Thành tiền: " + bill.getTongTien(), headerFont));
            totalCell.setColspan(8);
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(totalCell);

            // Thêm footer
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK);
            Paragraph footer = new Paragraph("CHUC QUY KHACH MUA SAM VUI VE!!!!", footerFont);
            document.add(footer);
            document.add(new Paragraph("\n"));

            // Tạo footer bằng bảng
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setWidthPercentage(100);
            footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell footerCell = new PdfPCell(new Phrase("----------------------------- CAM ON QUY KHACH ---------------------------", footerFont));
            footerTable.addCell(footerCell);
            document.add(footerTable);

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private boolean isValidBillStatus(String trangThai) {
        return !trangThai.equalsIgnoreCase("Đã huỷ") && !trangThai.equalsIgnoreCase("Chờ thanh toán");
    }

    private void addTableHeader(PdfPTable table, Font headerFont) {
        String[] headers = {"Tên sản phẩm", "Size", "Màu sắc", "Đơn giá", "Số lượng", "Thành tiền"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addTableData(PdfPTable table, List<HoaDonChiTiet> listHDCT, Font dataFont) {
        for (HoaDonChiTiet detail : listHDCT) {
            table.addCell(new PdfPCell(new Phrase(detail.getSanPhamChiTiet().getSanPham().getTen(), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSanPhamChiTiet().getKichThuoc()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSanPhamChiTiet().getMauSac()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getDonGia()), dataFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSoLuong()), dataFont)));
            BigDecimal thanhTien = detail.getDonGia().multiply(BigDecimal.valueOf(detail.getSoLuong()));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(thanhTien), dataFont)));
        }
    }

    // Hàm chuyển hướng tương tự saveForm trong Spring Boot
    private String saveForm(Integer idHoaDon) {
        return "redirect:/admin/detail-order/" + idHoaDon;
    }

}