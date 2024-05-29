package com.example.asm.service.impl;

import com.example.asm.model.MauSac;
import com.example.asm.model.SanPham;
import com.example.asm.repository.MauSacRepository;
import com.example.asm.service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MauSacImpl implements MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;

    @Override
    public List<MauSac> getAllMauSac() {
        return mauSacRepository.findAll();
    }

    @Override
    public void saveMauSac(MauSac mauSac) {
        mauSacRepository.save(mauSac);
    }

    @Override
    public void deleteMauSac(Integer id) {
        mauSacRepository.deleteById(id);
    }

    @Override
    public Optional<MauSac> findMauSacById(Integer id) {
        return mauSacRepository.findById(id);
    }

    @Override
    public Page<MauSac> pageMauSac(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return mauSacRepository.findAll(pageable);
    }

    @Override
    public List<MauSac> searchMauSac(String keyword) {
        return mauSacRepository.searchMauSacByTen(keyword);
    }

    @Override
    public Page<MauSac> SearchandPageMauSac(int pageNo, int pageSize, String keyword) {
        List list = this.searchMauSac(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Integer start = (int) pageable.getOffset();

        Integer end = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset() + pageable.getPageSize());

        list = list.subList(start, end);
        return new PageImpl<MauSac>(list, pageable, searchMauSac(keyword).size());
    }
}
