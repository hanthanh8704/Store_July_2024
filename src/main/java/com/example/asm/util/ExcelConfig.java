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
                    throw new RuntimeException("Không tìm thấy sản phẩm: " + sanPhamTen);
                }
                sanPhamChiTiet.setSanPham(sanPham);

                String kichThuocTen = row.getCell(3).getStringCellValue();
                KichThuoc kichThuoc = kichThuocService.findByTen(kichThuocTen);
                if (kichThuoc == null) {
                    throw new RuntimeException("Không tìm thấy kích thước: " + kichThuocTen);
                }
                sanPhamChiTiet.setKichThuoc(kichThuoc);

                String mauSacTen = row.getCell(4).getStringCellValue();
                MauSac mauSac = mauSacService.findByTen(mauSacTen);
                if (mauSac == null) {
                    throw new RuntimeException("Không tìm thấy màu sắc: " + mauSacTen);
                }
                sanPhamChiTiet.setMauSac(mauSac);

                sanPhamChiTiet.setSoLuong((int) row.getCell(5).getNumericCellValue());
                sanPhamChiTiet.setDonGia(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
                sanPhamChiTiet.setTrangThai((int) row.getCell(7).getNumericCellValue());

                // Lưu sản phẩm chi tiết vào cơ sở dữ liệu
                sanPhamChiTietService.saveSanPhamChiTiet(sanPhamChiTiet);
            }
            return true; // Return true if import is successful
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if import fails
        }
    }

}
