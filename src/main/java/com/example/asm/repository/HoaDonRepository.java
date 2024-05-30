package com.example.asm.repository;

import com.example.asm.model.HoaDon;
import com.example.asm.model.HoaDonChiTiet;
import com.example.asm.model.NhanVien;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT sp FROM HoaDon sp WHERE sp.nhanVien.ten LIKE %?1%")
    List<HoaDon> searchHoaDonByTen(String ten);

    @Query("SELECT h FROM HoaDonChiTiet h WHERE h.hoaDon.id = :hoaDonId")
    List<HoaDonChiTiet> getHDCTByHDID(@Param("hoaDonId") Integer hoaDonId);

    @Query("SELECT h FROM HoaDonChiTiet h")
    Page<HoaDonChiTiet> findAllPaging(Pageable pageable);

    @Query("SELECT h FROM HoaDon h WHERE h.id = :id")
    HoaDon findHoaDonById(Integer id);

    // Phương thức tính thành tiền ở hdct
    @Query("SELECT SUM(hdct.donGia * hdct.soLuong) FROM HoaDonChiTiet hdct WHERE hdct.hoaDon.id = :idHoaDon")
    BigDecimal findTongTienByHoaDonId(@Param("idHoaDon") Integer idHoaDon);

    // Phương thuưức thêm sản phẩm vào giỏ hàng
    @Procedure(name = "ThemSanPhamVaoHoaDonChiTiet")
    void themSanPhamVaoHoaDonChiTiet(@Param("idHD") Integer idHD, @Param("idCTSP") Integer idCTSP, @Param("soLuong") Integer soLuong);

    // Hàm này dùng để xóa sản phẩm khỏi giỏ hàng
    @Procedure(name = "XoaSanPhamKhoiGioHang")
    void xoaSanPhamKhoiGioHang(@Param("idHDCT") Integer idHDCT, @Param("idCTSP") Integer idCTSP);

    // Hàm này dùng để cập nhật số lượng sản phẩm trong giỏ hàng
    @Procedure(name = "CapNhatSanPhamTrongGioHang")
    void capNhatSanPhamTrongGioHang(@Param("idCTSP") Integer idCTSP, @Param("idHDCT") Integer idHDCT,
                                    @Param("soLuongThayDoi") Integer soLuongThayDoi,
                                    @Param("soLuongTrongGio") Integer soLuongTrongGio);
    // Hàm này dùng để thanh toán hóa đơn
    @Procedure(name = "THANHTOANHOADON")
    void thanhToanHoaDon(
            @Param("idHD") Integer idHD,
            @Param("idKH") Integer idKH,
            @Param("idNV") Integer idNV
    );
}
