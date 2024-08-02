package com.example.asm.service.impl;

import com.example.asm.model.CartDetail;
import com.example.asm.model.HoaDon;
import com.example.asm.model.SanPhamChiTiet;
import com.example.asm.repository.CartDetailRepository;
import com.example.asm.repository.SanPhamChiTietRepository;
import com.example.asm.service.CartsService;
import com.example.asm.util.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@SessionScope
@Service
public class CartImpl implements CartsService {
    private Timestamp timestamp;
    @Autowired
    private SanPhamChiTietRepository productsRepository;

    @Autowired
    private CartDetailRepository orderDetailsRepository;

    @Override
    public List<CartDetail> getItems() {
        // Trả về tất cả các mặt hàng trong giỏ hàng từ cơ sở dữ liệu
        return orderDetailsRepository.findAll();
    }

    @Override
    public void add(int id) {
        // Tìm sản phẩm trong giỏ hàng
        CartDetail item = orderDetailsRepository.findBySanPhamChiTietId(id);
        // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
            orderDetailsRepository.save(item);
        } else {
            // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
            SanPhamChiTiet product = productsRepository.findById(id).orElse(null);
            if (product != null) {
                CartDetail newItem = new CartDetail(null, null, product, 1, product.getDonGia(), null, null);
                orderDetailsRepository.save(newItem);
            }
        }
    }

    @Override
    public void remove(int id) {
        // Xóa sản phẩm khỏi giỏ hàng theo ID sản phẩm
        CartDetail item = orderDetailsRepository.findBySanPhamChiTietId(id);
        if (item != null) {
            orderDetailsRepository.delete(item);
        }
    }

    @Override
    public void clear() {
        // Xóa toàn bộ giỏ hàng
        orderDetailsRepository.deleteAll();
    }

    @Override
    public void update(int id, int qty) {
        // Cập nhật số lượng sản phẩm trong giỏ hàng
        CartDetail item = orderDetailsRepository.findBySanPhamChiTietId(id);
        if (item != null) {
            item.setQuantity(qty);
            orderDetailsRepository.save(item);
        }
    }

    @Override
    public int getTotal() {
        // Tính tổng số lượng sản phẩm trong giỏ hàng
        return orderDetailsRepository.findAll()
                .stream()
                .mapToInt(CartDetail::getQuantity)
                .sum();
    }

    @Override
    public int getAmount() {
        // Tính tổng giá trị của giỏ hàng
        return orderDetailsRepository.findAll()
                .stream()
                .mapToInt(item -> item.getQuantity() * item.getPrice().intValue())
                .sum();
    }
}
