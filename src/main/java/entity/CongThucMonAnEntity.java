package entity;

/**
 *
 * @author ADMIN
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
    public CongThucMonAnEntity(int id, int monAnId, int nguyenLieuId, Double khoiLuong) {
        this(id, monAnId, nguyenLieuId, khoiLuong, null, null, null);
    }
} 