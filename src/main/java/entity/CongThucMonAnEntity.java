package entity;

import java.math.BigDecimal;

/**
 * Entity representing a recipe ingredient (công thức món ăn) in the database.
 * Implemented as a record for immutability and automatic implementation of common methods.
 */
public record CongThucMonAnEntity(
        int id,
        int monAnId,
        int nguyenLieuId,
        BigDecimal khoiLuong,
        // Display fields (not in the database table but joined)
        String tenMon,
        String tenNguyenLieu,
        String donViTinh
) {
    /**
     * Constructor with only the database fields (no display fields)
     */
    public CongThucMonAnEntity(int id, int monAnId, int nguyenLieuId, BigDecimal khoiLuong) {
        this(id, monAnId, nguyenLieuId, khoiLuong, null, null, null);
    }
} 