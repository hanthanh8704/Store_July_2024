package com.example.asm.service;

import com.example.asm.model.SanPham;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SanPhamService {
    List<SanPham> getAllSanPham();

    void saveSanPham(SanPham sanPham);

    void deleteSanPham(Integer id);

    Optional<SanPham> findSanPhamById(Integer id);

    Page<SanPham> findPageSanPham(int pageNo, int pageSize);

    List<SanPham> searchSanPham(String keyword);

    // Hàm này dùng để kết hợp tìm kiếm và phân trang
    Page<SanPham> SearchandPageSanPham(int pageNo, int pageSize, String keyword);

    SanPham findByTen(String ten);
}
