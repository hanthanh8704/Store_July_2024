package com.example.asm.service.impl;

import com.example.asm.model.Category;
import com.example.asm.model.SanPham;
import com.example.asm.repository.CateogoryRepository;
import com.example.asm.repository.SanPhamRepository;
import com.example.asm.service.CategoryService;
import com.example.asm.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryImpl implements CategoryService {
    @Autowired
    private CateogoryRepository sanPhamRepository;


    @Override
    public List<Category> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    @Override
    public void saveSanPham(Category sanPham) {
        sanPhamRepository.save(sanPham);
    }

    @Override
    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findSanPhamById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    public Page<Category> findPageSanPham(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return sanPhamRepository.findAll(pageable);
    }

    @Override
    public List<Category> searchSanPham(String keyword) {
        return sanPhamRepository.searchSanPhamByTen(keyword);
    }

    @Override
    public Page<Category> SearchandPageSanPham(int pageNo, int pageSize, String keyword) {
        List list = this.searchSanPham(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<Category>(list, pageable, this.searchSanPham(keyword).size());
    }

    @Override
    public Category findByTen(String ten) {
        return sanPhamRepository.findByTen(ten);
    }
}
