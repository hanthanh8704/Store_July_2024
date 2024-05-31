package com.example.asm.repository;

import com.example.asm.model.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    @Query("Select ms FROM MauSac ms WHERE ms.ten LIKE %?1%")
    List<MauSac> searchMauSacByTen(String ten);

    MauSac findByTen(String ten);
}
