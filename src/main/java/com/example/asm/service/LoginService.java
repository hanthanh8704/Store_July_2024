package com.example.asm.service;

import com.example.asm.model.NhanVien;

public interface LoginService {
    NhanVien findByUsernameAndPassword(String username, String password);
}
