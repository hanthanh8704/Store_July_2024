package com.example.asm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "kichthuoc")
public class KichThuoc {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull(message = "Mã không được bỏ trống")
    @Column(name = "ma")
    private String ma;
    @NotNull(message = "Tên không được bỏ trống")
    @Column(name = "ten")
    private String ten;
    @NotNull(message = "Trạng phải lựa chọn")
    @Column(name = "trang_thai")
    private Integer trangThai;
}
