package com.example.asm.repository;

import com.example.asm.model.Cart;
import com.example.asm.model.CartDetail;
import com.example.asm.model.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    // Hàm
    CartDetail findByCartAndSanPhamChiTiet(Cart cart, SanPhamChiTiet sanPhamChiTiet);

    CartDetail findBySanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet);

    //Boolean existsSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet);
    // Phương thức này trả về một danh sách List<Cart> của tất cả các giỏ hàng của một người dùng cụ thể.
    List<CartDetail> findByCart(Cart cart);
//    Orderdetails findByProductsId(int productId);
    CartDetail findBySanPhamChiTietId(int productId);

    CartDetail findByCartId(int hoadonId);
}
