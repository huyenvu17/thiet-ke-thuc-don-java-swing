package entity;

import java.math.BigDecimal;

/**
 * Entity class representing a NguyenLieuEntity (Ingredient) in the system
 */
public record NguyenLieuEntity(
        int id,
        String tenNguyenLieu,
        String donViTinh,
        BigDecimal donGia,
        int nhomThucPhamId
        ) {
}
