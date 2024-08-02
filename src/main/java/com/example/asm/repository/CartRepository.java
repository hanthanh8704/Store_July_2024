package com.example.asm.repository;

import com.example.asm.model.Cart;
import com.example.asm.model.KhachHang;
import com.example.asm.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CartRepository extends JpaRepository<Cart,Integer> {

    @Query("SELECT c FROM Cart c WHERE c.khachHang = :khachHang")
    Cart findByKhachHang(@Param("khachHang") KhachHang khachHang);
    // Kiểm tra xem bảng ghi cart đã tồn tại trong khách hàng hay chưa
    Boolean existsByKhachHang(KhachHang khachHang);
    // Phương thức này trả về một danh sách List<Cart> của tất cả các giỏ hàng của một người dùng cụ thể.
    //List<Cart> findByKhachHang(KhachHang khachHang);
}
