package com.example.asm.util;

import com.example.asm.model.KhachHang;
import com.example.asm.model.NhanVien;
import org.springframework.stereotype.Component;

/**
 * Class này dùng để quản lý phiên đăng nhập
 *
 */
@Component
public class LoginSession {
    public static NhanVien nhanVien = null;
    public static KhachHang khachHang = null;
    // Kiểm tra xem đã đăng nhập chưa
    public static boolean isLogin (){
        return LoginSession.nhanVien != null;
    }
    // Set giá trị cho biến nhân viên khi đăng nhập thành công
    public static void setNhanVien(NhanVien nhanVien) {
        LoginSession.nhanVien = nhanVien;
    }
    // Kiểm tra xem nhân viên đó là admin hay không
    public static boolean isAdmin() {
        return LoginSession.nhanVien.getUsername().equals("Admin");
    }
    public static void logout() {
        LoginSession.nhanVien = null;
    }
    // Kiểm tra xem có phải là khách hàng đã đăng nhập chưa
    public static boolean isKhachHangLogin() {
        return LoginSession.khachHang!= null;
    }

    // Set giá trị cho biến khách hàng khi đăng nhập thành công
    public static void setKhachHang(KhachHang khachHang) {
        LoginSession.khachHang = khachHang;
    }

    // Kiểm xem có phải là khách hàng hay không
}
