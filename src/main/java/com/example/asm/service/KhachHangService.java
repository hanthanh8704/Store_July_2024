package com.example.asm.service;

import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPham;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHang> getAllKhachHang();

    void saveKhachHang(KhachHang khachHang);

    void deleteKhachHang(Integer id);

    Optional<KhachHang> findKhachHangById(Integer id);
    KhachHang findKhachHangById2(Integer id);
    KhachHang findKhachHangByTen(String ten);
    KhachHang findKhachHangBySdt(String sdt);

    Page<KhachHang> findPageKhachHang(int pageNo, int pageSize);

    List<KhachHang> searchKhachHang(String keyword);

    // Hàm này dùng để kết hợp tìm kiếm và phân trang
    Page<KhachHang> SearchandPageKhachHang(int pageNo, int pageSize, String keyword);

    KhachHang findKhachHangByUsernameAndPassword(String username, String password);

}
