package com.example.asm.service.impl;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.KhachHang;
import com.example.asm.repository.HoaDonRepository;
import com.example.asm.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoaDonImpl implements HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public List<HoaDon> fillAllHoaDon() {
        return hoaDonRepository.findAll();
    }

    @Override
    public Optional<HoaDon> findHoaDonById(Integer id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    public List<HoaDon> searchHoaDon(String keyword) {
        return hoaDonRepository.searchHoaDonByTen(keyword);
    }

    @Override
    public Page<HoaDon> pageHoaDon(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return hoaDonRepository.findAll(pageable);
    }

    @Override
    public List<HoaDonChiTiet> getHDCTByHDID(Integer hoaDonId) {
        return hoaDonRepository.getHDCTByHDID(hoaDonId);
    }

    @Override
    public Page<HoaDon> SearchandPageHoaDon(int pageNo, int pageSize, String keyword) {
        List list = this.searchHoaDon(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<HoaDon>(list, pageable, this.searchHoaDon(keyword).size());
    }

}
