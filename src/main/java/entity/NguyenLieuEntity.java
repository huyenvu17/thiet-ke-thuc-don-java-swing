package entity;

import java.math.BigDecimal;

/**
 * Entity class:  NguyenLieuEntity
 */
public record NguyenLieuEntity(
        int id,
        String tenNguyenLieu,
        Double khoiLuong,
        BigDecimal donGia,
        int nhomThucPhamId
        ) {
}
