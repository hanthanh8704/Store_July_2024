package com.example.asm.service.impl;

import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPham;
import com.example.asm.repository.KhachHangRepository;
import com.example.asm.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KhachHangImpl implements KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Override
    public List<KhachHang> getAllKhachHang() {
        return khachHangRepository.findAll();
    }

    @Override
    public void saveKhachHang(KhachHang khachHang) {
        khachHangRepository.save(khachHang);
    }

    @Override
    public void deleteKhachHang(Integer id) {
        khachHangRepository.deleteById(id);
    }

    @Override
    public Optional<KhachHang> findKhachHangById(Integer id) {
        return khachHangRepository.findById(id);
    }

    @Override
    public KhachHang findKhachHangById2(Integer id) {
        return khachHangRepository.findKhachHangByIdLike(id);
    }

    @Override
    public KhachHang findKhachHangByTen(String ten) {
        return (KhachHang) khachHangRepository.searchKhachHangByTen(ten);
    }

    @Override
    public KhachHang findKhachHangBySdt(String sdt) {
      return khachHangRepository.findKhachHangBySdtLike(sdt);
    }


    @Override
    public Page<KhachHang> findPageKhachHang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return khachHangRepository.findAll(pageable);
    }

    @Override
    public List<KhachHang> searchKhachHang(String keyword) {
        return khachHangRepository.searchKhachHangByTen(keyword);
    }

    @Override
    public Page<KhachHang> SearchandPageKhachHang(int pageNo, int pageSize, String keyword) {
        List list = this.searchKhachHang(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<KhachHang>(list, pageable, this.searchKhachHang(keyword).size());
    }
}
