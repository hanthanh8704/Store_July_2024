package com.example.asm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Mã sản phẩm không được bỏ trống")
    @Column(name = "ma")
    private String ma;
    @NotBlank(message = "Tên sản phẩm không được bỏ trống")
    @Column(name = "ten")
    private String ten;
    @NotNull(message = "Trạng thái phải là lựa chọn")
    @Column(name = "trang_thai")
    private Integer trangThai;

}