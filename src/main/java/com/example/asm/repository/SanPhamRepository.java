package com.example.asm.repository;

import com.example.asm.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("SELECT sp FROM SanPham sp WHERE sp.ten LIKE %?1%")
    List<SanPham> searchSanPhamByTen(String ten);

    SanPham findByTen(String ten);
}
