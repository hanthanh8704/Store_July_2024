package com.example.asm.service;

import com.example.asm.model.HoaDon;
import com.example.asm.model.SanPham;
import com.example.asm.model.SanPhamChiTiet;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface SanPhamChiTietService {
    List<SanPhamChiTiet> getAllSanPhamChiTiet();

    void saveSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet);

    void deleteSanPhamChiTiet(Integer id);

    Optional<SanPhamChiTiet> findSanPhamChiTietById(Integer id);

    Page<SanPhamChiTiet> findPageSanPhamChiTiet(int pageNo, int pageSize);

    List<SanPhamChiTiet> searchSanPhamChiTiet(String keyword);

    // Hàm này dùng để kết hợp tìm kiếm và phân trang
    Page<SanPhamChiTiet> SearchandPageSanPhamChiTiet(int pageNo, int pageSize, String keyword);

    int getSoLuongTrongGioHangByIdCTSP(Integer idCTSP);

    int getSoLuongTonByIDCTSP(Integer idCTSP);
     boolean checkAddGioHang(int idCtsp, int soLuongThemVaoGio);
    SanPhamChiTiet findSPCTById(Integer id);
}
