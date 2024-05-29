package com.example.asm.repository;

import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("SELECT sp FROM KhachHang sp WHERE sp.ten LIKE %?1%")
    List<KhachHang> searchKhachHangByTen(String ten);
}
