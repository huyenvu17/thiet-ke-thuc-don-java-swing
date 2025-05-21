package entity;

/**
 * Entity class: CongThucMonAnEntity
 */
public record CongThucMonAnEntity(
        int id,
        int monAnId,
        int nguyenLieuId,
        Double khoiLuong,
        // Display fields (not in the database table but joined)
        String tenMon,
        String tenNguyenLieu,
        String donViTinh
) {
    /**
     * Constructor with only the database fields (no display fields)
     */
    public CongThucMonAnEntity(int id, int monAnId, int nguyenLieuId, Double khoiLuong) {
        this(id, monAnId, nguyenLieuId, khoiLuong, null, null, null);
    }
} 