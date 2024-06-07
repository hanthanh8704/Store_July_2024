package com.example.asm.repository;

import com.example.asm.model.Category;
import com.example.asm.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CateogoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT sp FROM Category sp WHERE sp.ten LIKE %?1%")
    List<Category> searchSanPhamByTen(String ten);

    Category findByTen(String ten);
}
