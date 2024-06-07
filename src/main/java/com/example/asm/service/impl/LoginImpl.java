package com.example.asm.service.impl;

import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import com.example.asm.repository.LoginRepository;
import com.example.asm.repository.NhanVienRepository;
import com.example.asm.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginImpl implements LoginService {
    @Autowired
    private LoginRepository loginRepository;
    @Override
    public NhanVien findByUsernameAndPassword(String username, String password) {
        return loginRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public KhachHang findKhachHangByUsernameAndPassword(String username, String password) {
        return loginRepository.findKhachHangByUsernameAndPassword(username, password);
    }
}
