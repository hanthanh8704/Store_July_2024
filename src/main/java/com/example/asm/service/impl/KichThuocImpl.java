package com.example.asm.service.impl;

import com.example.asm.model.KichThuoc;
import com.example.asm.model.SanPham;
import com.example.asm.repository.KichThuocRepository;
import com.example.asm.service.KichThuocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KichThuocImpl implements KichThuocService {
    @Autowired
    KichThuocRepository kichThuocRepository;
    @Override
    public List<KichThuoc> getAllKichThuoc() {
        return kichThuocRepository.findAll();
    }

    @Override
    public void saveKichThuoc(KichThuoc kichThuoc) {
        kichThuocRepository.save(kichThuoc);
    }

    @Override
    public void deleteKichThuoc(Integer id) {
        kichThuocRepository.deleteById(id);
    }

    @Override
    public Optional<KichThuoc> findKichThuoc(Integer id) {
        return kichThuocRepository.findById(id);
    }

    @Override
    public Page<KichThuoc> pageKichThuoc(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1,pageSize);
        return kichThuocRepository.findAll(pageable);
    }

    @Override
    public List<KichThuoc> searchKichThuoc(String keyword) {
        return kichThuocRepository.searchKichThuocByTen(keyword);
    }

    @Override
    public Page<KichThuoc> SearchandPageKichThuoc(int pageNo, int pageSize, String keyword) {
        List list = this.searchKichThuoc(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<KichThuoc>(list, pageable, this.searchKichThuoc(keyword).size());
    }
}
