package com.example.asm.service;

import com.example.asm.model.NhanVien;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface NhanVienService {
    List<NhanVien> getAllNhanVien();
    void saveNhanVien(NhanVien nhanVien);
    void deleteNhanVien(Integer id);
    Optional<NhanVien> findNhanVien(Integer id);
    Page<NhanVien> findNhanVien(int pageNo, int pageSize);
    List<NhanVien> searchNhanVien(String keyword);
    Page<NhanVien> SearchandPageNhanVien(int pageNo, int pageSize, String keyword);
}
