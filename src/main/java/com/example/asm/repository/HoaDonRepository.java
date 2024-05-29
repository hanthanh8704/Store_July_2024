package com.example.asm.repository;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
    @Query("SELECT sp FROM HoaDon sp WHERE sp.nhanVien.ten LIKE %?1%")
    List<HoaDon> searchHoaDonByTen(String ten);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.id = :hoaDonId")
    List<HoaDonChiTiet> getHDCTByHDID(@Param("hoaDonId") Integer hoaDonId);

    @Query("SELECT h FROM HoaDonChiTiet h")
    Page<HoaDonChiTiet> findAllPaging(Pageable pageable);
}
