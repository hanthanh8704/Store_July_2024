package com.example.asm.repository;

import com.example.asm.model.SanPham;
import com.example.asm.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    @Query("SELECT sp FROM SanPhamChiTiet sp WHERE sp.sanPham.ten LIKE %?1%")
    List<SanPhamChiTiet> searchSanPhamChiTietByTen(String ten);
}
