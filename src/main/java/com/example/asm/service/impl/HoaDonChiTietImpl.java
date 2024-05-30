package com.example.asm.service.impl;

import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.repository.HoaDonChiTietRepository;
import com.example.asm.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietImpl implements HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Override
    public List<HoaDonChiTiet> fillAllHDCT() {
        return hoaDonChiTietRepository.findAll();
    }
}
