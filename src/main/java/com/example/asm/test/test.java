//package com.example.asm.test;
//
//import com.example.asm.entity.NhanVien;
//import com.example.asm.repository.NhanVienRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class test {
//    public static void main(String[] args) {
//        // Tạo danh sách nhân viên mẫu để kiểm thử
//        List<NhanVien> nhanVienList = new ArrayList<>();
//        nhanVienList.addnew NhanVien(1, "Admin", "NV001", "Admin", "123", 1));
//        nhanVienList.add(new NhanVien(2, "Hán Thanh", "NV002", "HanThanh", "123", 1));
//        nhanVienList.add(new NhanVien(3, "July", "NV003", "July", "123", 1));
//
//        // Tạo một đối tượng NhanVienRepository để tìm kiếm
//        NhanVienRepository repository = new NhanVienRepository();
//
//        // Kiểm thử phương thức findByUsernameAndPassword
//        String testUsername = "Admin";
//        String testPassword = "123";
//        NhanVien foundNhanVien = repository.findByUsernameAndPassword(testUsername, testPassword);
//
//        if (foundNhanVien != null) {
//            System.out.println("Nhân viên được tìm thấy:");
//            System.out.println("Username: " + foundNhanVien.getUsername());
//            System.out.println("Password: " + foundNhanVien.getPassword());
//        } else {
//            System.out.println("Không tìm thấy nhân viên với username và password đã cho.");
//        }
//    }
//}
