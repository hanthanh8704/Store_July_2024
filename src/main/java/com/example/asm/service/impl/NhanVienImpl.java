package com.example.asm.service.impl;

import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import com.example.asm.repository.NhanVienRepository;
import com.example.asm.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NhanVienImpl implements NhanVienService {
    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Override
    public List<NhanVien> getAllNhanVien() {
        return nhanVienRepository.findAll();
    }

    @Override
    public void saveNhanVien(NhanVien nhanVien) {
        nhanVienRepository.save(nhanVien);
    }

    @Override
    public void deleteNhanVien(Integer id) {
        nhanVienRepository.deleteById(id);
    }

    @Override
    public Optional<NhanVien> findNhanVien(Integer id) {
        return nhanVienRepository.findById(id);
    }

    @Override
    public Page<NhanVien> findNhanVien(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return nhanVienRepository.findAll(pageable);
    }

    @Override
    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienRepository.searchNhanVienByTen(keyword);
    }

    @Override
    public Page<NhanVien> SearchandPageNhanVien(int pageNo, int pageSize, String keyword) {
        List list = this.searchNhanVien(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<NhanVien>(list, pageable, this.searchNhanVien(keyword).size());
    }
}
