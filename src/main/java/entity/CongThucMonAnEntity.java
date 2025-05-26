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
        String tenMon,
        String tenNguyenLieu,
        String donViTinh
) {
    public CongThucMonAnEntity(int id, int monAnId, int nguyenLieuId, Double khoiLuong) {
        this(id, monAnId, nguyenLieuId, khoiLuong, null, null, null);
    }
} 