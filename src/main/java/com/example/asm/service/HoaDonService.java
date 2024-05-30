package com.example.asm.service;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HoaDonService {
    List<HoaDon> fillAllHoaDon();

    void saveHoaDon(HoaDon hoaDon);

    Optional<HoaDon> findHoaDonById(Integer id);

    HoaDon findById(Integer id);

    List<HoaDon> searchHoaDon(String keyword);

    Page<HoaDon> pageHoaDon(int pageNo, int pageSize);

    List<HoaDonChiTiet> getHDCTByHDID(Integer hoaDonId);

    Page<HoaDon> SearchandPageHoaDon(int pageNo, int pageSize, String keyword);

    BigDecimal getTongTienByIdHoaDon(Integer idHoaDon);

    BigDecimal calculateTotalAmount();

    void themSanPhamVaoHoaDonChiTiet(Integer idHD, Integer idCTSP, Integer soLuong);

    void xoaSanPhamKhoiGioHang(Integer hdct, Integer idCTSP);

    void capNhatSanPhamTrongGioHang(Integer idCTSP, Integer idHDCT, Integer soLuongThayDoi, Integer soLuongTrongGio);

    void thanhToanHoaDon(Integer idHD, Integer idKH, Integer idNV);

    boolean kiemTraSanPhamTonTaiTrongGioHang(Integer id, Integer spctId);

    void congDonSoLuongSanPhamTrongGioHang(Integer id, Integer spctId, Integer soLuong);
}
