//package com.example.asm.util;
//
//import com.example.asm.entity.KichThuoc;
//import com.example.asm.entity.MauSac;
//import com.example.asm.entity.SanPham;
//import com.example.asm.entity.SanPhamChiTiet;
//import com.example.asm.repon.KichThuocRepository;
//import com.example.asm.repon.MauSacRepository;
//import com.example.asm.repon.SanPhamChiTietRepository;
//import com.example.asm.repon.SanPhamRepository;
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//
//@Repository
//public class ExcelConfig {
//    @Autowired
//    SanPhamRepository sanPhamRepository;
//
//    @Autowired
//    private MauSacRepository mauSacRepository;
//
//    @Autowired
//    private KichThuocRepository kichThuocRepository;
//
//    @Autowired
//    private SanPhamChiTietRepository sanPhamChiTietRepository;
//
//    public void exportExcel(HttpServletResponse response) throws Exception {
//
//        List<SanPham> courses = sanPhamRepository.findAll();
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("ImportExcel");
//        HSSFRow row = sheet.createRow(0);
//
//        row.createCell(0).setCellValue("ID");
//        row.createCell(1).setCellValue("Mã");
//        row.createCell(2).setCellValue("Tên");
//        row.createCell(3).setCellValue("Trạng Thái");
//
//        int dataRowIndex = 1;
//
//        for (SanPham course : sanPhamRepository.findAll()) {
//            HSSFRow dataRow = sheet.createRow(dataRowIndex);
//            dataRow.createCell(0).setCellValue(course.getId());
//            dataRow.createCell(1).setCellValue(course.getMa());
//            dataRow.createCell(2).setCellValue(course.getTen());
//            dataRow.createCell(3).setCellValue(course.getTrangThai());
//            dataRowIndex++;
//        }
//        ServletOutputStream ops = response.getOutputStream();
//        workbook.write(ops);
//        workbook.close();
//        ops.close();
//    }
//
//    public boolean importExcel(MultipartFile file) throws IOException {
//        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            if (rowIterator.hasNext()) {
//                rowIterator.next();
//            }
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                SanPham sanPham = new SanPham();
//
//                sanPham.setId((int) row.getCell(0).getNumericCellValue()); // ID
//                sanPham.setMa(row.getCell(1).getStringCellValue()); // Mã
//                sanPham.setTen(row.getCell(2).getStringCellValue()); // Tên
//                sanPham.setTrangThai((int) row.getCell(3).getNumericCellValue()); // Trạng thái
//
//                // Add sản phẩm vừa import vào list
//                sanPhamRepository.create(sanPham);
//            }
//            return true; // Return true if import is successful
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false; // Return false if import fails
//        }
//    }
//
//
//    public void exportExcelCTSP(HttpServletResponse response) throws Exception {
//
//        List<SanPhamChiTiet> courses = sanPhamChiTietRepository.findAll();
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("ImportExcel");
//        HSSFRow row = sheet.createRow(0);
//
//        row.createCell(0).setCellValue("ID");
//        row.createCell(1).setCellValue("Mã");
//        row.createCell(2).setCellValue("Tên Sản Phẩm");
//        row.createCell(3).setCellValue("Tên Kích Thước");
//        row.createCell(4).setCellValue("Tên Màu Sắc");
//        row.createCell(5).setCellValue("Số Lượng");
//        row.createCell(6).setCellValue("Đơn Giá");
//        row.createCell(7).setCellValue("Trạng Thái");
//
//        int dataRowIndex = 1;
//
//        for (SanPhamChiTiet course : sanPhamChiTietRepository.findAll()) {
//            HSSFRow dataRow = sheet.createRow(dataRowIndex);
//            dataRow.createCell(0).setCellValue(course.getId());
//            dataRow.createCell(1).setCellValue(course.getMa());
//            dataRow.createCell(2).setCellValue(course.getSanPham().getTen());
//            dataRow.createCell(3).setCellValue(course.getKichThuoc().getTen());
//            dataRow.createCell(4).setCellValue(course.getMauSac().getTen());
//            dataRow.createCell(5).setCellValue(course.getSoLuong());
//            dataRow.createCell(6).setCellValue(course.getDonGia());
//            dataRow.createCell(7).setCellValue(course.getTrangThai());
//            dataRowIndex++;
//        }
//        ServletOutputStream ops = response.getOutputStream();
//        workbook.write(ops);
//        workbook.close();
//        ops.close();
//    }
//
//    public boolean importExcelSPCT(MultipartFile file) throws IOException {
//        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            if (rowIterator.hasNext()) {
//                rowIterator.next(); // Bỏ qua dòng tiêu đề
//            }
//
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
//
//                // Set ID nếu bạn muốn cập nhật hoặc để null để tạo mới
//                sanPhamChiTiet.setId((int) row.getCell(0).getNumericCellValue());
//
//                // Set các thuộc tính khác từ Excel
//                sanPhamChiTiet.setMa(row.getCell(1).getStringCellValue());
//
//                String sanPhamTen = row.getCell(2).getStringCellValue();
//                SanPham sanPham = sanPhamRepository.findByTen(sanPhamTen);
//                if (sanPham == null) {
//                    throw new RuntimeException("Không tìm thấy sản phẩm: " + sanPhamTen);
//                }
//                sanPhamChiTiet.setSanPham(sanPham);
//
//                String kichThuocTen = row.getCell(3).getStringCellValue();
//                KichThuoc kichThuoc = kichThuocRepository.findByTen(kichThuocTen);
//                if (kichThuoc == null) {
//                    throw new RuntimeException("Không tìm thấy kích thước: " + kichThuocTen);
//                }
//                sanPhamChiTiet.setKichThuoc(kichThuoc);
//
//                String mauSacTen = row.getCell(4).getStringCellValue();
//                MauSac mauSac = mauSacRepository.findByTen(mauSacTen);
//                if (mauSac == null) {
//                    throw new RuntimeException("Không tìm thấy màu sắc: " + mauSacTen);
//                }
//                sanPhamChiTiet.setMauSac(mauSac);
//
//                sanPhamChiTiet.setSoLuong((int) row.getCell(5).getNumericCellValue());
//                sanPhamChiTiet.setDonGia(row.getCell(6).getNumericCellValue());
//                sanPhamChiTiet.setTrangThai((int) row.getCell(7).getNumericCellValue());
//
//                // Lưu sản phẩm chi tiết vào cơ sở dữ liệu
//                sanPhamChiTietRepository.create(sanPhamChiTiet);
//            }
//            return true; // Return true if import is successful
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false; // Return false if import fails
//        }
//    }
//
//}
