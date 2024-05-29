package com.example.asm.service;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HoaDonService {
    List<HoaDon> fillAllHoaDon();

    Optional<HoaDon> findHoaDonById(Integer id);

    List<HoaDon> searchHoaDon(String keyword);

    Page<HoaDon> pageHoaDon(int pageNo, int pageSize);

    List<HoaDonChiTiet> getHDCTByHDID(Integer hoaDonId);

    Page<HoaDon> SearchandPageHoaDon(int pageNo, int pageSize, String keyword);
}
