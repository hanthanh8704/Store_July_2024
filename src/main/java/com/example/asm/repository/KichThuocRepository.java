package com.example.asm.repository;

import com.example.asm.model.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface KichThuocRepository extends JpaRepository<KichThuoc,Integer> {
    @Query("select sp from KichThuoc sp where sp.ten LIKE %?1%")
    List<KichThuoc> searchKichThuocByTen(String ten);

    KichThuoc findByTen(String ten);
}
