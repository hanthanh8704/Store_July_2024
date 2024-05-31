package com.example.asm.service.impl;

import com.example.asm.model.SanPham;
import com.example.asm.repository.SanPhamRepository;
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
public class SanPhamImpl implements SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;


    @Override
    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll();
    }

    @Override
    public void saveSanPham(SanPham sanPham) {
        sanPhamRepository.save(sanPham);
    }

    @Override
    public void deleteSanPham(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    @Override
    public Optional<SanPham> findSanPhamById(Integer id) {
        return sanPhamRepository.findById(id);
    }

    @Override
    public Page<SanPham> findPageSanPham(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return sanPhamRepository.findAll(pageable);
    }

    @Override
    public List<SanPham> searchSanPham(String keyword) {
        return sanPhamRepository.searchSanPhamByTen(keyword);
    }

    @Override
    public Page<SanPham> SearchandPageSanPham(int pageNo, int pageSize, String keyword) {
        List list = this.searchSanPham(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<SanPham>(list, pageable, this.searchSanPham(keyword).size());
    }

    @Override
    public SanPham findByTen(String ten) {
        return sanPhamRepository.findByTen(ten);
    }
}
