package com.example.asm.service.impl;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.repository.HoaDonChiTietRepository;
import com.example.asm.repository.SanPhamChiTietRepository;
import com.example.asm.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamChiTietImpl implements SanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Override
    public List<SanPhamChiTiet> getAllSanPhamChiTiet() {
        return sanPhamChiTietRepository.findAll();
    }

    @Override
    public void saveSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet) {
        sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

    @Override
    public void deleteSanPhamChiTiet(Integer id) {
        sanPhamChiTietRepository.deleteById(id);
    }

    @Override
    public Optional<SanPhamChiTiet> findSanPhamChiTietById(Integer id) {
        return sanPhamChiTietRepository.findById(id);
    }

    @Override
    public Page<SanPhamChiTiet> findPageSanPhamChiTiet(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return sanPhamChiTietRepository.findAll(pageable);
    }

    @Override
    public List<SanPhamChiTiet> searchSanPhamChiTiet(String keyword) {
        return sanPhamChiTietRepository.searchSanPhamChiTietByTen(keyword);
    }

    @Override
    public Page<SanPhamChiTiet> SearchandPageSanPhamChiTiet(int pageNo, int pageSize, String keyword) {
        List list = this.searchSanPhamChiTiet(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<SanPhamChiTiet>(list, pageable, this.searchSanPhamChiTiet(keyword).size());
    }

    @Override
    public int getSoLuongTrongGioHangByIdCTSP(Integer idCTSP) {
        return sanPhamChiTietRepository.getSoLuongTrongGioHangByIdCTSP(idCTSP);
    }

    @Override
    public int getSoLuongTonByIDCTSP(Integer idCTSP) {
        return sanPhamChiTietRepository.getSoLuongTonByIDCTSP(idCTSP);
    }

    @Override
    public boolean checkAddGioHang(int idCtsp, int soLuongThemVaoGio) {
        int soLuongTonSP = getSoLuongTonByIDCTSP(idCtsp);
        int soLuongTrongGio = getSoLuongTrongGioHangByIdCTSP(idCtsp);
        int soLuongTon = soLuongTrongGio + soLuongTonSP;
        if (soLuongThemVaoGio < 1) {
            return false;
        } else if (soLuongThemVaoGio > soLuongTon) {
            return false;
        }

        return true;
    }

    @Override
    public SanPhamChiTiet findSPCTById(Integer id) {
        return sanPhamChiTietRepository.findSPCTById(id);
    }
}
