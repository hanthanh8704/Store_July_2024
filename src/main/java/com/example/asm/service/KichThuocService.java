package com.example.asm.service;

import com.example.asm.model.KichThuoc;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface KichThuocService {
    List<KichThuoc> getAllKichThuoc();

    void saveKichThuoc(KichThuoc kichThuoc);
    void deleteKichThuoc(Integer id);
    Optional<KichThuoc> findKichThuoc(Integer id);
    Page<KichThuoc> pageKichThuoc(int pageNo, int pageSize);
    List<KichThuoc> searchKichThuoc(String keyword);
    Page<KichThuoc> SearchandPageKichThuoc(int pageNo, int pageSize, String keyword);

}
