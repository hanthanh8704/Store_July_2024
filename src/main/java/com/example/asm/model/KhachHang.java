package com.example.asm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "khachhang")
public class KhachHang {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã khách hàng không được bỏ trống")
    @Column(name = "ma_khach_hang")
    private String maKh;

    @NotBlank(message = "Tên khách hàng không được bỏ trống")
    @Column(name = "ten")
    private String ten;

    @NotBlank(message = "Số điện thoại không được bỏ trống")
    @Column(name = "sdt")
    private String sdt;

    @NotNull(message = "Trạng thái phải được lựa chọn")
    @Column(name = "trang_thai")
    private Integer trangThai;
}
