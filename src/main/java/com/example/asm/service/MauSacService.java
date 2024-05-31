package com.example.asm.service;

import com.example.asm.model.MauSac;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MauSacService {
    List<MauSac> getAllMauSac();
    void saveMauSac(MauSac mauSac);

    void deleteMauSac(Integer id);

    Optional<MauSac> findMauSacById(Integer id);
    Page<MauSac> pageMauSac(int pageNo, int pageSize);
    List<MauSac> searchMauSac(String keyword);

    // Hàm này dùng để kết hợp tìm kiếm và phân trang
    Page<MauSac> SearchandPageMauSac(int pageNo, int pageSize, String keyword);

    MauSac findByTen(String ten);
}
