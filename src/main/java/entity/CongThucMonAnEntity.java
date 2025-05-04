package entity;

import java.math.BigDecimal;

/**
 * Entity record representing a CongThucMonAn (Dish Recipe) in the system
 */
public record CongThucMonAnEntity(
    int id,
    int monAnId,
    int nguyenLieuId,
    BigDecimal khoiLuong
) {} 