package com.example.asm.repository;

import com.example.asm.model.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
    // Hàm này dùng để kiểm tra xem sản phẩm chi tiết đã tồn tại trong hóa đơn chi tiết chưa
    boolean existsByHoaDonIdAndSanPhamChiTietId(Integer hoaDonId, Integer sanPhamChiTietId);
    // Hàm này dùng để cộng dồn sản phẩm chi tiết nếu có id trùng với id sản phẩm sẽ được rh
    HoaDonChiTiet findByHoaDonIdAndSanPhamChiTietId(Integer hoaDonId, Integer sanPhamChiTietId);
}
