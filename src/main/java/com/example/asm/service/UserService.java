package com.example.asm.service;

import com.example.asm.model.Category;
import com.example.asm.model.KichThuoc;
import com.example.asm.model.MauSac;
import com.example.asm.model.SanPhamChiTiet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Category> getListCategory();

    List<SanPhamChiTiet> getListProducdetail();

    List<SanPhamChiTiet> getListProductdetailByCreatedAt();

    List<Category> getListCategoryByCreatedAt();

    // Lấy ra color
    List<MauSac> getListColor();

    // Lấy ra size
    List<KichThuoc> getListSize();
    // Lấy ra brand

    // Lấy ra sản phẩm bằng màu sắc
    List<SanPhamChiTiet> getProductDetailByColor(Integer id);

    // Lấy ra sản phẩm bằng danh mục
    List<SanPhamChiTiet> getProductDetailByCategory(Integer id);

    // Lấy ra sản phẩm bằng size
    List<SanPhamChiTiet> getProductDetailBySize(Integer id);

    // Lấy ra sản phẩm bằng id sp
    Optional<SanPhamChiTiet> findProductdetailById(Integer id);

    // Lọc theo khoảng giá
    List<SanPhamChiTiet> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    

}
