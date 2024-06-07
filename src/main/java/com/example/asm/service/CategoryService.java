package com.example.asm.service;

import com.example.asm.model.Category;
import com.example.asm.model.SanPham;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllSanPham();

    void saveSanPham(Category sanPham);

    void deleteSanPham(Integer id);

    Optional<Category> findSanPhamById(Integer id);

    Page<Category> findPageSanPham(int pageNo, int pageSize);

    List<Category> searchSanPham(String keyword);

    // Hàm này dùng để kết hợp tìm kiếm và phân trang
    Page<Category> SearchandPageSanPham(int pageNo, int pageSize, String keyword);

    Category findByTen(String ten);
}
