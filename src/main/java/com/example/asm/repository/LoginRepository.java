package com.example.asm.repository;

import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface LoginRepository extends JpaRepository<NhanVien,Integer> {
    NhanVien findByUsernameAndPassword(String username, String password);
    KhachHang findKhachHangByUsernameAndPassword(String username, String password);

}
