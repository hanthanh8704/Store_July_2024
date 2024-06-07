package com.example.asm.service;

import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginService {
    NhanVien findByUsernameAndPassword(String username, String password);

    @Query("SELECT k FROM KhachHang k WHERE k.username = :username AND k.password = :password")
    KhachHang findKhachHangByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
