package com.example.asm.util;

import com.example.asm.model.KichThuoc;
import com.example.asm.model.MauSac;
import com.example.asm.model.SanPham;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.service.KichThuocService;
import com.example.asm.service.MauSacService;
import com.example.asm.service.SanPhamChiTietService;
import com.example.asm.service.SanPhamService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Repository
public class ExcelConfig {
    private final SanPhamService sanPhamService;
    private final MauSacService mauSacService;
    private final KichThuocService kichThuocService;
    private final SanPhamChiTietService sanPhamChiTietService;

    public ExcelConfig(SanPhamService sanPhamService, MauSacService mauSacService, KichThuocService kichThuocService, SanPhamChiTietService sanPhamChiTietService) {
        this.sanPhamService = sanPhamService;
        this.mauSacService = mauSacService;
        this.kichThuocService = kichThuocService;
        this.sanPhamChiTietService = sanPhamChiTietService;
    }

    public void exportExcelCTSP(HttpServletResponse response) throws Exception {

        List<SanPhamChiTiet> courses = sanPhamChiTietService.getAllSanPhamChiTiet();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("ImportExcel");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Mã");
        row.createCell(2).setCellValue("Tên Sản Phẩm");
        row.createCell(3).setCellValue("Tên Kích Thước");
        row.createCell(4).setCellValue("Tên Màu Sắc");
        row.createCell(5).setCellValue("Số Lượng");
        row.createCell(6).setCellValue("Đơn Giá");
        row.createCell(7).setCellValue("Trạng Thái");

        int dataRowIndex = 1;

        for (SanPhamChiTiet course : sanPhamChiTietService.getAllSanPhamChiTiet()) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(course.getId());
            dataRow.createCell(1).setCellValue(course.getMaSpct());
            dataRow.createCell(2).setCellValue(course.getSanPham().getTen());
            dataRow.createCell(3).setCellValue(course.getKichThuoc().getTen());
            dataRow.createCell(4).setCellValue(course.getMauSac().getTen());
            dataRow.createCell(5).setCellValue(course.getSoLuong());
            dataRow.createCell(6).setCellValue(course.getDonGia().doubleValue()); // Corrected line
            dataRow.createCell(7).setCellValue(course.getTrangThai());
            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    public boolean importExcelSPCT(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Bỏ qua dòng tiêu đề
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();

                // Set ID nếu bạn muốn cập nhật hoặc để null để tạo mới
                sanPhamChiTiet.setId((int) row.getCell(0).getNumericCellValue());

                // Set các thuộc tính khác từ Excel
                sanPhamChiTiet.setMaSpct(row.getCell(1).getStringCellValue());

                String sanPhamTen = row.getCell(2).getStringCellValue();
                SanPham sanPham = sanPhamService.findByTen(sanPhamTen);
                if (sanPham == null) {
                    System.out.println("Không tìm thấy sản phẩm: " + sanPhamTen);
                    return false;
                }
                sanPhamChiTiet.setSanPham(sanPham);

                String kichThuocTen = row.getCell(3).getStringCellValue();
                KichThuoc kichThuoc = kichThuocService.findByTen(kichThuocTen);
                if (kichThuoc == null) {
                    System.out.println("Không tìm thấy kích thước: " + kichThuocTen);
                    return false;
                }
                sanPhamChiTiet.setKichThuoc(kichThuoc);

                String mauSacTen = row.getCell(4).getStringCellValue();
                MauSac mauSac = mauSacService.findByTen(mauSacTen);
                if (mauSac == null) {
                    System.out.println("Không tìm thấy màu sắc: " + mauSacTen);
                    return false;
                }
                sanPhamChiTiet.setMauSac(mauSac);

                int soLuong = (int) row.getCell(5).getNumericCellValue();
                if (soLuong <= 0) {
                    System.out.println("Số lượng phải lớn hơn 0");
                    return false;
                }
                sanPhamChiTiet.setSoLuong(soLuong);

                double donGiaValue = row.getCell(6).getNumericCellValue();
                if (donGiaValue <= 0) {
                    System.out.println("Đơn giá phải lớn hơn 0");
                    return false;
                }
                sanPhamChiTiet.setDonGia(BigDecimal.valueOf(donGiaValue));

                int trangThai = (int) row.getCell(7).getNumericCellValue();
                // Kiểm tra trạng thái nếu không hợp lệ thì trả về false
                if (trangThai != 0 && trangThai != 1) {
                    System.out.println("Trạng thái không hợp lệ");
                    return false;
                }
                sanPhamChiTiet.setTrangThai(trangThai);

                // Kiểm tra xem sản phẩm chi tiết đã tồn tại trong cơ sở dữ liệu chưa
                SanPhamChiTiet existingSPCT = sanPhamChiTietService.findSPCTById(sanPhamChiTiet.getId());
                if (existingSPCT != null) {
                    // Nếu tồn tại, thực hiện cập nhật
                    sanPhamChiTietService.saveSanPhamChiTiet(sanPhamChiTiet);
                } else {
                    // Nếu không tồn tại, thực hiện thêm mới
                    sanPhamChiTietService.saveSanPhamChiTiet(sanPhamChiTiet);
                }
            }
            return true; // Return true if import is successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if import fails
        }
    }



}
