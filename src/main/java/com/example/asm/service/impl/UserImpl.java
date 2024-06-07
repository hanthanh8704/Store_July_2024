package com.example.asm.service.impl;

import com.example.asm.model.Category;
import com.example.asm.model.KichThuoc;
import com.example.asm.model.MauSac;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.repository.CateogoryRepository;
import com.example.asm.repository.KichThuocRepository;
import com.example.asm.repository.MauSacRepository;
import com.example.asm.repository.SanPhamChiTietRepository;
import com.example.asm.service.MauSacService;
import com.example.asm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
@Service
public class UserImpl implements UserService {
    @Autowired
    private CateogoryRepository categoriesRepository;
    @Autowired
    private SanPhamChiTietRepository productDetailRepository;

    @Autowired
    private MauSacRepository colorRepository;

    @Autowired
    private KichThuocRepository sizeRepository;

    @Override
    public List<Category> getListCategory() {
        return (List<Category>) categoriesRepository.findAll();
    }

    @Override
    public List<SanPhamChiTiet> getListProducdetail() {
        return (List<SanPhamChiTiet>) productDetailRepository.findAll();
    }

    @Override
    public List<SanPhamChiTiet> getListProductdetailByCreatedAt() {
        return null;
    }

    @Override
    public List<Category> getListCategoryByCreatedAt() {
        return null;
    }

    // Hàm này lấy ra sản phẩm bằng danh mục
    @Override
    public List<SanPhamChiTiet> getProductDetailByCategory(Integer id) {
        return productDetailRepository.getProductdetailByCate(id);
    }

    @Override
    public List<MauSac> getListColor() {
        return (List<MauSac>) colorRepository.findAll();
    }

    @Override
    public List<KichThuoc> getListSize() {
        return (List<KichThuoc>) sizeRepository.findAll();

    }

    @Override
    public List<SanPhamChiTiet> getProductDetailByColor(Integer id) {
        return productDetailRepository.findProductdetailByColor(id);
    }

    @Override
    public List<SanPhamChiTiet> getProductDetailBySize(Integer id) {
        return productDetailRepository.findProductdetailBySize(id);
    }

    @Override
    public Optional<SanPhamChiTiet> findProductdetailById(Integer id) {
        return productDetailRepository.findById(id);
    }

    @Override
    public List<SanPhamChiTiet> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productDetailRepository.findByDonGiaBetween(minPrice, maxPrice);
    }
}
