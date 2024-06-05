package com.example.asm.controller;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.service.HoaDonService;
import com.example.asm.util.LoginSession;
import com.example.asm.util.PDFGenerator;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class HoaDonController {
    @Autowired
    private LoginSession loginSession;
    private final HoaDonService hoaDonService;
    private final PDFGenerator pdfGenerator;

    public HoaDonController(HoaDonService hoaDonService, PDFGenerator pdfGenerator) {
        this.hoaDonService = hoaDonService;
        this.pdfGenerator = pdfGenerator;
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

    // Hàm này dùng để xuất file pdf
//    @GetMapping("/billPdf/{id}")
//    public void exportToPDF(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
//        HoaDon bill = hoaDonService.findById(id);
//        System.out.println("Đây là hóa đơn dòng 89 : " + bill);
//
//        if (bill == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bill status for PDF generation.");
//            return;
//        }
//
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=bill_" + id + ".pdf");
//
//        Document document = new Document();
//        try {
//            PdfWriter.getInstance(document, response.getOutputStream());
//            document.open();
//
//            // Tạo tiêu đề
//            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD, BaseColor.BLACK);
//            Paragraph title = new Paragraph("The HabitShop", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(new Paragraph("\n"));
//
//            // Thông tin hóa đơn
//            Font infoFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.DARK_GRAY);
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String nowS = formatter.format(new Date());
//
//            document.add(new Paragraph("Ma hoa đon: " + bill.getId(), infoFont));
//            document.add(new Paragraph("Ten khach hang: " + bill.getKhachHang().getTen(), infoFont));
//            document.add(new Paragraph("Ngay tao hoa đon: " + bill.getNgayMuaHang(), infoFont));
//            document.add(new Paragraph("Ngay xuat hoa đon: " + nowS, infoFont));
//            document.add(new Paragraph("Ma nguoi tao hoa đon: " + bill.getNhanVien().getMaNV(), infoFont));
//            document.add(new Paragraph("Ten nguoi xuat hoa đon: " + bill.getNhanVien().getTen(), infoFont));
//            document.add(new Paragraph("Tong tien của hoa đon: " + bill.getTongTien(), infoFont));
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("Danh sach san pham da mua"));
//            document.add(new Paragraph("\n"));
//
//            // Tạo bảng sản phẩm
//            PdfPTable table = new PdfPTable(8);
//            table.setWidthPercentage(100);
//            table.setSpacingBefore(10f);
//            table.setSpacingAfter(10f);
//
//            // Tiêu đề bảng
//            Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.DARK_GRAY);
//            addTableHeader(table, headerFont);
//
//            // Dữ liệu bảng
//            Font dataFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.DARK_GRAY);
//            List<HoaDonChiTiet> listHDCT = hoaDonService.getHDCTByHDID(id);
//            System.out.println("Dòng 140" + listHDCT);
//            addTableData(table, listHDCT, dataFont);
//
//            // Thêm bảng vào document
//            document.add(table);
//
//            // Thêm tổng tiền vào cuối bảng
//            PdfPCell totalCell = new PdfPCell(new Phrase("Thành tiền: " + bill.getTongTien(), headerFont));
//            totalCell.setColspan(8);
//            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            table.addCell(totalCell);
//
//            // Thêm footer
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph("\n"));
//            Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, BaseColor.BLACK);
//            Paragraph footer = new Paragraph("CHUC QUY KHACH MUA SAM VUI VE!!!!", footerFont);
//            document.add(footer);
//            document.add(new Paragraph("\n"));
//
//            // Tạo footer bằng bảng
//            PdfPTable footerTable = new PdfPTable(1);
//            footerTable.setWidthPercentage(100);
//            footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//            PdfPCell footerCell = new PdfPCell(new Phrase("------------ CAM ON QUY KHACH -------------", footerFont));
//            footerTable.addCell(footerCell);
//            document.add(footerTable);
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//    }
//
//
//    private boolean isValidBillStatus(String trangThai) {
//        return !trangThai.equalsIgnoreCase("Đã huỷ") && !trangThai.equalsIgnoreCase("Chờ thanh toán");
//    }
//
//    private void addTableHeader(PdfPTable table, Font headerFont) {
//        String[] headers = {"Tên sản phẩm", "Size", "Màu sắc", "Đơn giá", "Số lượng", "Thành tiền"};
//        for (String header : headers) {
//            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
//            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
//            table.addCell(cell);
//        }
//    }
//
//    private void addTableData(PdfPTable table, List<HoaDonChiTiet> listHDCT, Font dataFont) {
//        for (HoaDonChiTiet detail : listHDCT) {
//            table.addCell(new PdfPCell(new Phrase(detail.getSanPhamChiTiet().getSanPham().getTen(), dataFont)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSanPhamChiTiet().getKichThuoc()), dataFont)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSanPhamChiTiet().getMauSac()), dataFont)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getDonGia()), dataFont)));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getSoLuong()), dataFont)));
//            BigDecimal thanhTien = detail.getDonGia().multiply(BigDecimal.valueOf(detail.getSoLuong()));
//            table.addCell(new PdfPCell(new Phrase(String.valueOf(thanhTien), dataFont)));
//        }
//    }


}
