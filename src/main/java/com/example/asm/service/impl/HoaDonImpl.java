package com.example.asm.service.impl;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.repository.HoaDonChiTietRepository;
import com.example.asm.repository.HoaDonRepository;
import com.example.asm.repository.SanPhamChiTietRepository;
import com.example.asm.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonImpl implements HoaDonService {
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    public HoaDonImpl(SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    @Override
    public List<HoaDon> fillAllHoaDon() {
        return hoaDonRepository.findAll();
    }

    @Override
    public void saveHoaDon(HoaDon hoaDon) {
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public Optional<HoaDon> findHoaDonById(Integer id) {
        return hoaDonRepository.findById(id);
    }

    @Override
    public HoaDon findById(Integer id) {
        return hoaDonRepository.findHoaDonById(id);
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

    @Override
    public BigDecimal getTongTienByIdHoaDon(Integer idHoaDon) {
        return hoaDonRepository.findTongTienByHoaDonId(idHoaDon);
    }

    @Override
    public BigDecimal calculateTotalAmount() {
        List<HoaDonChiTiet> listHDCT = hoaDonChiTietRepository.findAll(); // Hàm này trả về danh sách tất cả hóa đơn chi tiết
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (HoaDonChiTiet hdct : listHDCT) {
            totalAmount = totalAmount.add(hdct.getThanhTien());
        }
        return totalAmount;
    }

    @Override
    public void themSanPhamVaoHoaDonChiTiet(Integer idHD, Integer idCTSP, Integer soLuong) {
        hoaDonRepository.themSanPhamVaoHoaDonChiTiet(idHD, idCTSP, soLuong);
    }

    @Override
    public void xoaSanPhamKhoiGioHang(Integer idHDCT, Integer idCTSP) {
        hoaDonRepository.xoaSanPhamKhoiGioHang(idHDCT, idCTSP);
    }

    @Override
    public void capNhatSanPhamTrongGioHang(Integer idCTSP, Integer idHDCT, Integer soLuongThayDoi, Integer soLuongTrongGio) {
        hoaDonRepository.capNhatSanPhamTrongGioHang(idCTSP, idHDCT, soLuongThayDoi, soLuongTrongGio);
    }

    @Override
    public void thanhToanHoaDon(Integer idHD, Integer idKH, Integer idNV) {
        hoaDonRepository.thanhToanHoaDon(idHD, idKH, idNV);
    }

    @Override
    public boolean kiemTraSanPhamTonTaiTrongGioHang(Integer id, Integer spctId) {
        return hoaDonChiTietRepository.existsByHoaDonIdAndSanPhamChiTietId(id, spctId);
    }
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    @Override
    public void congDonSoLuongSanPhamTrongGioHang(Integer id, Integer spctId, Integer soLuong) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findByHoaDonIdAndSanPhamChiTietId(id, spctId);
        if (hoaDonChiTiet != null) {
            // Nếu sản phẩm đã tồn tại trong giỏ hàng, cộng dồn số lượng
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + soLuong);
        }
        hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

}
